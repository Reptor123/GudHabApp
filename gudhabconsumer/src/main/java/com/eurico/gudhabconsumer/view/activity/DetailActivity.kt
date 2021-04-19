package com.eurico.gudhabconsumer.view.activity

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eurico.gudhabconsumer.Model.User
import com.eurico.gudhabconsumer.R
import com.eurico.gudhabconsumer.ViewModel.ViewModelUser
import com.eurico.gudhabconsumer.databinding.ActivityDetailBinding
import com.eurico.gudhabconsumer.view.adapter.SectionPagerAdapter
import com.eurico.gudhabconsumer.view.adapter.UserAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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

        adapter = UserAdapter()

        MVU = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelUser::class.java)

        var listUser = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setTabLayout()

        MVU.detailUser(listUser.Username.toString())
        setData()
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
