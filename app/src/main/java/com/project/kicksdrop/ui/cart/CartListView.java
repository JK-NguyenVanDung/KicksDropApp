package com.project.kicksdrop.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CartAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.promocode.CouponProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartListView extends AppCompatActivity {

    //product cart
    TextView  totalProducts, totalPaymentHead, totalPayment;
    Button couponPage, productCartOrder;
    ImageButton back;
    FirebaseUser fUser;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    private ArrayList<Product> mProducts;
    private String coupon_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list_view);
        matching();
        Intent intent = getIntent();

        coupon_id = intent.getStringExtra("coupon_id");
        Log.d("test","test" +coupon_id);
        //back
        back.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        couponPage.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                double totalPrice = Double.parseDouble(totalPayment.getText().toString().substring(1));
                Intent intent = new Intent(getApplicationContext(), CouponProduct.class);
                intent.putExtra("price", totalPrice);
                startActivityForResult(intent, 1);

            }
        });

        //order page
        productCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalPrice = Double.parseDouble(totalPayment.getText().toString().substring(1));
                Intent intent = new Intent(getApplicationContext(), CartProductOrder.class);
                intent.putExtra("price", totalPrice);
                intent.putExtra("coupon",coupon_id);
                startActivityForResult(intent, 1);
            }
        });
        //recycler view
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getCart(fUser.getUid());

        if (coupon_id != null) {
            Discount(coupon_id);
        }
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
                    //String coupon = hashMap.get("coupon_id").toString();
                    //Cart cart = new Cart(user_Id,,productsInCart);

                    getProduct(productsInCart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getProduct(List<HashMap<String,String>> cartProducts){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProducts = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for(HashMap<String, String> item : cartProducts){
                    for(DataSnapshot dtShot: snapshot.getChildren()){
                    Product product = dtShot.getValue(Product.class);
                        assert product != null;
                        product.getProduct_colors().remove(0);
                        for(String color: product.getProduct_colors()){
                            String cartProductId = dtShot.getKey() + color.substring(1);
                            if(cartProductId.equals(item.get("cartProductID"))){
                                product.setProduct_id(dtShot.getKey());
                                product.getProduct_images().remove(0);
                                mProducts.add(product);
                            }
                        }

                    }
                }
                cartAdapter = new CartAdapter(getApplicationContext(),mProducts,cartProducts,totalPayment,totalProducts,totalPaymentHead,coupon_id);
                recyclerView.setAdapter(cartAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void matching() {
        recyclerView= findViewById(R.id.Cart_rv_products);
        couponPage = (Button) findViewById(R.id.Cart_btn_coupon);
        productCartOrder = (Button) findViewById(R.id.productCart_btn_order);
        totalPayment = (TextView) findViewById(R.id.Cart_tv_totalPayment);
        totalProducts = findViewById(R.id.Cart_tv_total_products);
        totalPaymentHead = findViewById(R.id.Cart_tv_total_head);
        back = findViewById(R.id.Cart_btn_back);
    }
    private void Discount(String coupon_id){
        if (coupon_id != null){
            String prices = totalPayment.getText().toString().trim();
            Log.d("test", prices);
        }
    }
}