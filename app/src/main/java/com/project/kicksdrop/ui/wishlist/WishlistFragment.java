package com.project.kicksdrop.ui.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.kicksdrop.databinding.FragmentWishlistBinding;

public class WishlistFragment extends Fragment {

    TextView TotalItems, ProductName,
            ProductCost, ProductType;
    Spinner listDropDownSize;
    Button addBtn;
    ImageButton colorIBtn, removeIbtn;

    private WishlistViewModel wishlistViewModel;
    private FragmentWishlistBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wishlistViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textWishlist;
        wishlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


//    public void matching() {
//        TotalItems = (TextView) findViewById(R.id.wishlist_tv_totalItems);
//        ProductName = (TextView) findViewById(R.id.wishlist_tv_productName);
//        ProductCost = (TextView) findViewById(R.id.wishlist_tv_productCost);
//        ProductType = (TextView) findViewById(R.id.wishlist_tv_productType);
//        listDropDownSize = (Spinner) findViewById(R.id.wishlist_spinner_dropDownSize);
//        addBtn = (Button) findViewById(R.id.wishList_btn_add);
//        colorIBtn = (ImageButton) findViewById(R.id.wishlist_ibtn_yellow);
//        removeIbtn = (ImageButton) findViewById(R.id.wishlist_ibtn_remove);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}