package com.project.kicksdrop.ui.searchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.project.kicksdrop.R;

public class SearchViewProduct extends AppCompatActivity {

    ImageButton prevBtn, cartBtn, chatBtn;
    EditText searchView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_product);
    }

    private void matching() {
        prevBtn = (ImageButton) findViewById(R.id.search_ibtn_prev);
        cartBtn = (ImageButton) findViewById(R.id.search_ibtn_cart);
        chatBtn = (ImageButton) findViewById(R.id.search_ibtn_chat);
        searchView = (EditText) findViewById(R.id.search_et_searchView);
        recyclerView = (RecyclerView) findViewById(R.id.search_rv_products);
    }
}