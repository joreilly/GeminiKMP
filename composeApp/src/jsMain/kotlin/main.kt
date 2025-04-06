import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady

//@OptIn(ExperimentalComposeUiApi::class)
//fun main() {
//    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
//        App()
//    }
//}
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        val body = document.body ?: return@onWasmReady
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
//        ComposeViewport(body) {
            App()
        }
    }
}