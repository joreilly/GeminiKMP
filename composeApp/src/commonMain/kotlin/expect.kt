import androidx.compose.ui.graphics.ImageBitmap
import com.russhwolf.settings.ObservableSettings
import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.Settings


sealed class Platform {
    data class Android(val message: String) : Platform()
    data class Ios(val message: String) : Platform()
    data class Desktop(val message: String) : Platform()
    data class Web(val message: String) : Platform()
}


expect fun getPlatform(): Platform


expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun getDataSettings(): Settings

expect fun getDataSettingsFlow(): ObservableSettings?


expect suspend fun createDatabaseDriver(): SqlDriver

expect fun showAlert(message: String):Unit
