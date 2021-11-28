package com.project.kicksdrop.ui.productBrands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.home.HomeFragment;
import com.project.kicksdrop.ui.product.ProductInfo;

import java.util.ArrayList;

public class ProductBrands extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    private ArrayList<Product> mProduct;
    ImageButton prevIBtn, cartIBtn, chatIBtn;
    EditText searchProduct;
    ProductListAdapter productAdapter;
    RecyclerView recyclerView;
    String brand;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brands);
        matching();
        recyclerView.setHasFixedSize(true);

        brand = getIntent().getStringExtra("brand");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        title.setText(brand);
        getProduct();


    }

    private void matching() {
        title = (TextView) findViewById(R.id.productBrands_tv_brand);
        prevIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_prev);
        cartIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_cart);
        chatIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_chat);
        searchProduct = (EditText) findViewById(R.id.productBrands_iBtn_search);
        recyclerView = (RecyclerView) findViewById(R.id.brand_rv_products);
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
                productAdapter = new ProductListAdapter(getApplicationContext(),mProduct, ProductBrands.this);
                recyclerView.setAdapter(productAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}