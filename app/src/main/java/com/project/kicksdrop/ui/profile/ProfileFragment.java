package com.project.kicksdrop.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.databinding.FragmentProfileBinding;
import com.project.kicksdrop.ui.auth.LoginActivity;
import com.project.kicksdrop.ui.customerOrder.CustomerOrder;
import com.project.kicksdrop.ui.auth.ResetPasswordActivity;
import com.project.kicksdrop.ui.customerOrder.CustomerOrder;
import com.project.kicksdrop.ui.home.HomeFragment;
import com.project.kicksdrop.ui.profileuser.EditProfileUser;
import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout, profile, changePass,  user_order;
    private ImageView avatar;
    private FirebaseUser account;
    private StorageReference storageReference;
    private LoadingScreen loading = new LoadingScreen(ProfileFragment.this);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        loading.startLoadingScreenFragment();
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        logoutUI();
        profileUI();
        resetPasswordUI();
        user_Order();

        avatar = binding.profileIvUser;
        account = FirebaseAuth.getInstance().getCurrentUser();

        assert account != null;
        getAccount(account.getUid(),avatar);

        return root;
    }

    private void getAccount(String user_id, ImageView avatar){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/" + user_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();

                if(hashMap.get("avatar") != null){
                    loadImage();

                }else{
                    loading.dismissDialog();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadImage(){
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference ref =  storageReference.child("userProfile/" + account.getUid());
        try {

            File file = File.createTempFile("tmp",".jpg");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    avatar.setImageBitmap(bitmap);
                    if(loading != null){
                        loading.dismissDialog();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resetPasswordUI() {
        changePass = binding.profileBtnChangePassword;
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goChangePass = new Intent(getContext(), ResetPasswordActivity.class);
                startActivity(goChangePass);
            }
        });
    }


    private void profileUI() {
        profile = binding.profileBtnProfile;
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goProfile = new Intent(getContext(), EditProfileUser.class);
                startActivity(goProfile);
            }
        });
    }

    private void logoutUI() {
        logout = binding.profileBtnSignOut;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = requireContext().getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("rememberMe","false");
                editor.apply();
                Intent iLogout = new Intent(getContext(), LoginActivity.class);
                iLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                FirebaseAuth.getInstance().signOut();
                startActivity(iLogout);

            }
        });
    }
    private void user_Order() {
        user_order = binding.profileBtnUserOrder;
        user_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IOrder = new Intent(getContext(), CustomerOrder.class);
                startActivity(IOrder);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}