package com.eurico.gudhabapp.view.activity

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eurico.gudhabapp.R
import com.eurico.gudhabapp.ViewModel.ViewModelUser
import com.eurico.gudhabapp.databinding.ActivityFavBinding
import com.eurico.gudhabapp.db.DatabaseContract
import com.eurico.gudhabapp.helper.MappingHelper
import com.eurico.gudhabapp.view.adapter.UserAdapter
import kotlinx.coroutines.*

class FavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavBinding
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
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MVU = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModelUser::class.java)

        adapter = UserAdapter()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        DividerItemDecoration(this, LinearLayoutManager(this).orientation).apply {
            binding.rvUser.addItemDecoration(this)
        }

        val handlerThread = HandlerThread("FavObs")
        handlerThread.start()

        val hanler = Handler(handlerThread.looper)

        val myObserver= object: ContentObserver(hanler){
            override fun onChange(selfChange: Boolean) {
                loadData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

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
        loading(false)
        binding.rvUser.adapter = adapter
    }

}