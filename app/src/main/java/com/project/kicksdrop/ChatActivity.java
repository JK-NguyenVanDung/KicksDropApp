package com.project.kicksdrop;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.adapter.MessageAdapter;
import com.project.kicksdrop.model.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements MessageAdapter.OnMessageListener{

    EditText input;
    ImageButton send,back;
    FirebaseUser fUser;
    FirebaseAuth auth;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        matching();
        auth = FirebaseAuth.getInstance();

        //recycler view
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);



        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String adminID = "ew0Zuldh3eMj13EEX4BK3XJoJ1m2";
        Log.d("user",fUser.getUid());
        readMessage(fUser.getUid(),adminID);

        send.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String msg = input.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getUid(),adminID,msg);
                }else{
                    Toast.makeText(ChatActivity.this,"Please enter a message to send",Toast.LENGTH_SHORT).show();
                }
                input.setText("");
            }
        });
        back.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chat").push().setValue(hashMap);
    }

    private void readMessage(String myId, String userId){
        mChat =new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){
                    Chat chat = dtShot.getValue(Chat.class);
                    assert chat != null;
                    chat.setId(dtShot.getKey());
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this,mChat,ChatActivity.this );
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void matching(){
        input= findViewById(R.id.chat_et_input);
        send = findViewById(R.id.chat_ib_send);
        back = findViewById(R.id.chat_ib_back);

        recyclerView = findViewById(R.id.chat_rv_chatArea);
    }
    private void enableEditText(EditText editText,KeyListener listener,boolean condition) {
        editText.setFocusable(condition);
        editText.setEnabled(condition);
        editText.setCursorVisible(condition);
        editText.setKeyListener(listener);
        editText.setBackgroundColor(condition? Color.TRANSPARENT: Color.LTGRAY);
        editText.setFocusableInTouchMode(condition);
    }
    @Override
    public void onMessageClick(int position, View view, Chat chat, KeyListener listener, EditText editText, Button save, Button cancel) {
        Log.d(TAG, "onMessageClick: clicked");

        PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view,Gravity.RIGHT);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked
                Toast.makeText(ChatActivity.this, "Edited successfully "+ menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Chat");

                switch (menuItem.getItemId()) {
                    case R.id.edit:

                        //reference.child(chat.getId()).child("message").setValue(str_Name);
                        enableEditText(editText,listener,true);
                        save.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String editedText = editText.getText().toString();
                                reference.child(chat.getId()).child("message").setValue(editedText);
                                reference.child(chat.getId()).child("edited").setValue(true);
                                save.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                                enableEditText(editText,listener,false);

                                Toast.makeText(ChatActivity.this, "Edited successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableEditText(editText,listener,false);

                                save.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                            }
                        });
                        return true;
                    case R.id.delete:
                        reference.child(chat.getId()).removeValue();
                        ((ViewGroup)editText.getParent()).removeView(editText);
                }
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
        //onButtonShowPopupWindowClick(this.findViewById(android.R.id.content).getRootView());
    }
}