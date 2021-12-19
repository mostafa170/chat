package com.chat.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
    }
 fun handleObserver(){
     // Create a client
     val apolloClient = ApolloClient.Builder()
         .serverUrl("https://solidsolutionsegypt.org/graphql")
         .build()
     // Execute your query. This will suspend until the response is received.
//     val response = apolloClient.query(users(first = "10",page=1)).execute()

//     println("Hero.name=${response.data?.users?.data?.get(0).name}")
 }

}