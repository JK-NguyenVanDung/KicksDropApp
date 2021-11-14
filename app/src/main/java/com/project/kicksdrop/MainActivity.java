package com.project.kicksdrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.kicksdrop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Spinner dropDownProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_wishlist, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        matching();
        spinner();

    }

    private void matching() {
        dropDownProduct = (Spinner) findViewById(R.id.sp_item_product_cart_dropDownSize);
    }

    private void spinner() {
        ProductSize[] productSize = ProductSizeData.getSizeProduct();
        ArrayAdapter<ProductSize> adapter = new ArrayAdapter<ProductSize>(this, android.R.layout.simple_spinner_item, productSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.dropDownProduct.setAdapter(adapter);

        //when user select a list item
        this.dropDownProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelected(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        ProductSize productSize = (ProductSize) adapter.getItem(position);

        Toast.makeText(getApplicationContext(), "Size" + productSize.getSizeProduct(), Toast.LENGTH_SHORT).show();
    }

}