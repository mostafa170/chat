package com.chat.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.chat.R
import com.chat.databinding.FragmentHomeBinding
import com.chat.utils.UserPreferenceHelper
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import androidx.lifecycle.Observer
import com.chat.ChatThread.ChatThreadActivity


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var viewModel: HomeViewModel? = null
    private var adapter: PeopleListAdapter? = null
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
        init()
        viewModel!!.getListPeaple(UserPreferenceHelper.getUser().id)
        handleObserver()

    }

    fun init() {
        adapter = PeopleListAdapter(null)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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
        viewModel!!.getErrorMessage().observe(getViewLifecycleOwner(), { integer: Int ->
            if (integer == 1) {
                Toast.makeText(context, "error in response data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "error in Network", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel!!.getPeapleResponseMutableLiveData().observe(getViewLifecycleOwner(), Observer {
            if (it.data == null) {
                //errorMessage if data coming is null;
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            } else {
                //show data in recyclerView
                binding.progressBar.setVisibility(View.GONE)
                adapter = PeopleListAdapter(it.data)
                binding.recHomePeople.setAdapter(adapter)
                adapter!!.setOnItemClickListener(PeopleListAdapter.OnItemClickListener { pos, dataItem ->
                    val intent = Intent(requireContext(), ChatThreadActivity::class.java)
                    intent.putExtra("people_id", dataItem.id)
                    startActivity(intent)
                })
            }
        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }
    private fun refresh(){
        synchronized(this) {
            viewModel!!.getListPeaple(UserPreferenceHelper.getUser().id)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.setVisibility(View.VISIBLE)
        }
    }
}