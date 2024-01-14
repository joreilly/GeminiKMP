import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.await
import kotlin.js.Promise


@Composable
actual fun ImagePicker(
    show: Boolean,
    initialDirectory: String?,
    title: String?,
    onImageSelected: ImageFileImported,
) {
    LaunchedEffect(show) {
        if (show) {
            val data = importImageFile()
            onImageSelected("", data)
        }
    }
}


private suspend fun importImageFile(): String? {
    return try {
        pickFile().await<JsString>().toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun pickFile(): Promise<JsString> = js(
    """
    (async () => {
    return new Promise((resolve, reject) => {
      const input = document.createElement('input');
      input.type = 'file';
      input.onchange = () => {
        const file = input.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = () => {
                let encoded = reader.result.toString().replace(/^data:(.*,)?/, '');
                if ((encoded.length % 4) > 0) {
                    encoded += '='.repeat(4 - (encoded.length % 4));
                }          
                resolve(encoded);
          }
          reader.onerror = () => {
                reject(reader.error);
                resolve("")
          }
          reader.readAsDataURL(file);
        } else {
          reject(new Error('No file was selected'));
        }
      };
      input.click();
    });
    })()
  """,
)
