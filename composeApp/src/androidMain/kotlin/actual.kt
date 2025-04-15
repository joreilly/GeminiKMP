
import android.content.Context
import android.os.Build
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.johnoreilly.gemini.MainActivity

actual fun getPlatform(): Platform {
    return Platform.Android("Android ${Build.VERSION.SDK_INT}")
}

actual fun getDataSettings(): Settings {
    val sharedPreferences =
        MainActivity.instance.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}

actual fun getDataSettingsFlow(): ObservableSettings? {
    val sharedPreferences =
        MainActivity.instance.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}
