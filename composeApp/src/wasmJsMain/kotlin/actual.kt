import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun getPlatform(): Platform {
    return Platform.Web("Web wasm")
}

actual fun createAppDataStore() = appDataStore { dataStoreFileName }
