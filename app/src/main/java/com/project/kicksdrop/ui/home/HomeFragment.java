package com.project.kicksdrop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
private FragmentHomeBinding binding;
    ImageButton chatBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final ImageButton button = binding.homeBtnChat;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                button.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        return root;

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}