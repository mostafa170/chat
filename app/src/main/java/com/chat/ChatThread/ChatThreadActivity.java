package com.chat.ChatThread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.chat.R;
import com.chat.main.model.searchPeople.PeopleItem;

public class ChatThreadActivity extends AppCompatActivity {
    PeopleItem peopleItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread);

        if (getIntent().hasExtra("peopleItem")){
            peopleItem= (PeopleItem) getIntent().getSerializableExtra("peopleItem");
            Log.e("peopleItem",peopleItem.getName());
        }



    }
}