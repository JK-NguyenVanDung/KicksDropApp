package com.project.kicksdrop.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CartAdapter;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.coupon.CouponPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartListView extends AppCompatActivity {

    //product cart
    Context context;
    TextView  totalProducts, totalPaymentHead, totalPayment, couponCode, noAnyOne;
    Button couponPage, productCartOrder;
    ImageButton back;
    FirebaseUser fUser;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    private ArrayList<Product> mProducts;
    private String coupon_id;
    private com.project.kicksdrop.model.Coupon coupon;
    private double totalAmount;
    private final LoadingScreen loading = new LoadingScreen(CartListView.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list_view);

        matching();
        context= this;
        loading.startLoadingScreen();

        coupon_id= "";

        back.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                finish();

            }
        });

        couponPage.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                double totalPrice = Double.parseDouble(totalPayment.getText().toString().substring(1));
                Intent intent = new Intent(getApplicationContext(), CouponPage.class);
                intent.putExtra("price", totalPrice);
                startActivityForResult(intent, 1);

            }
        });

        //order page
        productCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalPrice = Double.parseDouble(totalPayment.getText().toString().substring(1));
                if(totalPrice > 0.00){
                    Intent intent = new Intent(getApplicationContext(), CartProductOrder.class);
                    intent.putExtra("price", totalPrice);
                    intent.putExtra("coupon",coupon_id);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "No Products In Cart", Toast.LENGTH_SHORT).show();
                }
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

        if (!coupon_id.equals("")) {
            Discount(coupon_id);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            coupon_id = data.getStringExtra("coupon_id");
            double total = CartAdapter.getTotalAmount();
            getCoupon(coupon_id,total);
        }
    }

    private void getCoupon(String coupon_id, double total){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dtShot: snapshot.getChildren()){

                    com.project.kicksdrop.model.Coupon temp = dtShot.getValue(com.project.kicksdrop.model.Coupon.class);
                    assert temp != null;
                    temp.setCoupon_id(dtShot.getKey());
                    if(coupon_id.equals(dtShot.getKey())){
                        coupon = temp;
                        break;
                    }
                }
                if(coupon != null){
                    totalAmount = total;
                    double percent = Integer.parseInt(coupon.getCoupon_percent());
                    double max = coupon.getCoupon_max_price();
                    double min = coupon.getCoupon_min_price();
                    if(totalAmount < max && totalAmount > min ){
                        double discount = totalAmount * ((double) percent/100);
                        totalAmount -= discount;
                        java.util.Currency usd = java.util.Currency.getInstance("USD");
                        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                        format.setCurrency(usd);
                        String sPrice =format.format(totalAmount);
                        totalPayment.setText(sPrice);
                        totalPaymentHead.setText(sPrice);
                        couponCode.setText("Coupon: " + coupon.getCoupon_code());
                    }
                }
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
                    //String coupon = hashMap.get("coupon_id").toString();
                    //Cart cart = new Cart(user_Id,,productsInCart);

                    getProduct(productsInCart);
                }else{
                    loading.dismissDialog();
                }if (productsInCart.size() == 0){
                    noAnyOne.setVisibility(View.VISIBLE);
                }else if(productsInCart.size() != 0){
                    noAnyOne.setVisibility(View.GONE);
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

                cartAdapter = new CartAdapter(context,mProducts,cartProducts,totalPayment,totalProducts,totalPaymentHead,coupon_id,loading);
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
        couponCode=  findViewById(R.id.Cart_coupon_code);
        back = findViewById(R.id.Cart_btn_back);
        noAnyOne = findViewById(R.id.Cart_noAnyOne);
    }
    private void Discount(String coupon_id){
        if (coupon_id != null){
            String prices = totalPayment.getText().toString().trim();
            Log.d("test", prices);
        }
    }
}