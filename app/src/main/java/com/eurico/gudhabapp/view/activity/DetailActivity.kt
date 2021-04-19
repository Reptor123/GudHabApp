package com.eurico.gudhabapp.view.activity

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eurico.gudhabapp.Model.User
import com.eurico.gudhabapp.R
import com.eurico.gudhabapp.ViewModel.ViewModelUser
import com.eurico.gudhabapp.databinding.ActivityDetailBinding
import com.eurico.gudhabapp.db.DatabaseContract
import com.eurico.gudhabapp.db.FavHelper
import com.eurico.gudhabapp.helper.MappingHelper
import com.eurico.gudhabapp.view.adapter.SectionPagerAdapter
import com.eurico.gudhabapp.view.adapter.UserAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var MVU: ViewModelUser
    private lateinit var adapter: UserAdapter


    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val favHelper = FavHelper.getInstance(applicationContext)

        adapter = UserAdapter()

        MVU = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelUser::class.java)

        var listUser = intent.getParcelableExtra<User>(EXTRA_USER) as User
        loadData(listUser.Username.toString())
        checkdata(listUser.Username.toString())

        setTabLayout()
        favButton()

        MVU.detailUser(listUser.Username.toString())
        setData()
    }

    private fun checkdata(id: String){
        val favHelper = FavHelper.getInstance(applicationContext)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        if(favHelper.selectById(id)){
            fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        else
            fab.setImageResource(R.drawable.ic_baseline_notfav)
    }

    private fun favButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val favHelper = FavHelper.getInstance(applicationContext)
        var usernamecheck = ""
        var avatardata = ""
        favHelper.open()
        fab.setOnClickListener{
            val values = ContentValues()
            MVU.getUserDetail().observe(this, {data ->
                values.put(DatabaseContract.userColumns.name, data.Name)
                values.put(DatabaseContract.userColumns.username, data.Username)
                values.put(DatabaseContract.userColumns.location, data.Location)
                values.put(DatabaseContract.userColumns.comp, data.Comp)
                values.put(DatabaseContract.userColumns.repo, data.Repo)
                values.put(DatabaseContract.userColumns.follower, data.Follower)
                values.put(DatabaseContract.userColumns.following, data.Following)
                values.put(DatabaseContract.userColumns.avatar, data.Avatar)
                usernamecheck = data.Username.toString()
                avatardata = data.Avatar.toString()
            })
            if(favHelper.selectById(usernamecheck)){
                favHelper.deleteById(usernamecheck)
                fab.setImageResource(R.drawable.ic_baseline_notfav)
                Toast.makeText(this@DetailActivity, "Berhasil menghapus favorite", Toast.LENGTH_SHORT).show()
            }
            else {
                favHelper.insert(values)
                val listDataUser: ArrayList<User> = arrayListOf()
                listDataUser.add(
                    User(
                        Username = usernamecheck,
                        Avatar = avatardata
                    )
                )
                adapter.listuser = listDataUser
                fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this@DetailActivity, "Berhasil menambah favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadData(id: String) {
        GlobalScope.launch(Dispatchers.Main){
            val favHelper = FavHelper.getInstance(applicationContext)
            favHelper.open()
            val deferredFav = async(Dispatchers.IO){
                val cursor = favHelper.queryAll()
                MappingHelper.mapCursorToArraylist(cursor)
            }
            val fav = deferredFav.await()
            if(fav.size > 0){
                adapter.listuser = fav
            }
        }
    }




    private fun setTabLayout() {
        val name: User = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.nama = name.Username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }


    private fun setData() {
        MVU.getUserDetail().observe(this, { data ->
            if (data != null) {
                Glide.with(this@DetailActivity)
                    .load(data.Avatar)
                    .apply(RequestOptions().override(200, 200))
                    .fitCenter()
                    .into(binding.foto)
                binding.name.text = data.Username
                binding.follower.text = data.Follower.toString()
                binding.following.text = data.Following.toString()
                binding.repo.text = data.Repo.toString()
                binding.kompeni.text = data.Comp.toString()
                binding.lokasi.text = data.Location.toString()
            }
        })
    }


}
