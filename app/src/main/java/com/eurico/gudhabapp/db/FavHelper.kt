package com.eurico.gudhabapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.eurico.gudhabapp.db.DatabaseContract.userColumns.Companion.TABLE_NAME
import com.eurico.gudhabapp.db.DatabaseContract.userColumns.Companion.username
import java.sql.SQLException

class FavHelper(context: Context) {

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: FavHelper? = null
        fun getInstance(context: Context): FavHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavHelper(context)
        }

        private lateinit var database: SQLiteDatabase
    }

    @Throws(SQLException::class)
    fun open(){
        database = dataBaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$username ASC"
        )
    }

    fun queryById(usernames: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$username = ?",
            arrayOf(usernames),
            null,
            null,
            null,
            null
        )
    }

    fun selectById(id: String): Boolean{
        val db = dataBaseHelper.writableDatabase
        val query = "SELECT * FROM fav WHERE username = '$id'"
        val cursor = db.rawQuery(query, null)
        var usernames = ""
        if(cursor.moveToFirst()){
            do{
                usernames = cursor.getString(cursor.getColumnIndex("username"))
            }while(cursor.moveToNext())
        }
        cursor.close()
        if(usernames != ""){
            return true
        }
        return false
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(usernames: String): Int{
        return database.delete(DATABASE_TABLE, "$username = '$usernames'", null)
    }
}