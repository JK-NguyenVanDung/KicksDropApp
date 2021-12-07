package com.project.kicksdrop.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.simpleratingbar.OnRatingChangedListener;
import com.borjabravo.simpleratingbar.SimpleRatingBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MessagePopUp;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.ColorCircleAdapter;
import com.project.kicksdrop.adapter.ImageAdapter;
import com.project.kicksdrop.model.Image;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.auth.LoginActivity;
import com.project.kicksdrop.ui.cart.CartListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView name,currentSize,price,amount,discountPercent,ratingCount;
    ImageButton increaseAmount,decreaseAmount,goBack,share;
    Button addToCart;
    Spinner sizeSpinner;
    Product product;
    ImageView productImage;
    ArrayList<HashMap<String,String>> productInCart;
    ViewPager viewPager;
    ImageAdapter imageAdapter;
    TextView indexNumb;
    FirebaseUser fUser;
    ImageButton cart;
    Context context;
    TextView numReviewer;
    TextView discountPrice;
    FrameLayout layoutDiscount;
    private Double numPrice = 0.0 ;
    private Double discountedPrice;
    private TextView tvNumberCart;
    private final LoadingScreen loading = new LoadingScreen(ProductDetail.this);
    int currentAmount = 1;
    ColorCircleAdapter circleAdapter;
    RecyclerView mCirclesRecyclerView;
    NavController navController;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navController.handleDeepLink(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        loading.startLoadingScreen();
        context = this;
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        matching();
        getProductInCart(fUser.getUid());




        increaseAmount.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(currentAmount < product.getProduct_quantity()){
                    currentAmount++;
                    amount.setText(Integer.toString(currentAmount));

                }else{
                    Toast.makeText(getApplicationContext(),"Reach maximum available product",Toast.LENGTH_SHORT).show();
                }
            }
        });
        decreaseAmount.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(currentAmount >1){
                    currentAmount--;
                    amount.setText(Integer.toString(currentAmount));
                }else{
                    Toast.makeText(getApplicationContext(),"Minimum amount is 1",Toast.LENGTH_SHORT).show();
                }
            }
        });
        goBack.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fUser != null){
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        cart.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent;
                if(fUser != null){
                    intent = new Intent(getApplicationContext(), CartListView.class);
                }else{
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                startActivity(intent);
            }
        });
        Intent intent = getIntent();

        String id = intent.getStringExtra("id");


        tvNumberCart = (TextView) findViewById(R.id.product_tv_numberCart);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mCirclesRecyclerView = (RecyclerView) findViewById(R.id.productInfo_rv_circles);
        mCirclesRecyclerView.setLayoutManager(layoutManager);
        share.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                String message = ("https://kicksdrop.com/product/"+ id);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share From KicksDrop");
                startActivity(shareIntent);
            }
        });
        if(fUser == null){

        }else{
            getProduct(id);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("cart/"+fUser.getUid() + "/product");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getKey() != null) {


                        Long numberCart = snapshot.getChildrenCount();
                        if(tvNumberCart != null){
                            tvNumberCart.setText(String.valueOf(numberCart));
                        }
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

    private void getProduct(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                product = snapshot.getValue(Product.class);
                assert product != null;
                product.setProduct_id(snapshot.getKey());
                //Log.d("yeah",product.getProduct_sizes().toString());
                java.util.Currency usd = java.util.Currency.getInstance("USD");
                java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                format.setCurrency(usd);

                double normalPrice = product.getProduct_price();
                discountedPrice = product.getDiscount_price();
                if(discountedPrice != 0){
                    discountPrice.setVisibility(View.VISIBLE);
                    layoutDiscount.setVisibility(View.VISIBLE);
                    discountPrice.setPaintFlags(discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    double temp = (discountedPrice/normalPrice) * 100;
                    int  discountPercentage = (int) (100.00- (Math.round(temp)));
                    discountPercent.setText("-" + discountPercentage + "%");
                    String sPrice =format.format(discountedPrice);
                    price.setText(sPrice);
                    String discount =format.format(normalPrice);
                    discountPrice.setText(discount);
                }else{
                    String sPrice =format.format(product.getProduct_price());
                    price.setText(sPrice);
                }


                if (product.getProduct_quantity()<=0){
                    currentAmount = 0;
                    addToCart.setText("Out of stock");
                    addToCart.setEnabled(false);
                    increaseAmount.setEnabled(false);
                    decreaseAmount.setEnabled(false);

                }
                ratingCount.setText(product.getRating_amount()+" reviews");
                ratingStar.setRating((float) product.getProduct_rating());
                ratingStar.setEnabled(false);
                String value = product.getProduct_sizes().get(1);
                name.setText(product.getProduct_name());
                currentSize.setText(value);
                //currentSizeSelector.setText(value);

                amount.setText(Integer.toString(currentAmount));

                product.getProduct_images().remove(0);

                int size =product.getProduct_images().size();
                int count = 0;
                List<Image> images = new ArrayList<>(size);
                while (count < size){
                    String tempImg = product.getProduct_images().get(count).get("image");
                    String tempColor = product.getProduct_images().get(count).get("color");
                    Image temp = new Image(tempImg,tempColor);
                    images.add(temp);
                    count++;
                }
                indexNumb = findViewById(R.id.tv_productInfo_productPage);

                imageAdapter = new ImageAdapter(getApplicationContext(),images,loading);
                viewPager.setAdapter(imageAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    public void onPageScrollStateChanged(int state) {


                    }
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        String displayText = (position+1) + "/"+ size;
                        indexNumb.setText(displayText);

                    }

                    public void onPageSelected(int position) {
                        List<ImageButton> buttons=  ColorCircleAdapter.getColorButtons();
                        if(buttons != null){
                            buttons.get(position).performClick();
                        }
                    }
                });

                ArrayList<String> colors = product.getProduct_colors();
                colors.remove(0);

                circleAdapter = new ColorCircleAdapter(getApplicationContext(),colors,images,viewPager);
                mCirclesRecyclerView.setAdapter(circleAdapter);

                product.getProduct_sizes().remove(0);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, product.getProduct_sizes());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sizeSpinner.setAdapter(adapter);
                sizeSpinner.setOnItemSelectedListener(ProductDetail.this);
                addToCart.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(fUser != null){
                            String pickedSize = sizeSpinner.getSelectedItem().toString();
                            int pickedAmount = Integer.parseInt(amount.getText().toString());
                            String pickedColor = ColorCircleAdapter.getPickedColor();
                            fUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert fUser != null;
                            addProductCart(fUser.getUid(),product.getProduct_id(),pickedAmount,pickedColor,pickedSize);
                            MessagePopUp messagePopUp = new MessagePopUp();
                            messagePopUp.show(context,"Add To Cart Successfully");
                        }else{
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductInCart(String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart").child(idUser).child("product");
        productInCart = new ArrayList<HashMap<String, String>>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productInCart.clear();

               for (DataSnapshot item : snapshot.getChildren()){
                   String key = item.getKey();
                   //ProductsInCart value = (ProductsInCart) item.getValue();
                   String value = item.getValue().toString();

                   value = value.substring(8).split(",")[0];

                   HashMap<String, String> map = new HashMap<>();
                   map.put(key,value);

                   productInCart.add(map);

               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addProductCart(String idUser,String idProduct,int amount, String color,String size){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");
        boolean flag = true;
        String idColor = color.substring(1);
        for (int i = 0; i < productInCart.size(); i++) {
            for ( String key : productInCart.get(i).keySet() ) {
                if ((idProduct+idColor).equals(key)){
                    amount = amount + Integer.parseInt(productInCart.get(i).get(key));
                    myRef.child(idUser).child("product").child(idProduct+idColor).child("amount").setValue(amount);
                    myRef.child(idUser).child("product").child(idProduct+idColor).child("size").setValue(size);
                    flag= false;
                    break;
                }
            }
        }

        if (flag){
            myRef.child(idUser).child("product").child(idProduct+idColor).child("productId").setValue(idProduct);
            myRef.child(idUser).child("product").child(idProduct+idColor).child("amount").setValue(amount);
            myRef.child(idUser).child("product").child(idProduct+idColor).child("color").setValue(color);
            myRef.child(idUser).child("product").child(idProduct+idColor).child("size").setValue(size);

        }

       }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSize.setText(sizeSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void matching(){
        ratingCount = findViewById(R.id.productInfo_tv_reviews);
        ratingStar = findViewById(R.id.productDetail_ratingStar);
        name = findViewById(R.id.tv_productInfo_productName);
        currentSize = findViewById(R.id.tv_productInfo_productSize);
        discountPercent = findViewById(R.id.productDetail_tv_percent);
        price = findViewById(R.id.tv_productInfo_product_price);
        amount = findViewById(R.id.tv_productInfo_amoutOfProducts);
        increaseAmount =  findViewById(R.id.ibtn_productInfo_increase);
        decreaseAmount =  findViewById(R.id.ibtn_productInfo_decrease);
        addToCart =  findViewById(R.id.btn_productInfo_addToCart);
        sizeSpinner = findViewById(R.id.spinner_productInfo_product_Size);
        goBack = findViewById(R.id.ibtn_productInfo_back);
        productImage = findViewById(R.id.productInfo_iv_image);
        viewPager = findViewById(R.id.productInfo_vp_image);
        cart = findViewById(R.id.productInfo_btn_cart);
        tvNumberCart = findViewById(R.id.product_tv_numberCart);
        share= findViewById(R.id.product_btn_share);
        numReviewer = findViewById(R.id.tv_productInfo_reviews);
        discountPrice = findViewById(R.id.tv_product_DiscountPrice);
        layoutDiscount = findViewById(R.id.FL_layoutDiscount);
    }

}