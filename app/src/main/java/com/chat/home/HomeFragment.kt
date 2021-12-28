package com.chat.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chat.R
import com.chat.databinding.FragmentHomeBinding
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange

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

    fun handleObserver() {
        val options = PusherOptions()
        options.setCluster("ap4")

        val pusher = Pusher("cc31df9923d00ef5b9c9", options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i(
                    "Pusher", "State changed from ${change.previousState} to ${change.currentState}"
                )
            }

            override fun onError(
                message: String,
                code: String,
                e: Exception
            ) {
                Log.i(
                    "Pusher",
                    "There was a problem connecting! code ($code), " + "message ($message), exception($e)"
                )
            }
        }, ConnectionState.ALL)

        val channel = pusher.subscribe("chatify")
        channel.bind("messaging") { event ->
            Log.i("Pusher", "Received event with data: $event")
        }
    }
}