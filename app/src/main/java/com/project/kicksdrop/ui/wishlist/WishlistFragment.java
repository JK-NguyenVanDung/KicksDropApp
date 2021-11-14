package com.project.kicksdrop.ui.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.kicksdrop.databinding.FragmentWishlistBinding;

public class WishlistFragment extends Fragment {
    private WishListViewModel wishListViewModel;
private FragmentWishlistBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        wishListViewModel =
                new ViewModelProvider(this).get(WishListViewModel.class);

    binding = FragmentWishlistBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textWishlist;
        wishListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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