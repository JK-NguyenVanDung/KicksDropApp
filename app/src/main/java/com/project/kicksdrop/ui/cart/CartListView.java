package com.project.kicksdrop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.kicksdrop.R;

public class CartListView extends AppCompatActivity {

    ImageButton prevIBtn, removeIBtn, colorIBtn;
    TextView totalItems, totalMoney, totalPayment, nameProduct,
            typeProduct;
    Button selectCouponBtn, orderBtn;
    Spinner sizeProduct, amountProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list_view);
    }

    private void matching() {
        prevIBtn = (ImageButton) findViewById(R.id.ibtn_CartListView_prev);
        removeIBtn = (ImageButton) findViewById(R.id.cartListView_ibtn_remove);
        colorIBtn = (ImageButton) findViewById(R.id.cartListView_ibtn_yellow);
        totalItems = (TextView) findViewById((R.id.cartListView_tv_totalItems));
        totalMoney = (TextView) findViewById((R.id.cartListView_tv_totalMoney));
        totalPayment = (TextView) findViewById((R.id.cartListView_tv_payment));
        nameProduct = (TextView) findViewById((R.id.cartListView_tv_name));
        typeProduct = (TextView) findViewById((R.id.cartListView_tv_type));
        sizeProduct = (Spinner) findViewById((R.id.cartListView_spinner_dropDownSize));
        amountProduct = (Spinner) findViewById((R.id.cartListView_spinner_dropDownAmount));
        selectCouponBtn = (Button) findViewById((R.id.cartListView_btn_selectCoupon));
        orderBtn = (Button) findViewById((R.id.cartListView_btn_order));
    }
}