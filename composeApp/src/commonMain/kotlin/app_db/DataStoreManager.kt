package app_db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.set
import getDataSettings
import getDataSettingsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import createAppDataStore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOn
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.single
//
//
//object DataStoreManager {
//    private val preference = createAppDataStore()
//    const val THEME_KEY = "theme"
//
//    suspend fun  setValue(key: String, value: String) {
//        preference.edit {
//            val data = stringPreferencesKey(key)
//            it[data] = value
//        }
//    }
//
//    suspend fun getValue(key: String): String? {
//        val data = stringPreferencesKey(key)
//        val preferences = preference.data.single()
//        return preferences[data]
//    }
//
//    fun getValueFlow(key: String): Flow<String?> {
//        val data = stringPreferencesKey(key)
//        return preference.data.map { it[data] }.flowOn(Dispatchers.Default)
//    }
//
//    suspend fun deleteValue(key: String) {
//        preference.edit {
//            val data = stringPreferencesKey(key)
//            it.remove(data)
//        }
//    }
//}

@OptIn(ExperimentalSettingsApi::class)
object DataManager {
    private val settings = getDataSettings()
    private val settingsFlow = getDataSettingsFlow()
    const val THEME_KEY = "theme"

    // Save functions
    fun setValue(key: String, value: String) = settings.set(key = key, value = value)

    // Regular get functions
    fun getValue(key: String): String = settings.getString(key, "null")

    // **Flow functions**
//    fun getValueFlow(key: String): Flow<String?> {
//        return settingsFlow?.getStringOrNullFlow(key = key)
//            ?: flow {
//                while (true) {
//                    emit(getValue(key))
//                }
//            }
//    }
    fun getValueFlow(key: String): Flow<String?> {
        return settingsFlow?.getStringOrNullFlow(key = key) // Reactive flow if available
            ?: flowOf(getValue(key)) // Emit a single value if no flow is available
    }

    fun clear() {
        settings.clear()
        settingsFlow?.clear()
    }
}