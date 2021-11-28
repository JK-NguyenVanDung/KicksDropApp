package com.project.kicksdrop.ui.searchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.product.ProductInfo;
import com.project.kicksdrop.ui.productBrands.ProductBrands;

import java.util.ArrayList;

public class SearchViewProduct extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    ProductListAdapter productAdapter;
    ArrayList<Product> mProduct;
    ImageButton prevBtn, cartBtn, chatBtn;
    EditText searchView;
    RecyclerView recyclerView;
    String keySearch;
    private final LoadingScreen loading = new LoadingScreen(SearchViewProduct.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_product);

        matching();
        recyclerView.setHasFixedSize(true);
        loading.startLoadingScreen();
        keySearch = getIntent().getStringExtra("keySearch");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);


        getProduct();
    }

    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.search_ibtn_prev);
        cartBtn = (ImageButton) findViewById(R.id.search_ibtn_cart);
        chatBtn = (ImageButton) findViewById(R.id.search_ibtn_chat);
        searchView = (EditText) findViewById(R.id.search_et_searchView);
        recyclerView = (RecyclerView) findViewById(R.id.search_rv_products);
    }
    public void onProductClick(int position, View view, String id) {
        Intent intent = new Intent(getApplicationContext(), ProductInfo.class);
        intent.putExtra("id", id);
        startActivity(intent);
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

                    if (product.getProduct_name().toLowerCase().contains(keySearch.toLowerCase())){
                        product.setProduct_id(dtShot.getKey());
                        mProduct.add(product);
                    }
                }
                productAdapter = new ProductListAdapter(getApplicationContext(),mProduct, SearchViewProduct.this,loading);
                recyclerView.setAdapter(productAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}