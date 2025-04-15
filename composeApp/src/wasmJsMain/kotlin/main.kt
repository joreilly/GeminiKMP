import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()
    }
}
// For jsMain
//@OptIn(ExperimentalComposeUiApi::class)
//fun main() {
//    onWasmReady {
//        val body = document.body ?: return@onWasmReady
//    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
////        ComposeViewport(body) {
//            App()
//        }
//    }
//}