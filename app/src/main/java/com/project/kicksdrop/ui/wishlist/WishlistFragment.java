package com.project.kicksdrop.ui.wishlist;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.adapter.WishlistAdapter;
import com.project.kicksdrop.adapter.WishlistItemAdapter;
import com.project.kicksdrop.databinding.FragmentWishlistBinding;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Product> mWishlist ;
    private Product product;

    private WishlistViewModel wishlistViewModel;
    private FragmentWishlistBinding binding;
    private WishlistAdapter wishlistAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wishlistViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        recyclerView = binding.wishlistRvProducts;

        //recycler view
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        String idUser = fUser.getUid().toString();
        getUser(idUser);

        final TextView textView = binding.textWishlist;
        wishlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    private void getUser(String user_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+user_id);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Product> wishlist = new  ArrayList<Product>();
                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                HashMap<String,String> coupon = (HashMap<String,String>) hashMap.get("coupon");
                ArrayList<String> listWishlist = (ArrayList<String>) hashMap.get("wishlist");

                getProduct(listWishlist);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProduct(ArrayList<String> wishlist){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mWishlist = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWishlist.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){

                    product = dtShot.getValue(Product.class);
                    assert product != null;
                    product.setProduct_id(dtShot.getKey());


                    for (int i = 0; i < wishlist.size(); i++) {
                        if (wishlist.get(i).equals(dtShot.getKey())){
                            mWishlist.add(product);
                        }
                    }


                }
                wishlistAdapter = new WishlistAdapter(getContext(),mWishlist);
                recyclerView.setAdapter(wishlistAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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