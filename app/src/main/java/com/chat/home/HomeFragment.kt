package com.chat.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.ApolloClient
import com.chat.R
import com.chat.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
            .inflate(inflater!!, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init()
        handleObserver()
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://solidsolutionsegypt.org/graphql")
            .build()
        lifecycleScope.launchWhenResumed {
            val response = apolloClient.query(users()).execute()

            Log.d("LaunchList", "Success ${response.data}")
        }
    }
 fun handleObserver() {
     // Create a client
     // Execute your query. This will suspend until the response is received.

 }
}