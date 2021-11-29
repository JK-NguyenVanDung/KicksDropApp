package com.project.kicksdrop;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

public class MessagePopUp {



    public void show(Context context, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context)

                .setIcon(R.drawable.ic__81607_complete_icon)

                .setTitle(" Message")

                .setMessage(message)

                .show();
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                alertDialog.dismiss();
            }
        }, 1000  );
    }
}
