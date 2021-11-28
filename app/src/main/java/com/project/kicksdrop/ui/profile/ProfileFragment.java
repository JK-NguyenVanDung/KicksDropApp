package com.project.kicksdrop.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.project.kicksdrop.R;
import com.project.kicksdrop.databinding.FragmentProfileBinding;
import com.project.kicksdrop.ui.auth.LoginActivity;
import com.project.kicksdrop.ui.auth.ResetPasswordActivity;
import com.project.kicksdrop.ui.customerOrder.CustomerOrder;
import com.project.kicksdrop.ui.profileuser.EditProfileUser;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout, profile, changePass, myOrder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        logoutUI();
        profileUI();
        resetPasswordUI();
        myOrder();
        return root;
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

    private void myOrder() {
        myOrder = binding.profileBtnMyOrder;
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMyOrder = new Intent(getContext(), CustomerOrder.class);
                startActivity(goMyOrder);
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