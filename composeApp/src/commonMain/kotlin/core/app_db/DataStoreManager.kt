package core.app_db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.set
import getDataSettings
import getDataSettingsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalSettingsApi::class)
object DataManager {
    private val settings = getDataSettings()
    private val settingsFlow = getDataSettingsFlow()
    const val THEME_KEY = "theme"

    // Save functions
    fun setValue(key: String, value: String) = settings.set(key = key, value = value)

    // Regular get functions
    fun getValue(key: String): String = settings.getString(key, "null")


    fun getValueFlow(key: String): Flow<String?> {
        return settingsFlow?.getStringOrNullFlow(key = key) // Reactive flow if available
            ?: flowOf(getValue(key)) // Emit a single value if no flow is available
    }

    fun clear(key: String) {
        settings.remove(key)
    }

    fun clear() {
        settings.clear()
        settingsFlow?.clear()
    }
}