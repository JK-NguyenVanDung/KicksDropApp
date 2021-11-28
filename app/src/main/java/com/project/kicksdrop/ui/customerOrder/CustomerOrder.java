package com.project.kicksdrop.ui.customerOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.BillProductAdapter;
import com.project.kicksdrop.adapter.CartAdapter;
import com.project.kicksdrop.adapter.OrderProductAdapter;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrder extends AppCompatActivity {

    FirebaseUser fUser;
    RecyclerView recyclerView;
    BillProductAdapter billProductAdapter;
    private  ArrayList<Order> mOrder;
    private ArrayList<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrder.clear();

                for (DataSnapshot dtShot: snapshot.getChildren()){
                    HashMap<String,Object> hashMap = (HashMap<String, Object>) dtShot.getValue();
                    Log.d("Test", hashMap.toString());
                    Order order = dtShot.getValue(Order.class);
                    assert order != null;
                    mProducts = new ArrayList<>();
                    mOrder.add(order);
                }
                for(Order order: mOrder){
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mProducts.clear();
                            for(HashMap<String, String> item : order.getOrder_details()){
                                for(DataSnapshot dtShot: snapshot.getChildren()){
                                    Product product = dtShot.getValue(Product.class);
                                    assert product != null;
                                    product.getProduct_colors().remove(0);
                                    for(String color: product.getProduct_colors()){
                                        String cartProductId = dtShot.getKey() + color.substring(1);
                                        if(cartProductId.equals(item.get("cartProductID"))){
                                            product.setProduct_id(dtShot.getKey());
                                            product.getProduct_images().remove(0);
                                            mProducts.add(product);
                                        }
                                    }

                                }
                            }
                            OrderProductAdapter adapter = new OrderProductAdapter(getApplicationContext(), mProducts, order.getOrder_details());
                            order.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

//                Log.d("Test", mOrder.toString());

                billProductAdapter = new BillProductAdapter(getApplicationContext(),mOrder);

                recyclerView.setAdapter(billProductAdapter);




//                List<HashMap<String,Object>> listOrder = new ArrayList<>();
//                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
//                if (hashMap != null){
//                    for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
//                        String key = entry.getKey();
//                        HashMap<String, Object> item = (HashMap<String, Object>) hashMap.get(key);
//                        item.put("cartProductID", key);
//                        listOrder.add(item);
//                    }
//                    for(HashMap<String, Object> item : listOrder){
//                            mOrder.add(item);
//
//                    }

//
//                    //billProductAdapter = new BillProductAdapter(getApplicationContext(),productsInCart);
//                   //Log.d("test",mProducts.toString());
//                    //recyclerView.setAdapter(orderProductAdapter);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}