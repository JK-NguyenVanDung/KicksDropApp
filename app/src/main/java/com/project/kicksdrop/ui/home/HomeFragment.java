package com.project.kicksdrop.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.databinding.FragmentHomeBinding;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.cart.CartListView;
import com.project.kicksdrop.ui.product.ProductInfo;
import com.project.kicksdrop.ui.productBrands.ProductBrands;
import com.project.kicksdrop.ui.searchView.SearchViewProduct;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ProductListAdapter.OnProductListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ProductListAdapter productAdapter;
    private ArrayList<Product> mProduct;
    ArrayList<Product> sProduct;
    RecyclerView recyclerView;


//    ImageButton productContentIbtn, newDropsIBtn, nikesIbtn, adidasIBtn;
//    Button productTitleBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.homeRvProducts;

        //recycler view
        recyclerView.setHasFixedSize(true);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        getProduct();
        final ImageButton nikesIbtn = binding.homeIbtnNikes;
        nikesIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductBrands.class);
                intent.putExtra("brand","Nike");
                startActivity(intent);
            }
        });

        final ImageButton adidasIbtn = binding.homeIbtnAdidas;
        adidasIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductBrands.class);
                intent.putExtra("brand","Adidas");
                startActivity(intent);
            }
        });
        final ImageButton button = binding.homeBtnChat;
        button.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        final ImageButton cart = binding.homeBtnCart;
        cart.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartListView.class);
                startActivity(intent);
            }
        });

        final EditText search = binding.homeEtSearch;
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= 630 && !search.getText().toString().matches("")) {

                            Intent intent = new Intent(getContext(), SearchViewProduct.class);
                            intent.putExtra("keySearch",search.getText().toString());
                            startActivity(intent);

                            return true;

                    }
                }
                return false;
            }
        });

//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable edit) {
//                if (edit.length() != 0) {
//                    String keySearch = search.getText().toString();
//                    searchProduct(keySearch);
//                    Log.v("keySearch",keySearch);
//                }
//            }
//        });
//




        return root;

    }
    //    public void matching() {
//        productContentIbtn = (ImageButton) findViewById(R.id.home_ibtn_productContent);
//        newDropsIBtn = (ImageButton) findViewById(R.id.home_ibtn_newDrops);
//        nikesIbtn = (ImageButton) findViewById(R.id.home_ibtn_nikes);
//        adidasIBtn = (ImageButton) findViewById(R.id.home_ibtn_adidas);
//        productTitleBtn = (Button) findViewById(R.id.home_btn_productTitle);
//
//    }
    @Override
    public void onProductClick(int position, View view, String id) {
        Intent intent = new Intent(getContext(), ProductInfo.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void searchProduct(String keySearch){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        sProduct =new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sProduct.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;

                    if (product.getProduct_name().toLowerCase().contains(keySearch.toLowerCase())){
                        product.setProduct_id(dtShot.getKey());
                        sProduct.add(product);
                    }
                }
//                productAdapter = new ProductListAdapter(getContext(),mProduct, SearchViewProduct.this);
//                recyclerView.setAdapter(productAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProduct =new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProduct.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;
                    product.setProduct_id(dtShot.getKey());
                    mProduct.add(product);
                }
                productAdapter = new ProductListAdapter(getContext(),mProduct,HomeFragment.this );
                recyclerView.setAdapter(productAdapter);
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