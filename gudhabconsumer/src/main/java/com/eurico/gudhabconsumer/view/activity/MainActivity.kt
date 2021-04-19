package com.eurico.gudhabconsumer.view.activity

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eurico.gudhabconsumer.ViewModel.ViewModelUser
import com.eurico.gudhabconsumer.view.adapter.UserAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.eurico.gudhabconsumer.db.DatabaseContract
import com.eurico.gudhabconsumer.helper.MappingHelper
import com.eurico.gudhabconsumer.R
import com.eurico.gudhabconsumer.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var MVU: ViewModelUser

    companion object{
        const val AUTHORITY = "com.eurico.gudhabapp.provider.FavProvider"
        const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(DatabaseContract.userColumns.TABLE_NAME)
            .build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MVU = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModelUser::class.java)

        adapter = UserAdapter()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        DividerItemDecoration(this, LinearLayoutManager(this).orientation).apply {
            binding.rvUser.addItemDecoration(this)
        }
        loadData()

        loading(true)
        MVU.listUsers()
    }



    fun loadData() {
        GlobalScope.launch(Dispatchers.Main){
            binding.progressBar.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArraylist(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val fav = deferredFav.await()
            if(fav.size > 0){
                adapter.listuser = fav
                showRecycler()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun loading(status: Boolean){
        if(status)
            binding.progressBar.visibility = View.VISIBLE
        else{
            lifecycleScope.launch(Dispatchers.Default){
                delay(2000L)
                withContext(Dispatchers.Main){
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


    fun showRecycler(){

        val handlerThread = HandlerThread("FavObs")
        handlerThread.start()

        val hanler = Handler(handlerThread.looper)

        val myObserver= object: ContentObserver(hanler){
            override fun onChange(selfChange: Boolean) {
                loadData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loading(false)
        binding.rvUser.adapter = adapter
    }

}