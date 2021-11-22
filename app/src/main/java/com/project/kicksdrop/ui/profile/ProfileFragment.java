package com.project.kicksdrop.ui.profile;

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

import com.project.kicksdrop.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    ImageView userAvatar;
    TextView userName;
    Button wishList,profile,notification,address,signOut;

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
        return root;
    }

    //    private void matching() {
//        userAvatar= (ImageView) findViewById(R.id.profile_iv_user);
//        userName = (Button) findViewById(R.id.profile_tv_userName);
//        wishList =(ImageButton) findViewById(R.id.profile_btn_wishlist);
//        profile = (TextView) findViewById(R.id.profile_btn_profile);
//        notification = (TextView) findViewById(R.id.profile_btn_notification);
//        address = (TextView) findViewById(R.id.profile_btn_address);
//        signOut = (TextView) findViewById(R.id.profile_btn_signOut);
//    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}