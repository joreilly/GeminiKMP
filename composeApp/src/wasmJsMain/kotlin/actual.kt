import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual fun getPlatform(): Platform {
    return Platform.Web("Web wasm")
}

actual fun getDataSettings(): Settings {
    return StorageSettings()
}

actual fun getDataSettingsFlow(): ObservableSettings? {
    return null
}
