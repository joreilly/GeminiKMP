import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

private const val INFO = """
    Gemini KMP folder and its contents can be deleted. (*/.geminikmp/GeminiKmp/*
    The .json file is been used by the GeminiKMP app to store chat history. If this folder is deleted
    and the app is not installed the app will automatically create this folder.
    
    from co-developer
        (Ohior)
     (\__/)
     (•ㅅ•)   *sniff sniff*
    / 　 づ
   *       *
  *  🐭 R A T *
 *         *
*************
"""

class DeskTopJsonDB : JsonDatabase {
    private fun getFilePath(tableName: String): File {
        val appDir = File(System.getProperty("user.home"), ".geminikmp/GeminiKMP")
            .apply { mkdirs() }
        val file = File(appDir.path, tableName)
        val infoFile = File(appDir.path, "INFO")
        if (!infoFile.exists()) infoFile.writeText(INFO)
        if (!file.exists()) file.createNewFile()
        return file
    }

    override fun createData(tableName: String, data: ListString): Boolean {
        return try {
            getFilePath(tableName).writeText(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    override fun deleteData(tableName: String): Boolean {
        return try {
            getFilePath(tableName).delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getData(tableName: String): ListString {
        return try {
            val file = getFilePath(tableName)
            file.readText().ifEmpty { "[]" }
        } catch (e: Exception) {
            println(e.toString())
            "[]"
        }
    }

    override fun getDataFlow(tableName: String): Flow<ListString> {
        return flow {
            val json = getData(tableName)
            emit(json)
        }
    }
}