package com.project.kicksdrop.ui.cart;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CartAdapter;
import com.project.kicksdrop.adapter.OrderProductAdapter;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.customerOrder.CustomerOrder;
import com.project.kicksdrop.ui.orderCompleted.OrderCompleted;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartProductOrder extends AppCompatActivity {

    FirebaseUser fUser;
    ImageButton prevBtn;
    Button orderBtn;
    TextView  totalProducts, totalPaymentHead, totalPayment;
    TextView tv_address, tv_shipmentPartner, tv_couponPercent, tv_shipment, tv_totalPrice, tv_discount, tv_shipmentPrice;
    RadioGroup rGroup;
    OrderProductAdapter orderProductAdapter;
    RecyclerView recyclerView;
    private Coupon coupon;
    private ArrayList<Product> mProducts;
    private List<Coupon> mCoupon;
    private String coupon_id;
    private int percent, maxprice;
    private double price;
    private int shipmentPrice;
    private String timeStamp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_order);

        Intent intent = getIntent();
        price = intent.getDoubleExtra("price",0);
        coupon_id = intent.getStringExtra("coupon");
        Log.d("Test", "abc" + price + coupon_id);
        //
        matching();
        //

        //back
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //
                createOrder();
                Intent intent1 = new Intent(getApplicationContext(), CustomerOrder.class);
                startActivity(intent1);

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

        //
        if (coupon_id !=null){
            getCoupon(coupon_id);
        }
        double shipPrice = 10.00;


        tv_shipment.setText(String.valueOf(shipPrice));
        tv_shipmentPrice.setText(String.valueOf(shipPrice));

        tv_address.setText("123 ABC");
    }

    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.order_ibtn_prev);
        orderBtn = (Button) findViewById(R.id.order_btn_makeOrder);

        recyclerView = (RecyclerView) findViewById(R.id.order_rv_products);
        tv_address = (TextView) findViewById(R.id.order_tv_address);
        tv_totalPrice = (TextView) findViewById(R.id.order_tv_total);
        tv_couponPercent = (TextView) findViewById(R.id.order_tv_couponPercent);
        tv_discount = (TextView) findViewById(R.id.order_tv_discount);
        tv_shipment = (TextView) findViewById(R.id.order_tv_shippingCost);
        tv_shipmentPrice = (TextView) findViewById(R.id.order_tv_shipmentPrice);
        tv_shipmentPartner = (TextView) findViewById(R.id.order_tv_methodShipment);
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
                orderProductAdapter = new OrderProductAdapter(getApplicationContext(),mProducts,cartProducts);
                Log.d("test",mProducts.toString());
                recyclerView.setAdapter(orderProductAdapter);


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
                    Log.d("test", productsInCart.toString());
                    getProduct(productsInCart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private double caculateDiscount(int maxprice, int percent, double price){
        double discount = (price * percent) / 100;
        if (discount > maxprice){
            discount = maxprice;
        }
        return discount;
    }
    private void getCoupon(String coupon_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");
        mCoupon = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCoupon.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){

                    coupon = dtShot.getValue(Coupon.class);
                    assert coupon != null;
                    coupon.setCoupon_id(dtShot.getKey());
                    if(coupon_id.equals(dtShot.getKey())){
                        mCoupon.add(coupon);
                        break;
                    }
                }
                maxprice = coupon.getCoupon_max_price();
                percent = Integer.parseInt(coupon.getCoupon_percent());

                double discount = caculateDiscount(maxprice,percent,price);

                tv_discount.setText(String.valueOf(discount));
                tv_totalPrice.setText(String.valueOf(price));
                tv_couponPercent.setText(String.valueOf(percent));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void createOrder(){
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        timeStamp_id = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order/"+fUser.getUid()+"/"+timeStamp_id);


        myRef.child("address").setValue(tv_address.getText().toString().trim());
        myRef.child("coupon_id").setValue(coupon_id);
        myRef.child("oder_create_date").setValue(timeStamp);
        myRef.child("order_discount").setValue(tv_discount.getText().toString().trim());
        myRef.child("order_price").setValue(tv_totalPrice.getText().toString().trim());
        myRef.child("shipment_partner").setValue(tv_shipmentPartner.getText().toString().trim());
        myRef.child("shipping_price").setValue(tv_shipmentPrice.getText().toString().trim());
        myRef.child("status").setValue("Ordered");
        myRef.child("user_id").setValue(fUser.getUid());

        addProductOrder(fUser.getUid());
    }
    private void addProductOrder(String user_Id){
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
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("order/"+fUser.getUid()+"/"+timeStamp_id+"/order_details");
                    for (HashMap<String,String> item: productsInCart){

                        item.put("amount",String.valueOf(item.get("amount")));
                        item.put("productId",String.valueOf(item.get("productId")));

                        myRef.child(Objects.requireNonNull(item.get("cartProductID"))).setValue(item);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}