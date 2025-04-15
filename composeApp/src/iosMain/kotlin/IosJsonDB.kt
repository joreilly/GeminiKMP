import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

class IosJsonDB : JsonDatabase {


    private fun getFilePath(tableName: String): String {
        val dir = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        ).first() as String
        return "$dir/$tableName"
    }

    @OptIn(BetaInteropApi::class)
    override fun createData(tableName: String, data: ListString): Boolean {
        return try {
            val json = Json.encodeToString(data)
            val path = getFilePath(tableName)
            val nsData = NSString.create(string = json).dataUsingEncoding(NSUTF8StringEncoding)
            nsData?.writeToFile(path, atomically = true) ?: false
        } catch (e: Exception) {
            println("createData error: ${e.message}")
            false
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun deleteData(tableName: String): Boolean {
        return try {
            val path = getFilePath(tableName)
            val fileManager = NSFileManager.defaultManager
            fileManager.removeItemAtPath(path, null)
            true
        } catch (e: Exception) {
            println("deleteData error: ${e.message}")
            false
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun getData(tableName: String): ListString {
        return try {
            val path = getFilePath(tableName)
            val nsString = NSString.stringWithContentsOfFile(
                path,
                encoding = NSUTF8StringEncoding,
                error = null
            )
            if (nsString != null) {
                Json.decodeFromString<String>(nsString)
            }else "[]"
        } catch (e: Exception) {
            println("readRawJson error: ${e.message}")
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