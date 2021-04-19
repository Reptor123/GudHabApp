package com.eurico.gudhabapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.eurico.gudhabapp.provider.FavProvider"
    const val SCHEME = "content"

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

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}