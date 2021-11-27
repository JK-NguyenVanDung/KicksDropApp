package com.project.kicksdrop.ui.profile;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.databinding.FragmentProfileBinding;
import com.project.kicksdrop.ui.auth.LoginActivity;
import com.project.kicksdrop.ui.profileuser.EditProfileUser;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout, profile;
    private FirebaseUser account;

    private DatabaseReference reference;
    private String accountID;
    private StorageReference storageReference;

    private ImageView avatarProfile;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        account = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("account");
        storageReference = FirebaseStorage.getInstance().getReference();
        accountID = account.getUid();

        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        setAccount(account.getUid());

        avatarProfile = binding.profileIvUser;

        loadImage(avatarProfile, "Avatar_defaul.png");

        logoutUI();
        profileUI();
        return root;
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

    private void setAccount(String userID) {
        if (account != null){
            if(account == null){
                return;
            }
        }
        account = FirebaseAuth.getInstance().getCurrentUser();
        assert account != null;
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
                Intent Ilogout = new Intent(getContext(), LoginActivity.class);
                startActivity(Ilogout);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}