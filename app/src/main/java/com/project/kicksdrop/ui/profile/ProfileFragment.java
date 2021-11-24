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

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button logout;

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
        return root;
    }

    private void logoutUI() {
        logout = binding.btnProfileUserSignOut;
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