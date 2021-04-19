package com.eurico.gudhabapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eurico.gudhabapp.db.DatabaseContract.userColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbfav"

        private const val DATABASE_VERSION = 2

        private const val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.userColumns.username} PRIMARY KEY," +
                "${DatabaseContract.userColumns.name} TEXT NOT NULL," +
                "${DatabaseContract.userColumns.comp} TEXT NOT NULL," +
                "${DatabaseContract.userColumns.location} TEXT NOT NULL," +
                "${DatabaseContract.userColumns.repo} INTEGER NOT NULL," +
                "${DatabaseContract.userColumns.follower} INTEGER NOT NULL," +
                "${DatabaseContract.userColumns.following} INTEGER NOT NULL," +
                "${DatabaseContract.userColumns.avatar} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}