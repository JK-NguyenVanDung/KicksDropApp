package com.project.kicksdrop.ui.customerOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
                    mOrder.add(order);
                }
                if(mOrder.size() < 1){
                    loading.dismissDialog();
                }
                userOrderAdapter = new UserOrderAdapter(getApplicationContext(),mOrder,loading);

                recyclerView.setAdapter(userOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}