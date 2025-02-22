import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.jetbrains.skia.Image

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

actual fun getPlatform(): Platform {
    return Platform.Desktop(
        System.getProperty("os.name") + " " + System.getProperty("os.version")
    )
}

actual fun createAppDataStore(): DataStore<Preferences> {
    return appDataStore(
        producePath = { dataStoreFileName }
    )
}
