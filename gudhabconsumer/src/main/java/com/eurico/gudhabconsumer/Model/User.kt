package com.eurico.gudhabconsumer.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        val Name: String? = "",
        val Username: String? = "",
        val Location: String? = "",
        val Comp: String? = "",
        val Repo: Int? = 0,
        val Follower: Int? = 0,
        val Following: Int? = 0,
        val Avatar: String? = ""
) : Parcelable