package com.project.kicksdrop.ui.productBrands;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.kicksdrop.R;

public class ProductBrands extends AppCompatActivity {

    ImageButton prevIBtn, cartIBtn, chatIBtn;
    EditText searchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brands);
    }

    private void matching() {
        prevIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_prev);
        cartIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_cart);
        chatIBtn = (ImageButton) findViewById(R.id.productBrands_iBtn_chat);
        searchProduct = (EditText) findViewById(R.id.productBrands_iBtn_search);
    }
}