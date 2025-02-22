import androidx.compose.ui.graphics.ImageBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal fun appDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "dice.preferences_pb"
sealed class Platform {
    data class Android(val message: String) : Platform()
    data class Ios(val message: String) : Platform()
    data class Desktop(val message: String) : Platform()
    data class Web(val message: String) : Platform()
}

expect fun getPlatform(): Platform

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun createAppDataStore(): DataStore<Preferences>
