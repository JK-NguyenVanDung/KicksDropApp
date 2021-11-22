package com.project.kicksdrop.ui.nikeProducts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.kicksdrop.R;

public class NikeProducts extends AppCompatActivity {

    ImageButton prevIBtn, cartIBtn, chatIBtn;
    EditText searchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nike_products);
    }

    private void matching() {
        prevIBtn = (ImageButton) findViewById(R.id.nikes_iBtn_prev);
        cartIBtn = (ImageButton) findViewById(R.id.nikes_iBtn_cart);
        chatIBtn = (ImageButton) findViewById(R.id.nikes_iBtn_chat);
        searchProduct = (EditText) findViewById(R.id.nikes_iBtn_search);
    }
}