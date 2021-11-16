package com.project.kicksdrop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Chat;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private static List<Chat> mChat;
    private OnMessageListener mOnMessageListener;
    //Firebase
    static FirebaseUser fUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT =1;

    public MessageAdapter(Context context, List<Chat> mChat, OnMessageListener onMessageListener){
        this.context = context;
        this.mChat = mChat;
        this.mOnMessageListener = onMessageListener;
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);
        holder.show_Message.setText(chat.getMessage());
        if(chat.getEdited()) {
            holder.edited.setText("edited");
            holder.edited.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat ==null? 0: mChat.size();
    }

    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_sent, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_rcv, parent, false);
        }
        return new ViewHolder(view,mOnMessageListener);

    }
    private static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private static void setEdited(TextView edited) {
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public EditText show_Message;
        public TextView edited;
        public Button save,cancel;
        public KeyListener listener;
        OnMessageListener onMessageListener;
        public ViewHolder(@NonNull View itemView, OnMessageListener onMessageListener) {
            super(itemView);

            show_Message = itemView.findViewById(R.id.message_text_view);
            listener = show_Message.getKeyListener();
            save = itemView.findViewById(R.id.btn_save_edit);
            cancel = itemView.findViewById(R.id.btn_cancel_edit);
            edited = itemView.findViewById(R.id.chat_tv_edited);
            disableEditText(show_Message);


            this.onMessageListener = onMessageListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            fUser = FirebaseAuth.getInstance().getCurrentUser();

            if(mChat.get(position).getSender().equals(fUser.getUid())){
                onMessageListener.onMessageClick(getAdapterPosition(),v,mChat.get(position),listener,show_Message,save,cancel);
            }

            return false;
        }
    }
    public interface OnMessageListener{
        void onMessageClick(int position, View view, Chat chat, KeyListener listener, EditText editText,Button save, Button cancel);
    }

    @Override
    public int getItemViewType(int position){

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fUser.getUid())){
            return(MSG_TYPE_RIGHT);
        }else{
             return(MSG_TYPE_LEFT);
        }

    }
}
