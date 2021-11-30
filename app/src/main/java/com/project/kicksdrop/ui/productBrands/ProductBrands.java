package com.project.kicksdrop.ui.productBrands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.cart.CartListView;
import com.project.kicksdrop.ui.home.HomeFragment;
import com.project.kicksdrop.ui.product.ProductInfo;
import com.project.kicksdrop.ui.searchView.SearchViewProduct;

import java.util.ArrayList;

public class ProductBrands extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    private ArrayList<Product> mProduct;
    ImageButton prevIBtn, cartIBtn, chatIBtn;
    TextView tvNumberCart, noAnyThing, title;
    EditText search;
    ProductListAdapter productAdapter;
    RecyclerView recyclerView;
    String brand;
    String keySearch;
    private final LoadingScreen loading = new LoadingScreen(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brands);
        matching();

        loading.startLoadingScreen();
        recyclerView.setHasFixedSize(true);

        brand = getIntent().getStringExtra("brand");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        title.setText(brand);
        getProduct();

        prevIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCart = new Intent(ProductBrands.this, CartListView.class);
                startActivity(goCart);
            }
        });

        chatIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCart = new Intent(ProductBrands.this, ChatActivity.class);
                startActivity(goCart);
            }
        });





        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50) && !search.getText().toString().matches("")) {

                        Intent intent = new Intent(getApplicationContext(), SearchViewProduct.class);
                        intent.putExtra("keySearch", search.getText().toString());
                        startActivity(intent);

                        return true;

                    }
                }
                return false;
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = database.getReference("cart/"+fUser.getUid() + "/product");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getKey() != null) {


                    Long numberCart = snapshot.getChildrenCount();

                    tvNumberCart.setText(String.valueOf(numberCart));
                }else{
                    loading.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void matching() {
        prevIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_prev);
        cartIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_cart);
        chatIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_chat);
        search = (EditText) findViewById(R.id.productBrands_iBtn_search);
        recyclerView = (RecyclerView) findViewById(R.id.brand_rv_products);
        tvNumberCart = findViewById(R.id.tv_numberCart_Brands);
        noAnyThing = findViewById(R.id.Brands_noAnyThing);
        title = (TextView) findViewById(R.id.productBrands_tv_brand);
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

                    if (product.getProduct_brand().equals(brand)){
                        product.setProduct_id(dtShot.getKey());
                        mProduct.add(product);
                    }
                }

                if(mProduct.size() == 0){
                    noAnyThing.setVisibility(View.VISIBLE);
                }else {
                    noAnyThing.setVisibility(View.GONE);
                }

                productAdapter = new ProductListAdapter(getApplicationContext(),mProduct, ProductBrands.this,loading);
                recyclerView.setAdapter(productAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchProduct(String key){
        keySearch = key;
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
                if(mProduct.size() == 0){
                    noAnyThing.setVisibility(View.VISIBLE);
                }else {
                    noAnyThing.setVisibility(View.GONE);
                }

                if(mProduct.size() <1){
                    loading.dismissDialog();
                }
                productAdapter = new ProductListAdapter(getApplicationContext(),mProduct, ProductBrands.this,loading);
                recyclerView.setAdapter(productAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}