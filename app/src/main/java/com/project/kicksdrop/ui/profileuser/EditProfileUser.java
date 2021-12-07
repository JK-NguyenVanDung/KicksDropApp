package com.project.kicksdrop.ui.profileuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.ValueEventRegistration;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CouponAdapter;
import com.project.kicksdrop.model.Account;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.profile.ProfileFragment;
import com.project.kicksdrop.ui.wishlist.WishlistFragment;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class EditProfileUser extends AppCompatActivity {

    ImageView profileAvatar;
    private Button editBtn;
    ImageButton prevIBtn;
    Button deleteProfile;
    EditText userName,userSex,userEmail,userPhone, userAddress;
    private FirebaseUser account;
    private ArrayList<Account> mAccount;
    private DatabaseReference reference;
    private String  name, imagesName, email, gender, mobile, address;
    private Uri imageUri;
    private StorageReference storageReference;
    private final LoadingScreen loading = new LoadingScreen(EditProfileUser.this);
    private ShimmerFrameLayout shimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        matching();
        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("account");
        storageReference = FirebaseStorage.getInstance().getReference();
        loading.startLoadingScreen();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileUser.this, "Changed successful ! ",Toast.LENGTH_SHORT).show();
                isChangedProfile();
            }
        });

        prevIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileUser.this, "Delete successful !", Toast.LENGTH_SHORT).show();
                isDeleteProfile();
            }
        });
        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        setAccount(account.getUid());
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data.getData()!=null){
            imageUri = data.getData();
            profileAvatar.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        StorageReference ref = storageReference.child("userProfile/" + account.getUid());
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();

                Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("account/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
                myRef.child("avatar").setValue(account.getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
    }

    private void loadImage(ImageView image, String imageName){
        StorageReference ref =  storageReference.child("userProfile/" + imageName);
        try {

            File file = File.createTempFile("tmp",".jpg");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    if(loading !=null){
                        loading.dismissDialog();
                    }

                    shimmer.stopShimmer();
                    shimmer.hideShimmer();
                    shimmer.setVisibility(View.GONE);                }
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
                if(hashMap.get("address") != null){
                    address = Objects.requireNonNull(hashMap.get("address")).toString();

                }
                if(hashMap.get("avatar") != null){
                    imagesName= Objects.requireNonNull(hashMap.get("avatar")).toString();
                    loadImage(profileAvatar, imagesName);
                }else{
                    loading.dismissDialog();
                }

                userEmail.setText(email);
                userName.setText(name);
                userPhone.setText(mobile);
                userSex.setText(gender);
                userAddress.setText(address);

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
        address = userAddress.getText().toString().trim();

        myRef.child("email").setValue(email);
        myRef.child("name").setValue(name);
        myRef.child("gender").setValue(gender);
        myRef.child("mobile").setValue(mobile);
        myRef.child("address").setValue(address);

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
        address = userAddress.getText().toString().trim();

        myRef.child("email").setValue(email);
        myRef.child("name").setValue("");
        myRef.child("gender").setValue("");
        myRef.child("mobile").setValue("");
        myRef.child("address").setValue("");

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

    }


    private void matching() {
        shimmer = findViewById(R.id.shimmer_edit_profile);
        profileAvatar= (ImageView) findViewById(R.id.editProfile_iv_avatar);
        editBtn = (Button) findViewById(R.id.editProfile_btn_edit);
        prevIBtn =(ImageButton) findViewById(R.id.editProfile_btn_prev);
        userName = (EditText) findViewById(R.id.editProfile_et_name);
        userSex = (EditText) findViewById(R.id.editProfile_et_gender);
        userEmail = (EditText) findViewById(R.id.editProfile_et_address);
        userPhone = (EditText) findViewById(R.id.editProfile_et_phone);
        deleteProfile = (Button) findViewById(R.id.editProfile_btn_delete);
        userAddress = (EditText) findViewById(R.id.editProfile_et_infoAddress);
    }

}