package com.eurico.gudhabapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eurico.gudhabapp.ViewModel.ViewModelUser
import com.eurico.gudhabapp.databinding.FragmentFollowingBinding
import com.eurico.gudhabapp.view.adapter.UserAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class followingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: UserAdapter
    private lateinit var VMU: ViewModelUser

    companion object{
        private const val NAME = "name"

        @JvmStatic
        fun setting(name: String)= followingFragment().apply{
            arguments = Bundle().apply{
                putString(NAME, name)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        adapter = UserAdapter()

        VMU = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModelUser::class.java)
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        DividerItemDecoration(context, LinearLayoutManager(context).orientation).apply {
            binding.rvFollowing.addItemDecoration(this)
        }
        showRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString(NAME)
        loading(true)
        VMU.followingData(name)
        setData()
    }

    private fun showRecycler(){
        loading(false)
        binding.rvFollowing.adapter = adapter
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

    private fun setData(){
        VMU.getState().observe(viewLifecycleOwner,{state ->
            if(state){
                VMU.getUser().observe(viewLifecycleOwner, { user->
                    if(user != null){
                        loading(false)
                        adapter.setter(user)
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