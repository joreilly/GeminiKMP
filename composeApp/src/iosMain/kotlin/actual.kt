import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import org.jetbrains.skia.Image
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIAlertController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

actual fun getJsonDatabase(): JsonDatabase = IosJsonDB()

actual fun showAlert(message: String) {
    val alertController = UIAlertController
        .alertControllerWithTitle(null, message, 1000L)
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(alertController, true, null)

}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun getPlatform(): Platform {
    return Platform.Ios(
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    )
}

actual fun getDataSettings(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}

actual fun getDataSettingsFlow(): ObservableSettings? {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}


actual fun getTextToSpeech(): TextToSpeech = IosTextToSpeech()
