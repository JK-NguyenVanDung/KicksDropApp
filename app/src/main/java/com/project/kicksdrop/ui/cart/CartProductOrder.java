package com.project.kicksdrop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.project.kicksdrop.R;

public class CartProductOrder extends AppCompatActivity {

    ImageButton prevBtn;
    Button orderBtn;
    TextView couponText, couponPercent,
            location, methodShipment, shippingCost,
            total, shipmentPrice, discount, totalPayment,
            productName, productPrice, productSize, productAmount;
    RadioButton rMethodReceived, rMethodCredit, rMethodDebit;
    RadioGroup rGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_order);
    }

    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.order_ibtn_prev);
        orderBtn = (Button) findViewById(R.id.order_btn_makeOrder);

        couponText = (TextView) findViewById(R.id.order_tv_coupon);
        couponPercent = (TextView) findViewById(R.id.order_tv_couponPercent);
        location = (TextView) findViewById(R.id.order_tv_address);
        methodShipment = (TextView) findViewById(R.id.order_tv_methodShipment);
        shippingCost = (TextView) findViewById(R.id.order_tv_shippingCost);
        total = (TextView) findViewById(R.id.order_tv_total);
        shipmentPrice = (TextView) findViewById(R.id.order_tv_shipmentPrice);
        discount = (TextView) findViewById(R.id.order_tv_discount);
        couponText = (TextView) findViewById(R.id.order_tv_coupon);
        totalPayment = (TextView) findViewById(R.id.order_tv_totalPayment);

        productName = (TextView) findViewById(R.id.order_tv_name);
        productPrice = (TextView) findViewById(R.id.order_tv_price);
        productSize = (TextView) findViewById(R.id.order_tv_size);
        productAmount = (TextView) findViewById(R.id.order_tv_amount);

        rMethodReceived = (RadioButton) findViewById(R.id.order_rbtn_methodReceived);
        rMethodDebit = (RadioButton) findViewById(R.id.order_rbtn_methodDebit);
        rMethodCredit = (RadioButton) findViewById(R.id.order_rbtn_methodCredit);
        rGroup = (RadioGroup) findViewById(R.id.order_rbtn_radioGroup);
    }

}