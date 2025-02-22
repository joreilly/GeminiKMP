package app_db

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import createAppDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single


object DataStoreManager {
    private val preference = createAppDataStore()
    const val THEME_KEY = "theme"

    suspend fun <T : Any> setValue(key: String, value: T) {
        preference.edit {
            val data = stringPreferencesKey(key)
            it[data] = value.toString()
        }
    }

    suspend fun getValue(key: String): String? {
        val data = stringPreferencesKey(key)
        val preferences = preference.data.single()
        return preferences[data]
    }

    fun getValueFlow(key: String): Flow<String?> {
        val data = stringPreferencesKey(key)
        return preference.data.map { it[data] }.flowOn(Dispatchers.Default)
    }

    suspend fun deleteValue(key: String) {
        preference.edit {
            val data = stringPreferencesKey(key)
            it.remove(data)
        }
    }
}