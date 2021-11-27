package com.project.kicksdrop.ui.profileuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Account;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditProfileUser extends AppCompatActivity {

    ImageView profileAvatar;
    private Button editBtn;
    ImageButton prevIBtn;
    Button deleteProfile;
    EditText userName,userSex,userEmail,userPhone;
    private FirebaseUser account;
    private ArrayList<Account> mAccount;
    private DatabaseReference reference;
    private String accountID;
    private StorageReference storageReference;
    private Account Kaccount;
    private String userID, name, imagesName, email, gender, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        matching();

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("account");
        storageReference = FirebaseStorage.getInstance().getReference();
        accountID = account.getUid();

        disableEditText(userEmail);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileUser.this, "Changed successful ! ",Toast.LENGTH_SHORT).show();
                isChangedProfile();
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileUser.this, "Delete successful !", Toast.LENGTH_SHORT).show();
                isDeleteProfile();
            }
        });

        setAccount(account.getUid());
    }



    private void loadImage(ImageView image, String imageName){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imageName);
        try {

            File file = File.createTempFile("tmp",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAccount(String user_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/" + user_id);
        mAccount = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAccount.clear();

                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();

                email = hashMap.get("email").toString();
                name = hashMap.get("name").toString();
                mobile = hashMap.get("mobile").toString();
                gender = hashMap.get("gender").toString();

                userEmail.setText(email);
                userName.setText(name);
                userPhone.setText(mobile);
                userSex.setText(gender);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void isChangedProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());


        email = userEmail.getText().toString().trim();
        name = userName.getText().toString().trim();
        mobile = userPhone.getText().toString().trim();
        gender = userSex.getText().toString().trim();

        myRef.child("email").setValue(email);
        myRef.child("name").setValue(name);
        myRef.child("gender").setValue(gender);
        myRef.child("mobile").setValue(mobile);

    }

    private static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void isDeleteProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());


        email = userEmail.getText().toString().trim();
        name = userName.getText().toString().trim();
        mobile = userPhone.getText().toString().trim();
        gender = userSex.getText().toString().trim();

        myRef.child("email").setValue(email);
        myRef.child("name").setValue("");
        myRef.child("gender").setValue("");
        myRef.child("mobile").setValue("");

    }
//    private void createUser(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("account");
//
//
//        myRef.child(userID).child("coupon").child("CP1").setValue("CP1");
//        myRef.child(userID).child("coupon").child("CP2").setValue("CP2");
//
//        myRef.child(userID).child("email").setValue(email);
//        myRef.child(userID).child("gender").setValue(gender);
//        myRef.child(userID).child("mobile").setValue(mobile);
//        myRef.child(userID).child("name").setValue(name);
//    }


    private void setAccount(String userID) {
        if (account != null){
            if(account == null){
                return;
            }
        }
        account = FirebaseAuth.getInstance().getCurrentUser();
        assert account != null;
        getAccount(account.getUid());
        loadImage(profileAvatar, "Avatar_defaul.png");
    }


    private void matching() {

        profileAvatar= (ImageView) findViewById(R.id.editProfile_iv_avatar);
        editBtn = (Button) findViewById(R.id.editProfile_btn_edit);
        prevIBtn =(ImageButton) findViewById(R.id.editProfile_ibtn_prev);
        userName = (EditText) findViewById(R.id.editProfile_et_name);
        userSex = (EditText) findViewById(R.id.editProfile_et_gender);
        userEmail = (EditText) findViewById(R.id.editProfile_et_address);
        userPhone = (EditText) findViewById(R.id.editProfile_et_phone);
        deleteProfile = (Button) findViewById(R.id.editProfile_btn_delete);
    }

}