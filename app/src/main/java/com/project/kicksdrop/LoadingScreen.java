package com.project.kicksdrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

public class LoadingScreen {
    Activity activity;
    AlertDialog dialog;

    public LoadingScreen(Activity myActivity){
        activity = myActivity;
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
        dialog.dismiss();
    }
}
