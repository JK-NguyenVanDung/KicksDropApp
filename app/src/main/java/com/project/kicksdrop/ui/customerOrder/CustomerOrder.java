package com.project.kicksdrop.ui.customerOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.UserOrderAdapter;
import com.project.kicksdrop.adapter.OrderProductAdapter;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerOrder extends AppCompatActivity {

    FirebaseUser fUser;
    RecyclerView recyclerView;
    UserOrderAdapter userOrderAdapter;
    ImageButton back;
    private  ArrayList<Order> mOrder;

    public RecyclerView getProductsView() {
        return productsView;
    }

    public void setProductsView(RecyclerView productsView) {
        this.productsView = productsView;
    }

    private RecyclerView productsView;
    private ArrayList<Product> mProducts;
    private final LoadingScreen loading = new LoadingScreen(CustomerOrder.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        back = (ImageButton) findViewById(R.id.customerOrder_ibtn_prev);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loading.startLoadingScreen();;
        //recycler view
        recyclerView = (RecyclerView) findViewById(R.id.order_rv_order_List);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getOrder(fUser.getUid());
    }

    private void getOrder(String user_Id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order/"+user_Id);
        mOrder = new ArrayList<>();
        DatabaseReference ref = database.getReference("product");
        //final ArrayList<Product>[] products = new ArrayList[]{new ArrayList<>()};

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrder.clear();
                for (DataSnapshot dtShot: snapshot.getChildren()){
                    Order order = dtShot.getValue(Order.class);
                    assert order != null;
                    if(order.getOrder_details() != null){
                        ArrayList<Product> products  = getProducts(order.getOrder_details());
                        if( products.size() > 0 ){
                            OrderProductAdapter adapter = new OrderProductAdapter(getApplicationContext(),products, order.getOrder_details(),loading);
                            order.setAdapter(adapter);
                            mOrder.add(order);
                        }

                    }

                }

                loading.dismissDialog();
                userOrderAdapter = new UserOrderAdapter(getApplicationContext(),mOrder);

                recyclerView.setAdapter(userOrderAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ArrayList<Product>  getProducts(List<HashMap<String,String>> options){
        ArrayList<Product> products = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(HashMap<String, String> item : options){
                    for(DataSnapshot dtShot: snapshot.getChildren()){
                        Product product = dtShot.getValue(Product.class);
                        assert product != null;
                        product.getProduct_colors().remove(0);
                        String productId = dtShot.getKey();
                        if(productId.equals(item.get("productId"))){
                            product.setProduct_id(dtShot.getKey());
                            product.getProduct_images().remove(0);
                            product.setProduct_color(item.get("color"));
                            product.setProduct_size(item.get("size"));
                            product.setAmount(item.get("amount"));
                            products.add(product);
                        }

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return products;
    }
}