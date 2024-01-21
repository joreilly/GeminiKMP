import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap


typealias ImageFileImported = (String, String?, ByteArray?) -> Unit



@Composable
expect fun GeminiMarkdown(content: String)

@Composable
expect fun ImagePicker(
    show: Boolean,
    initialDirectory: String? = null,
    title: String? = null,
    onImageSelected: ImageFileImported
)