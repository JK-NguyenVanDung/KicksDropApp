package com.project.kicksdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.kicksdrop.ui.chat.ChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChatFragment.newInstance())
                    .commitNow();
        }
    }
}