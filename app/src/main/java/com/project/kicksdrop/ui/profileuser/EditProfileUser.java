package com.project.kicksdrop.ui.profileuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kicksdrop.R;

public class EditProfileUser extends AppCompatActivity {

    ImageView profileAvatar;
    Button editBtn;
    ImageButton prevIBtn;
    TextView userName,userSex,userEmail,userPhone;
    private FirebaseUser account;
    private DatabaseReference reference;
    private String accountID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        matching();

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("account");
        accountID = account.getUid();


    }
        private void matching() {
        profileAvatar= (ImageView) findViewById(R.id.editProfile_iv_avatar);
        editBtn = (Button) findViewById(R.id.editProfile_btn_edit);
        prevIBtn =(ImageButton) findViewById(R.id.editProfile_ibtn_prev);
        userName = (TextView) findViewById(R.id.editProfile_et_name);
        userSex = (TextView) findViewById(R.id.editProfile_et_gender);
        userEmail = (TextView) findViewById(R.id.editProfile_et_address);
        userPhone = (TextView) findViewById(R.id.editProfile_et_phone);
    }
}