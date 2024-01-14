import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow

import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlin.js.Promise


@OptIn(ExperimentalComposeUiApi::class)
fun main() {


    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()

/*
        val coroutineScope = rememberCoroutineScope()
        val api = remember { GeminiApi() }


        var text by remember { mutableStateOf("") }

        Column {

            Text(text)


            TextButton(
                onClick = {
                    coroutineScope.launch {
                        val imageData = importImageFile() ?: ""
                        val response = api.generateContent("What is this picture?", imageData)
                        text = response.toString()
                    }
                },

                modifier = Modifier
                    .weight(3f)
                    .padding(all = 4.dp)
            ) {
                Text("Pick File")
            }
        }
*/
    }
}
