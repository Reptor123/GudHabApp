package com.eurico.gudhabapp.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eurico.gudhabapp.view.fragment.followerFragment
import com.eurico.gudhabapp.view.fragment.followingFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var nama: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = followerFragment.setting(nama.toString())
            1 -> fragment = followingFragment.setting(nama.toString())
        }
        return fragment as Fragment
    }
}