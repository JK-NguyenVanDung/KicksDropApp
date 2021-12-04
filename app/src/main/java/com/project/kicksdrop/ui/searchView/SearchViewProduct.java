package com.project.kicksdrop.ui.searchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.product.ProductDetail;

import java.util.ArrayList;

public class SearchViewProduct extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    ProductListAdapter productAdapter;
    ArrayList<Product> mProduct;
    ImageButton prevBtn, cartBtn, chatBtn;
    AutoCompleteTextView searchView;
    RecyclerView recyclerView;
    String keySearch;
    TextView title, tvNumberCart, noAnyThing;
    private final LoadingScreen loading = new LoadingScreen(SearchViewProduct.this);
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_product);

        matching();
        recyclerView.setHasFixedSize(true);
        loading.startLoadingScreen();
        keySearch = getIntent().getStringExtra("keySearch");

        if (keySearch.equals("")){
            title.setText("SALE");
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

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
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
        //get firebase
        //connect
        DatabaseReference myRef = database.getReference("product");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;
                    product.setProduct_id(dtShot.getKey());
                    adapter.add(product.getProduct_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setAdapter(adapter);

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchViewProduct.class);
                intent.putExtra("keySearch", searchView.getText().toString());
                startActivity(intent);
                searchView.setText("");

            }
        });

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                @SuppressLint("ClickableViewAccessibility") final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchView.getRight() - searchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50) && !searchView.getText().toString().matches("")) {
                        keySearch = searchView.getText().toString();
                        getProduct();

                        return true;

                    }
                }
                return false;
            }
        });
        getProduct();

    }


    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.search_ibtn_prev);
        title =(TextView) findViewById(R.id.search_title);
        cartBtn = (ImageButton) findViewById(R.id.search_ibtn_cart);
        chatBtn = (ImageButton) findViewById(R.id.search_ibtn_chat);
        searchView = findViewById(R.id.search_et_searchView);
        recyclerView = (RecyclerView) findViewById(R.id.search_rv_products);
        tvNumberCart= findViewById(R.id.tv_numberCart_Brands);
        noAnyThing = findViewById(R.id.Search_noAnyThing);

    }
    public void onProductClick(int position, View view, String id) {
        Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
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
                if(mProduct.size() == 0){
                    noAnyThing.setVisibility(View.VISIBLE);
                }else {
                    noAnyThing.setVisibility(View.GONE);
                }

                if(mProduct.size() <1){
                    loading.dismissDialog();
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