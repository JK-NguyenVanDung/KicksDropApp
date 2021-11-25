package com.project.kicksdrop.ui.profileuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.ValueEventRegistration;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CouponAdapter;
import com.project.kicksdrop.model.Account;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.promocode.CouponProduct;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;

public class EditProfileUser extends AppCompatActivity {

    ImageView profileAvatar;
    Button editBtn;
    ImageButton prevIBtn;
    TextView userName,userSex,userEmail,userPhone;
    private FirebaseUser account;
    private ArrayList<Account> mAccount;
    private DatabaseReference reference;
    private String accountID;
    private StorageReference storageReference;
    private Account Kaccount;
    String name, imagesName, email, gender, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        matching();

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("account");
        storageReference = FirebaseStorage.getInstance().getReference();
        accountID = account.getUid();


    }

    private void getAccount(String userID) {
        if (account != null){
            if(account == null){
                return;
            }
        }
        account = FirebaseAuth.getInstance().getCurrentUser();
        assert account != null;

        getAccount(account.getUid());

        name = account.getDisplayName();
        email = account.getEmail();
        gender = account.getDisplayName();
        mobile = account.getPhoneNumber();
        imagesName = account.getDisplayName();

        userName.setText(name);
        userEmail.setText(email);
        userSex.setText(gender);
        userPhone.setText(mobile);

//        Glide.with(this).load(profileAvatar).error("Avatar_defaul.png");
                loadImage(profileAvatar, "Avatar_defaul.png");
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
        DatabaseReference myRef = database.getReference("account");
        mAccount = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAccount.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){
                    Kaccount = dtShot.getValue(Account.class);
                    assert Kaccount != null;
                    Kaccount.setIdUser(dtShot.getKey());
                    if (user_id.equals(dtShot.getKey())){
                        mAccount.add(Kaccount);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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