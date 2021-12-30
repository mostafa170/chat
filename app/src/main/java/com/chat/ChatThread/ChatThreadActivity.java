package com.chat.ChatThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.chat.ChatThread.model.ChatListResponse;
import com.chat.R;
import com.chat.databinding.ActivityChatThreadBinding;
import com.chat.main.model.searchPeople.PeopleItem;

public class ChatThreadActivity extends AppCompatActivity {
    String peopleItem;
    String idUser;
    ActivityChatThreadBinding binding;
    private ChatListViewModel viewModel;
    ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_thread);
        viewModel = new ViewModelProvider(this).get(ChatListViewModel.class);
        init();
        if (getIntent().hasExtra("peopleItem")) {
            peopleItem = getIntent().getStringExtra("peopleItem");
            viewModel.getChatList(peopleItem);
            Log.e("peopleItem", peopleItem);
        } else if (getIntent().hasExtra("people_id")) {
            idUser = getIntent().getStringExtra("people_id");
            viewModel.getChatList(idUser);
            Log.e("people_id", idUser);
        }
        onClickListener();
    }

    private void init() {
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setLayoutAnimation(animation);
        adapter = new ChatListAdapter(null);
        binding.recyclerView.setAdapter(adapter);
    }

    private void onClickListener() {
        viewModel.getErrorMessage().observe(this, integer -> {
            if (integer == 1) {
                Toast.makeText(ChatThreadActivity.this, "error in response data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChatThreadActivity.this, "error in Network", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getPeapleResponseMutableLiveData().observe(this, new Observer<ChatListResponse>() {
            @Override
            public void onChanged(ChatListResponse chatListResponse) {
                binding.progressBar.setVisibility(View.GONE);
                adapter = new ChatListAdapter(chatListResponse.getData());
                binding.recyclerView.setAdapter(adapter);

            }
        });
    }
}