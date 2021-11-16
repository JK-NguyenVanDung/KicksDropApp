package com.project.kicksdrop.model;

import android.util.Log;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String id;
    private boolean edited;
   public Chat(String sender, String receiver, String message,boolean edited){
       this.sender = sender;
       this.receiver = receiver;
       this.message = message;
       this.edited = edited;
   }


    public Chat(){

    }

    public boolean getEdited(){
        return edited;
    }
    public void setEdited(boolean edited) {
        this.edited = edited;
    }
    public String getId(){
       return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
