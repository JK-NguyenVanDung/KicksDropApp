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

    TextView wishListTotalItems, wishListProductName,
            wishListProductCost, wishListProductType;
    Spinner wishListDropDownSize;
    Button wishListMoreOption;
    ImageButton wishListYellow, wishListRemoveProduct;

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
//        wishListTotalItems = (TextView) findViewById(R.id.wishlist_tv_items);
//        wishListProductName = (TextView) findViewById(R.id.wishlist_tv_productName);
//        wishListProductCost = (TextView) findViewById(R.id.wishlist_tv_productCost);
//        wishListProductType = (TextView) findViewById(R.id.wishlist_tv_productType);
//        wishListDropDownSize = (Spinner) findViewById(R.id.wishlist_spinner_dropDownSize);
//        wishListMoreOption = (Button) findViewById(R.id.wishList_btn_moreOption);
//        wishListYellow = (ImageButton) findViewById(R.id.wishlist_ibtn_yellow);
//        wishListRemoveProduct = (ImageButton) findViewById(R.id.wishlist_ibtn_remove);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}