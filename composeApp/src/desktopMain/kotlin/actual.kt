import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.jetbrains.skia.Image
import java.util.prefs.Preferences

actual fun getPlatform(): Platform {
    return Platform.Desktop(
        System.getProperty("os.name") + " " + System.getProperty("os.version")
    )
}

actual fun getDataSettings(): Settings {
    return PreferencesSettings(Preferences.userRoot().node("app_preferences"))
}

actual fun getDataSettingsFlow(): ObservableSettings? {
    return PreferencesSettings(Preferences.userRoot().node("app_preferences"))
}
