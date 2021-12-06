package com.project.kicksdrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.List;

public class RatingPopUp {
    Activity activity;
    Fragment fragment;
    AlertDialog dialog;
    public RatingPopUp(Activity myActivity){
        activity = myActivity;
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    public void handle(){

    }
}
