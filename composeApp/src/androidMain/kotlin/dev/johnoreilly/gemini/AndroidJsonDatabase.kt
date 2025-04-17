package dev.johnoreilly.gemini

import JsonDatabase
import ListString
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.File

class AndroidJsonDatabase : JsonDatabase {
    override fun createData(tableName: String, data: ListString): Boolean {
        return try {
            val jsonString = Json.encodeToString(data)
            MainActivity.instance.openFileOutput(tableName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    override fun getData(tableName: String): ListString {
        return try {
            val file = File(MainActivity.instance.filesDir, tableName)
            if (!file.exists()) return "[]"
            Json.decodeFromString<String>(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
            "[]"
        }
    }

    override fun getDataFlow(tableName: String): Flow<ListString> {
        return flow {
            val json = getData(tableName)
            emit(json)
        }
    }

    override fun deleteData(tableName: String): Boolean {
        return try {
            val file = File(MainActivity.instance.filesDir, tableName)
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}