package com.eurico.gudhabapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.eurico.gudhabapp.Model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ViewModelUser: ViewModel(){
    val listUser = MutableLiveData<ArrayList<User>>()
    val state = MutableLiveData<Boolean>()
    val detail = MutableLiveData<User>()


    companion object{
        const val URL = "https://api.github.com"
        const val GITHUB_TOKEN = "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"


    }

    fun getUser(): LiveData<ArrayList<User>> = listUser
    fun getState(): LiveData<Boolean> = state
    fun getUserDetail(): LiveData<User> = detail


    fun listUsers(){
        val listDataUser: ArrayList<User> = arrayListOf()
        AndroidNetworking.get("$URL/users")
            .addHeaders("Authorization", GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object: JSONArrayRequestListener{
                override fun onResponse(response: JSONArray) {
                    try{
                        for (i in 0 until response.length() - 1){
                            with(response.getJSONObject(i)){
                                listDataUser.add(
                                    User(
                                        Username = getString("login"),
                                        Avatar = getString("avatar_url")
                                    )
                                )
                            }
                        }
                        listUser.postValue(listDataUser)
                        state.postValue(true)
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }

    fun detailUser(name: String){
        AndroidNetworking.get("$URL/users/${name}")
            .addHeaders("Authorization", GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    try {
                        with(response) {
                            detail.postValue(
                                User(
                                    Username = getString("login"),
                                    Avatar = getString("avatar_url"),
                                    Comp = getString("company"),
                                    Location = getString("location"),
                                    Repo = getInt("public_repos"),
                                    Follower = getInt("followers"),
                                    Following = getInt("following")
                                )
                            )
                        }
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }

    fun findUser(name: String) {
        val user = arrayListOf<User>()
        AndroidNetworking.get("$URL/search/users?q=${name}")
            .addHeaders("Authorization", GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    try{
                        if(response.getInt("total_count") > 0){
                            for (i in 0 until response.getJSONArray("items").length() - 1){
                                with(response.getJSONArray("items").getJSONObject(i)){
                                    user.add(
                                        User(
                                            Username = getString("login"),
                                            Avatar = getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            listUser.postValue(user)
                            state.postValue(true)
                        }
                        else{
                            state.postValue(false)
                        }
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }

    fun followerData(name: String?){
        val user = arrayListOf<User>()
        AndroidNetworking.get("$URL/users/${name}/followers")
            .addHeaders("Authentication", GITHUB_TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object: JSONArrayRequestListener{
                override fun onResponse(response: JSONArray) {
                    try{
                        if(response.length() > 0){
                            for (i in 0 until response.length() - 1){
                                with(response.getJSONObject(i)){
                                    user.add(
                                        User(
                                            Username = getString("login"),
                                            Avatar = getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            listUser.postValue(user)
                            state.postValue(true)
                        }
                        else{
                            state.postValue(false)
                        }
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }

    fun followingData(name: String?){
        val user = arrayListOf<User>()
        AndroidNetworking.get("$URL/users/${name}/following")
            .addHeaders("Authentication", GITHUB_TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object: JSONArrayRequestListener{
                override fun onResponse(response: JSONArray) {
                    try{
                        if(response.length() > 0){
                            for (i in 0 until response.length() - 1){
                                with(response.getJSONObject(i)){
                                    user.add(
                                        User(
                                            Username = getString("login"),
                                            Avatar = getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            listUser.postValue(user)
                            state.postValue(true)
                        }
                        else{
                            state.postValue(false)
                        }
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    anError.printStackTrace()
                }
            })
    }
}