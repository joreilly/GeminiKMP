import androidx.compose.runtime.Composable



typealias ImageFileImported = (String, String?) -> Unit


@Composable
expect fun ImagePicker(
    show: Boolean,
    initialDirectory: String? = null,
    title: String? = null,
    onImageSelected: ImageFileImported
)