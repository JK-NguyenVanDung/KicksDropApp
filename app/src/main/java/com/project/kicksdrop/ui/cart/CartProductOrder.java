package com.project.kicksdrop.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.project.kicksdrop.model.ProductsInCart;
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
    TextView  tv_shipmentPartner, tv_couponPercent, tv_shipment, tv_totalPrice, tv_discount, tv_shipmentPrice, tv_totalPayment, tv_couponCode;
    RadioGroup rGroup;
    EditText et_address;
    OrderProductAdapter orderProductAdapter;
    RecyclerView recyclerView;
    Integer quanity;
    List<HashMap<String,String>> productsInCart;
    private Coupon coupon;
    private ArrayList<Product> mProducts;
    private List<Coupon> mCoupon;
    private String coupon_id;
    private int percent, maxprice;
    private double price, totalPaymentPrice;
    private Double total = 0.0;
    private String timeStamp_id;
    private String address;
    private static KeyListener listener;
    private static Drawable bgAddress;
    private double shipPrice;
    private double discount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_order);

        Intent intent = getIntent();
        price = intent.getDoubleExtra("price",0);
        coupon_id = intent.getStringExtra("coupon");
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

                if(!et_address.getText().toString().equals(" ") && !et_address.getText().toString().equals("") ){
                    createOrder();
                    Intent intent1 = new Intent(getApplicationContext(), OrderCompleted.class);
                    startActivity(intent1);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Address field is empty!",Toast.LENGTH_SHORT).show();
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

        //
        if (!coupon_id.equals("")){
            getCoupon(coupon_id);
        }else{
            tv_couponCode.setText(" ");
            tv_couponPercent.setText(" ");
            tv_discount.setText(" ");

        }

        shipPrice = 10.00;
        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(shipPrice);
        String sShipPrice =format.format(shipPrice);

        tv_shipment.setText(sPrice);
        tv_shipmentPrice.setText(sShipPrice);
        address= " ";

        listener =et_address.getKeyListener();
        bgAddress = et_address.getBackground();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/" + fUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                address = Objects.requireNonNull(hashMap.get("address")).toString();
                if(!address.equals(" ")){
                    et_address.setText(address);
                    disableEditText(et_address);
                }else{
                    enableEditText(et_address,listener,true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






    }

    private void enableEditText(EditText editText, KeyListener listener, boolean condition) {
        editText.setFocusable(condition);
        editText.setEnabled(condition);
        editText.setCursorVisible(condition);
        editText.setKeyListener(listener);
        editText.setBackground(bgAddress);
        editText.setFocusableInTouchMode(condition);
    }
    private static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);

        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.order_ibtn_prev);
        orderBtn = (Button) findViewById(R.id.order_btn_makeOrder);

        recyclerView = (RecyclerView) findViewById(R.id.order_rv_products);
        et_address = (EditText) findViewById(R.id.order_et_address);
        tv_couponCode = (TextView)findViewById( R.id.order_tv_coupon );
        tv_totalPayment = (TextView) findViewById( R.id.order_tv_totalPayment );
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
                                product.setProduct_color(item.get("color"));
                                product.setProduct_size(item.get("size"));
                                product.setAmount(String.valueOf(item.get("amount")));
                                mProducts.add(product);
                                HashMap<String,Object> hashMap = (HashMap<String, Object>) dtShot.getValue();

                                if (item!= null && hashMap != null){
                                    try {
                                        total +=  Double.valueOf( String.valueOf(item.get("amount"))) * (Double) hashMap.get( "product_price" );
                                    } catch (Exception e){

                                    }
                                    tv_totalPrice.setText(total.toString());
                                    java.util.Currency usd = java.util.Currency.getInstance("USD");
                                    java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                                    format.setCurrency(usd);
                                    String sTotalPayment = format.format( total + shipPrice - discount );
                                    tv_totalPayment.setText(sTotalPayment);
                                }


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


                discount = caculateDiscount(maxprice,percent,price);
                java.util.Currency usd = java.util.Currency.getInstance("USD");
                java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                format.setCurrency(usd);
                String sPrice =format.format(price);
                String sDiscount =format.format(discount);
                String sTotalPayment = format.format(price + shipPrice);
                tv_discount.setText(sDiscount);
                tv_couponPercent.setText(percent + "%");
                tv_couponCode.setText( coupon.getCoupon_code() );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProduct(String product_id, int quanity){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product/"+product_id);
       myRef.child("product_quantity").setValue(quanity);
    }

    private void createOrder(){
        String timeStamp = new SimpleDateFormat("HH:mm: dd/MM/yyyy").format(Calendar.getInstance().getTime());
        timeStamp_id = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order/"+fUser.getUid()+"/"+timeStamp_id);
        DatabaseReference saveAddress = database.getReference("account/"+fUser.getUid()+"/address");
        //
        saveAddress.setValue(et_address.getText().toString().trim());
        myRef.child("address").setValue(et_address.getText().toString().trim());
        myRef.child("coupon_id").setValue(coupon_id);
        myRef.child("order_create_date").setValue(timeStamp);
        myRef.child("order_discount").setValue(tv_discount.getText().toString().trim().substring(1));
        myRef.child("order_price").setValue(tv_totalPayment.getText().toString().trim().substring(1));
        myRef.child("shipment_partner").setValue(tv_shipmentPartner.getText().toString().trim());
        myRef.child("shipping_price").setValue(tv_shipmentPrice.getText().toString().trim().substring(1));
        myRef.child("status").setValue("Ordered");
        myRef.child("user_id").setValue(fUser.getUid());
        myRef.child( "order_id" ).setValue( timeStamp_id );
        myRef.child( "quantity_product" ).setValue( String.valueOf( mProducts.size() ));
        //
        addProductOrder(fUser.getUid());
        deleteFromCart();
        deleteFromCoupon(coupon_id);


    }
    @SuppressLint("SetTextI18n")
    private void deleteFromCart(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+ "/product/");
        myRef.removeValue();

    }@SuppressLint("SetTextI18n")
    private void deleteFromCoupon(String coupon_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+ "/coupon/");
        myRef.child(coupon_id).removeValue();

    }

    private void addProductOrder(String user_Id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+user_Id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsInCart = new ArrayList<HashMap<String,String>>();
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
                    DatabaseReference myRef = database.getReference("order/"+fUser.getUid()+"/"+timeStamp_id);
                    for (HashMap<String,String> item: productsInCart){

                        item.put("amount",String.valueOf(item.get("amount")));
                        item.put("productId",String.valueOf(item.get("productId")));
                        //updateProduct(String.valueOf(item.get("productId"));

                    }
                    myRef.child("order_details").setValue(productsInCart);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}