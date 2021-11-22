package com.project.kicksdrop.ui.promocode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.kicksdrop.R;

public class CouponProduct extends AppCompatActivity {

    ImageButton prevBtn, removeCoupon;
    Button accept;
    CheckBox checkBox;
    TextView couponCode, couponContent, couponDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_product);
    }

//    private void matching() {
//        prevBtn = (ImageButton) findViewById(R.id.home_ibtn_productContent);
//        removeCoupon = (ImageButton) findViewById(R.id.coupon_ibtn_remove);
//        accept = (Button) findViewById(R.id.coupon_btn_accept);
//        checkBox = (CheckBox) findViewById(R.id.coupon_checkbox);
//        couponCode = (TextView) findViewById(R.id.coupon_tv_code);
//        couponContent = (TextView) findViewById(R.id.coupon_tv_content);
//        couponDate = (TextView) findViewById(R.id.coupon_tv_date);
//    }
}