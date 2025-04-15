
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class WebJsonDB : JsonDatabase {

    override fun  createData(tableName: String, data: ListString): Boolean {
        return try {
            val json = Json.encodeToString(data)
            localStorage.setItem(tableName, json)
            true
        } catch (e: Exception) {
            println("createData error $e")
            false
        }
    }

    override fun deleteData(tableName: String): Boolean {
        return try {
            localStorage.removeItem(tableName)
            true
        } catch (e: Exception) {
            println("createData error $e")
            false
        }
    }

    override fun getData(tableName: String): ListString {
        return try {
            val jsonString = localStorage.getItem(tableName) ?: "[]"
            Json.decodeFromString<String>(jsonString)
        } catch (e: Exception) {
            println("createData error $e")
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
