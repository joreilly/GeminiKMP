package core.app_db

import JsonDatabase
import core.models.ChatMessage
import getJsonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object ChatDbManager {

    private val jsonDatabase: JsonDatabase = getJsonDatabase()
    private const val CHAT_TABLE = "chat.json"


    suspend fun insertObjectToStore(chatMessage: ChatMessage) {
        withContext(Dispatchers.Main) {
            println("DATA1 ")
            val data = jsonDatabase.getData(CHAT_TABLE)
            println("DATA2 : $data")
//            if (data.isEmpty()) data = "[]"
            val jdata = Json.decodeFromString<List<ChatMessage>>(data)
            val jData = jdata.toMutableList()
            jData.add(chatMessage)
            println("DATA3 : $jdata")
            jsonDatabase.createData(CHAT_TABLE, Json.encodeToString(jData))
        }
    }

    suspend fun getObjectToStores(): List<ChatMessage> {
        return withContext(Dispatchers.Main) {
            println("DATA ")
            val data = jsonDatabase.getData(CHAT_TABLE)
            println("DATA : $data")
            if (data.isEmpty()) return@withContext emptyList()
            Json.decodeFromString<List<ChatMessage>>(data).toMutableList()
        }
    }

    suspend fun deleteAllObjectFromStore() {
        withContext(Dispatchers.Main) {
            jsonDatabase.deleteData(CHAT_TABLE)
        }
    }
}