package com.eurico.gudhabconsumer.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class userColumns: BaseColumns {
        companion object{
            const val TABLE_NAME = "fav"
            const val name = "name"
            const val username = "username"
            const val location = "location"
            const val comp = "comp"
            const val repo = "repo"
            const val follower = "follower"
            const val following = "following"
            const val avatar = "avatar"
        }
    }
}