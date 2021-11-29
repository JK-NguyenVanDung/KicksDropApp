package com.project.kicksdrop.ui.productBrands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.project.kicksdrop.ui.product.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBrands extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    private ArrayList<Product> mProduct;
    ImageButton prevIBtn, cartIBtn, chatIBtn;
    EditText searchProduct;
    ProductListAdapter productAdapter;
    RecyclerView recyclerView;
    String brand;
    FirebaseUser fUser;
    private TextView tvnumberCart;
    private int numberCart;
    private final LoadingScreen loading = new LoadingScreen(ProductBrands.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brands);
        matching();


        recyclerView.setHasFixedSize(true);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        brand = getIntent().getStringExtra("brand");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);


        getProduct();


        getCart(fUser.getUid());
        tvnumberCart = (TextView) findViewById(R.id.tv_numberCart_Brands);
        tvnumberCart.setText(String.valueOf(numberCart));

        prevIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCart(fUser.getUid());
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
    }

    private void matching() {
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
    private void getCart(String user_Id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+user_Id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String,String>> productsInCart = new ArrayList<HashMap<String,String>>();
                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                if(hashMap != null) {
                    HashMap<String, Object> listProduct = (HashMap<String, Object>) hashMap.get("product");
                    for (Map.Entry<String, Object> entry : listProduct.entrySet()) {
                        String key = entry.getKey();
                        HashMap<String, String> item = (HashMap<String, String>) listProduct.get(key);
                        item.put("cartProductID", key);
                        productsInCart.add(item);
                    }
                    productsInCart.size();
                    //String coupon = hashMap.get("coupon_id").toString();
                    //Cart cart = new Cart(user_Id,,productsInCart);

                }else{
                    loading.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}