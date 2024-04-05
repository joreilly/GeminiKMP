import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap


typealias ImageFileImported = (filePath: String, data: ByteArray?) -> Unit

@Composable
expect fun ImagePicker(
    show: Boolean,
    initialDirectory: String? = null,
    title: String? = null,
    onImageSelected: ImageFileImported
)