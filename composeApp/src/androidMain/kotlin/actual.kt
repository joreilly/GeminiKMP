import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.johnoreilly.gemini.MainActivity

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}

actual fun getPlatform(): Platform {
    return Platform.Android("Android ${Build.VERSION.SDK_INT}")
}

actual fun createAppDataStore(): DataStore<Preferences> {
    return appDataStore(
        producePath = { MainActivity.instance.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}
