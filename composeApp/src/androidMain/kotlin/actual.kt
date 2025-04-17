import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.johnoreilly.gemini.AndroidJsonDatabase
import dev.johnoreilly.gemini.AndroidTextToSpeech
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


actual fun showAlert(message: String) {
    android.widget.Toast.makeText(
        MainActivity.instance,
        message,
        android.widget.Toast.LENGTH_SHORT
    ).show()
}


actual fun getJsonDatabase(): JsonDatabase = AndroidJsonDatabase()

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return bitmap.asImageBitmap()
}

actual fun getTextToSpeech(): TextToSpeech = AndroidTextToSpeech()
