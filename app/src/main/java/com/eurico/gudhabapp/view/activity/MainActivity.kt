package com.eurico.gudhabapp.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eurico.gudhabapp.R
import com.eurico.gudhabapp.ViewModel.ViewModelUser
import com.eurico.gudhabapp.view.adapter.UserAdapter
import com.eurico.gudhabapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.eurico.gudhabapp.alarm.AlarmReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var MVU: ViewModelUser
    private lateinit var alarmReceiver: AlarmReceiver
    var searchUser = ""


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
        showRecycler()

        loading(true)
        MVU.listUsers()
        prepare()
        setAlarm()
    }

    private fun setAlarm() {
        alarmReceiver = AlarmReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(name: String): Boolean {
                loading(true)
                if(name.isNotEmpty()){
                    searchUser = name
                    MVU.findUser(name)

                }
                else{
                    MVU.listUsers()
                }
                prepare()
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        else if(item.itemId == R.id.favoritepage){
            val mIntentFav = Intent(this, FavActivity::class.java)
            startActivity(mIntentFav)
        }
        else if(item.itemId == R.id.alarm){
            Log.d("itemTitle", "Title: "+ item.title.toString()+ ", String: "+ resources.getString(R.string.set_alarm_on).toString() )
            if(item.title.toString() == resources.getString(R.string.set_alarm_on)){
                val repeatMessage = "Comeback here, we miss you <3"
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatMessage)
                item.setTitle(R.string.set_alarm_off)
            }
            else{
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
                item.setTitle(R.string.set_alarm_on)
            }
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


    private fun showRecycler(){
        loading(false)
        binding.rvUser.adapter = adapter
    }



    fun prepare(){
        MVU.getState().observe(this, {state->
            if(state){
                MVU.getUser().observe(this, {userItems->
                    if(userItems != null){
                        adapter.setter(userItems)
                        showRecycler()
                    }
                })
            }
            else{
                loading(false)
            }
        })
    }
}