package com.project.kicksdrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

public class DeletePopUp {
    Activity activity;
    Fragment fragment;
    AlertDialog dialog;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    Boolean flag = false;
    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void startPopUpScreenFragment(){
        new AlertDialog.Builder(fragment.getContext())
                .setTitle("Cảnh Báo")
                .setMessage("Bạn Có Muốn Xóa Không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }
    public void startPopUpScreen(){
        new AlertDialog.Builder(activity)
                .setTitle("Cảnh Báo")
                .setMessage("Bạn Có Muốn Xóa Không?")
               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag =true;
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    public void dismissDialog(){
        dialog.dismiss();
    }

    public void handle(){

    }
}
