import androidx.compose.ui.graphics.ImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings


sealed class Platform {
    data class Android(val message: String) : Platform()
    data class Ios(val message: String) : Platform()
    data class Desktop(val message: String) : Platform()
    data class Web(val message: String) : Platform()
}

expect fun getPlatform(): Platform

expect fun getDataSettings(): Settings
expect fun getDataSettingsFlow(): ObservableSettings?
