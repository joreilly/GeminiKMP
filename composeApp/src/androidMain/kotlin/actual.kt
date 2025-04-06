import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.johnoreilly.gemini.MainActivity
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import chat.database.ChatDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}

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


actual suspend fun createDatabaseDriver(): SqlDriver {
    return AndroidSqliteDriver(
        schema = ChatDatabase.Schema.synchronous(),
        MainActivity.instance,
        "objects.db"
    )
}

actual fun showAlert(message: String) {
    android.widget.Toast.makeText(
        MainActivity.instance,
        message,
        android.widget.Toast.LENGTH_SHORT
    ).show()
}
