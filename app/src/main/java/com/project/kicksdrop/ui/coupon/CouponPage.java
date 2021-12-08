package com.project.kicksdrop.ui.coupon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.CouponAdapter;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.ui.cart.CartListView;

import java.util.ArrayList;

public class CouponPage extends AppCompatActivity implements CouponAdapter.OnCouponListener{

    Button accept;
    ImageButton back;
    TextView totalPayment;
    FirebaseUser fUser;
    TextView noAnyThing;
    Context context;
    private CouponAdapter couponAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Coupon> mCoupon;
    private com.project.kicksdrop.model.Coupon coupon;
    private double price;
    private final LoadingScreen loading = new LoadingScreen(CouponPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        loading.startLoadingScreen();

        Intent intent = getIntent();
        price = intent.getDoubleExtra("price",0);
        Log.d("price", String.valueOf(price));
        matching();
        context =this;
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
        accept = findViewById(R.id.coupon_btn_apply);
        totalPayment = findViewById(R.id.Cart_tv_totalPayment);
        noAnyThing = findViewById(R.id.Coupon_noAnyThing);
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

                if(couponInList.size() == 0){
                    noAnyThing.setVisibility(View.VISIBLE);
                }else {
                    noAnyThing.setVisibility(View.GONE);
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
                if(snapshot.getKey() != null){
                    for(DataSnapshot dtShot: snapshot.getChildren()){
                        coupon = dtShot.getValue(com.project.kicksdrop.model.Coupon.class);
                        assert coupon != null;
                        coupon.setCoupon_id(dtShot.getKey());
                        for (int i = 0; i < couponInList.size(); i++) {
                            if (couponInList.get(i).equals(dtShot.getKey())){
                                boolean check = couponInList.get(i).equals(dtShot.getKey());
                                coupon.setCoupon_checked(false);
                                mCoupon.add(coupon);
                            }
                        }
                    }
                    loading.dismissDialog();
                    couponAdapter = new CouponAdapter(context, mCoupon, price, accept, CouponPage.this);
                    recyclerView.setAdapter(couponAdapter);
                }else{
                    loading.dismissDialog();

                }
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
        setResult(Activity.RESULT_OK,
                new Intent().putExtra("coupon_id", id));
        finish();

    }
}