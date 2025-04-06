import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.jetbrains.skia.Image
import java.util.prefs.Preferences
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import chat.database.ChatDatabase

actual suspend fun createDatabaseDriver(): SqlDriver {
//    You are using JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY),
    //    which creates an in-memory database that disappears when the app closes.
//    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
//        .also { AppDatabase.Schema.create(it).await() }

    val driver = JdbcSqliteDriver( "jdbc:sqlite:objects.db")
    ChatDatabase.Schema.create(driver)
    return driver
}

actual fun showAlert(message: String) {
    javax.swing.JOptionPane.showMessageDialog(null, message, "Notification", javax.swing.JOptionPane.INFORMATION_MESSAGE)
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

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
