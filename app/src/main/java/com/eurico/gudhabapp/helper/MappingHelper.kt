package com.eurico.gudhabapp.helper

import android.database.Cursor
import com.eurico.gudhabapp.Model.User
import com.eurico.gudhabapp.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArraylist(favoriteCursor: Cursor?): ArrayList<User>{
        val favList = ArrayList<User>()

        favoriteCursor?.apply {
            while(moveToNext()){
                val name = getString(getColumnIndexOrThrow(DatabaseContract.userColumns.name))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.userColumns.username))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.userColumns.location))
                val comp = getString(getColumnIndexOrThrow(DatabaseContract.userColumns.comp))
                val repo = getInt(getColumnIndexOrThrow(DatabaseContract.userColumns.repo))
                val follower = getInt(getColumnIndexOrThrow(DatabaseContract.userColumns.follower))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.userColumns.following))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.userColumns.avatar))
                favList.add(User(name,username, location, comp, repo, follower, following,avatar))
            }
        }
        return favList
    }
}