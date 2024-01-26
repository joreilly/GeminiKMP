import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.document
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.encoding.ExperimentalEncodingApi


actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

@Composable
actual fun GeminiMarkdown(content: String) {
    Text(content)
}


@OptIn(ExperimentalEncodingApi::class)
@Composable
actual fun ImagePicker(
    show: Boolean,
    initialDirectory: String?,
    title: String?,
    onImageSelected: ImageFileImported,
) {
    LaunchedEffect(show) {
        if (show) {
            val data = importImageFile()
            onImageSelected("", data)
        }
    }
}


private suspend fun importImageFile(): ByteArray? {
    return try {
        pickFile()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private suspend fun pickFile(): ByteArray? = suspendCoroutine { cont ->
    try {
        val input = document.createElement("input").apply {
            setAttribute("type", "file")
            setAttribute("accept", "image/*")
        } as HTMLInputElement

        input.onchange = {
            val file = input.files?.get(0)
            if (file != null) {
                val reader = FileReader()
                reader.onload = { event ->
                    val arrayBuffer = (event.target as FileReader).result as ArrayBuffer
                    val array = Uint8Array(arrayBuffer)

                    cont.resume(ByteArray(array.length) { array[it] })
                }
                reader.onerror = {
                    cont.resumeWithException(Exception(reader.error.toString()))
                }
                reader.readAsArrayBuffer(file)
            } else {
                cont.resumeWithException(Exception("No file was selected"))
            }
        }
        input.click()
    } catch (e: Exception) {
        cont.resumeWithException(e)
    }
}
