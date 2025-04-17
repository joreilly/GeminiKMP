import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.window
import org.jetbrains.skia.Image


//actual suspend fun createDatabaseDriver(): MySqlDriver {
////    return WebWorkerDriver(
////        Worker(
////            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""") as String
////        )
////    ).also { ChatDatabase.Schema.awaitCreate(it) }
//    throw NotImplementedError("createDatabaseDriver is not implemented on wasm, because sqldriver exist for only js")
//}
//actual suspend fun chatDatabase(): ChatDatabase? = null

actual fun getJsonDatabase(): JsonDatabase = WebJsonDB()

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

actual fun getTextToSpeech(): TextToSpeech = WebTextToSpeech()
