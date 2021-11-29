package com.project.kicksdrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

public class LoadingScreen {
    Activity activity;
    Fragment fragment;
    AlertDialog dialog;

    public LoadingScreen(Activity myActivity){
        activity = myActivity;
    }
    public LoadingScreen(Fragment myFragment){
        fragment = myFragment;
    }
    public void startLoadingScreenFragment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());

        LayoutInflater inflater = fragment.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

    }
    public void startLoadingScreen(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

    }
    public void dismissDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
