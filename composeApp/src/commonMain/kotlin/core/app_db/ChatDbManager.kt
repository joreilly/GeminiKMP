package core.app_db

import chat.database.ChatDatabase
import chat.database.ChatDatabaseQueries
import core.models.ChatMessage
import createDatabaseDriver

object ChatDbManager {

    private var database: ChatDatabase? = null
    private var dbQuery: ChatDatabaseQueries? = null

    suspend fun initializeDatabase() {
        database = ChatDatabase(createDatabaseDriver())
        dbQuery = database?.chatDatabaseQueries
    }

    suspend fun insertObjectToStore(chatDataClass: ChatMessage) {
        dbQuery?.insertChat(
            message = chatDataClass.message,
            sender = chatDataClass.sender,
            time = chatDataClass.time,
            id = chatDataClass.id
        )
    }

    fun getObjectToStores(): List<ChatMessage> {
        return dbQuery?.getAllChat()?.executeAsList()?.map {
            ChatMessage(
                id = it.id,
                message = it.message,
                sender = it.sender,
                time = it.time
            )
        } ?: emptyList()
    }

    suspend fun deleteAllObjectFromStore() {
        dbQuery?.deleteAllChat()
    }
}