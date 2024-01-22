import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.mikepenz.markdown.m3.Markdown
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap = Image.makeFromEncoded(this).toComposeImageBitmap()

@Composable
actual fun GeminiMarkdown(content: String) {
    Markdown(content)
}

@Composable
actual fun ImagePicker(
    show: Boolean,
    initialDirectory: String?,
    title: String?,
    onImageSelected: ImageFileImported,
) {
    val coroutineScope = rememberCoroutineScope()

    val fileExtensions = listOf("jpg", "png")
    FilePicker(show = show, fileExtensions = fileExtensions) { file ->
        coroutineScope.launch {
            file?.getFileByteArray()?.let { imageData ->
                onImageSelected(file.path, imageData)
            }
        }
    }
}
