package com.eurico.gudhabconsumer.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eurico.gudhabconsumer.ViewModel.ViewModelUser
import com.eurico.gudhabconsumer.databinding.FragmentFollowerBinding
import com.eurico.gudhabconsumer.view.adapter.UserAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class followerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var adapter: UserAdapter
    private lateinit var VMU: ViewModelUser

    companion object{
        private const val NAME = "name"

        @JvmStatic
        fun setting(name: String)= followerFragment().apply{
            arguments = Bundle().apply{
                putString(NAME, name)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        adapter = UserAdapter()

        VMU = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModelUser::class.java)
        binding.rvFollower.layoutManager = LinearLayoutManager(context)
        DividerItemDecoration(context, LinearLayoutManager(context).orientation).apply {
            binding.rvFollower.addItemDecoration(this)
        }
        showRecycler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString(NAME)
        loading(true)
        VMU.followerData(name)
        setData()
    }

    private fun showRecycler(){
        loading(false)
        binding.rvFollower.adapter = adapter
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