import androidx.compose.ui.graphics.ImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow


sealed class Platform {
    data class Android(val message: String) : Platform()
    data class Ios(val message: String) : Platform()
    data class Desktop(val message: String) : Platform()
    data class Web(val message: String) : Platform()
}

interface JsonDatabase {
    fun createData(tableName: String, data: ListString): Boolean
    fun getData(tableName: String): ListString
    fun getDataFlow(tableName: String): Flow<ListString>
    fun deleteData(tableName: String): Boolean
}

interface TextToSpeech{
    suspend fun speak(text: String)
    suspend fun stop()
}

expect fun getPlatform(): Platform

expect fun getJsonDatabase(): JsonDatabase

expect fun getTextToSpeech(): TextToSpeech

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun getDataSettings(): Settings

expect fun getDataSettingsFlow(): ObservableSettings?

expect fun showAlert(message: String)

typealias ListString = String



