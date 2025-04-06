import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import chat.database.ChatDatabase
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.window
import org.jetbrains.skia.Image
import org.w3c.dom.Worker


actual suspend fun createDatabaseDriver(): SqlDriver {
    return WebWorkerDriver(
        Worker(
            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""") as String
        )
    ).also { ChatDatabase.Schema.awaitCreate(it) }
}

actual fun showAlert(message: String) {
    window.alert(message)
}


actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun getPlatform(): Platform {
    return Platform.Web("Web wasm")
}


actual fun getDataSettings(): Settings {
    return StorageSettings()
}

actual fun getDataSettingsFlow(): ObservableSettings? {
    return null
}
