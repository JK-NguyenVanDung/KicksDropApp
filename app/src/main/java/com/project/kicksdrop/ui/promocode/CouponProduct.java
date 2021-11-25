package com.project.kicksdrop.ui.promocode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CouponAdapter;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.ui.cart.CartListView;

import java.util.ArrayList;

public class CouponProduct extends AppCompatActivity implements CouponAdapter.OnCouponListener{

    Button accept;
    ImageButton back;
    TextView totalPayment;
    FirebaseUser fUser;
    private CouponAdapter couponAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Coupon> mCoupon;
    private Coupon coupon;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        Intent intent = getIntent();
        price = intent.getDoubleExtra("price",0);
        matching();

        //
        //back
        back.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //recycler view
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getCouponList(fUser.getUid());
    }
    private void matching() {
        recyclerView = findViewById(R.id.body_productCoupon);
        back = findViewById(R.id.coupon_ibtn_prev);
        accept = findViewById(R.id.coupon_btn_accept);
        totalPayment = findViewById(R.id.Cart_tv_totalPayment);

    }
    private void getCouponList(String user_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+user_id+"/coupon");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> couponInList = new ArrayList<String>();
                for (DataSnapshot item : snapshot.getChildren()){
                    String abc = item.getKey();
                    couponInList.add(item.getKey());
                }
                ArrayList<String> scouponInList = couponInList;
                getCoupon(couponInList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCoupon(ArrayList<String> couponInList){
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


                    for (int i = 0; i < couponInList.size(); i++) {

                        if (couponInList.get(i).equals(dtShot.getKey())){
                            boolean check = couponInList.get(i).equals(dtShot.getKey());
                            Log.d("test",String.valueOf(check));
                            mCoupon.add(coupon);
                        }
                    }
                }
                ArrayList<Coupon> abc = mCoupon;
                Log.d("Test",String.valueOf(abc));
                couponAdapter = new CouponAdapter(getApplicationContext(), mCoupon, price, accept, CouponProduct.this);
                recyclerView.setAdapter(couponAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onCouponClick(int position, View view, String id) {
        Intent intent = new Intent(getApplicationContext(), CartListView.class);
        intent.putExtra("coupon_id", id);
        startActivity(intent);
    }
}