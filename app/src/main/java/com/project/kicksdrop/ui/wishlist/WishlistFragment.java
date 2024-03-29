package com.project.kicksdrop.ui.wishlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.adapter.WishlistAdapter;
import com.project.kicksdrop.databinding.FragmentWishlistBinding;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.cart.CartListView;
import com.project.kicksdrop.ui.searchView.SearchViewProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Product> mWishlist ;
    private Product product;

    private WishlistViewModel wishlistViewModel;
    private FragmentWishlistBinding binding;
    private WishlistAdapter wishlistAdapter;
    private EditText search;
    private TextView totalProducts;
    private FirebaseUser fUser;
    private TextView tvNumberCart, wishlistEmpty;
    private int numberCart;
    private final LoadingScreen loading = new LoadingScreen(WishlistFragment.this);

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wishlistViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        String idUser = fUser.getUid().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+idUser);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loading.startLoadingScreenFragment();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        wishlistEmpty = binding.wishlistEmptyText;

        recyclerView = binding.wishlistRvProducts;

        //recycler view
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        getWishlist(idUser);

        totalProducts = binding.wishlistTvItems;




        ImageButton cart,chat;

        cart = binding.wishlistBtnCart;

        chat = binding.wishlistBtnChat;
        tvNumberCart = binding.wishlistTvCartNumb;

        search = binding.wishlistEtSearch;
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50) && !search.getText().toString().matches("")) {
                        Intent intent = new Intent(getContext(), SearchViewProduct.class);
                        intent.putExtra("keySearch", search.getText().toString());
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        DatabaseReference ref = database.getReference("cart/"+fUser.getUid() + "/product");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getKey() != null) {


                    Long numberCart = snapshot.getChildrenCount();
                    if(tvNumberCart != null){
                        tvNumberCart.setText(String.valueOf(numberCart));
                    }

                }else{
                    loading.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cart.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartListView.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }


    private void getWishlist(String user_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+user_id);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Product> wishlist = new  ArrayList<Product>();
//                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
//                HashMap<String,String> coupon = (HashMap<String,String>) hashMap.get("coupon");
//                ArrayList<String> listWishlist = (ArrayList<String>) hashMap.get("wishlist");
                List<HashMap<String,String>> productsInWishlist = new ArrayList<HashMap<String,String>>();
                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                if(hashMap != null) {
                    for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                        String key = entry.getKey();
                        HashMap<String, String> item = (HashMap<String, String>) hashMap.get(key);
                        item.put("cartProductID", key);
                        productsInWishlist.add(item);
                    }
                    //String coupon = hashMap.get("coupon_id").toString();
                    //Cart cart = new Cart(user_Id,,productsInCart);

                    getProduct(productsInWishlist);
                }else if(productsInWishlist.size() == 0){
                    wishlistEmpty.setVisibility(View.VISIBLE);
                }else {
                    wishlistEmpty.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProduct(List<HashMap<String,String>> wishlist){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mWishlist = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWishlist.clear();
                    for(HashMap<String,String> item : wishlist){
                        for(DataSnapshot dtShot: snapshot.getChildren()){
                            if(item.get("product_id") != null) {

                                if (item.get("product_id").equals(dtShot.getKey())) {
                                    product = dtShot.getValue(Product.class);
                                    assert product != null;
                                    product.setProduct_id(dtShot.getKey());
                                    mWishlist.add(product);
                                }
                            }else{
                                loading.dismissDialog();
                            }
                        }




                }


                wishlistAdapter = new WishlistAdapter(getContext(),mWishlist,wishlist,totalProducts,loading);
                recyclerView.setAdapter(wishlistAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}