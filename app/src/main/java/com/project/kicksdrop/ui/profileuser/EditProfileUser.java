package com.project.kicksdrop.ui.profileuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.kicksdrop.R;

public class EditProfileUser extends AppCompatActivity {
    ImageView profileAvatar;
    Button editBtn;
    ImageButton prevIBtn;
    TextView userName,userSex,userEmail,userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);

    }

//    private void matching() {
//        profileAvatar= (ImageView) findViewById(R.id.editProfile_iv_avatar);
//        editBtn = (Button) findViewById(R.id.editProfile_btn_edit);
//        prevIBtn =(ImageButton) findViewById(R.id.editProfile_ibtn_prev);
//        userName = (TextView) findViewById(R.id.editProfile_tv_name);
//        userSex = (TextView) findViewById(R.id.editProfile_tv_sex);
//        userEmail = (TextView) findViewById(R.id.editProfile_tv_email);
//        userPhone = (TextView) findViewById(R.id.editProfile_tv_phone);
//    }


}