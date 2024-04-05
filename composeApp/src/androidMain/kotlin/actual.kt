import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}


@OptIn(ExperimentalEncodingApi::class)
@Composable
actual fun ImagePicker(
    show: Boolean,
    initialDirectory: String?,
    title: String?,
    onImageSelected: ImageFileImported,
) {
    val coroutineScope = rememberCoroutineScope()
    val contentResolver = LocalContext.current.contentResolver

    val fileExtensions = listOf("jpg", "png")
    FilePicker(show = show, fileExtensions = fileExtensions) { file ->
        coroutineScope.launch {
            file?.let {
                val imageData = getImageByteArray(file, contentResolver)
                imageData?.let {
                    onImageSelected(file.path, imageData)
                }
            }
        }
    }
}


fun getImageByteArray(file: MPFile<Any>, contentResolver: ContentResolver): ByteArray? {
    val uri = Uri.parse(file.path)
    val stream = contentResolver.openInputStream(uri)
    stream?.let {
        val bytes = stream.readBytes()
        stream.close()
        return bytes
    } ?: return null
}