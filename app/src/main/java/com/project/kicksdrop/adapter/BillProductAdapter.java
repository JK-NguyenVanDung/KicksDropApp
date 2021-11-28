package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillProductAdapter extends RecyclerView.Adapter<BillProductAdapter.ViewHolder> {
    RecyclerView recyclerView;
    private ArrayList<Product> mProducts;
    private Context context;
    OrderProductAdapter orderProductAdapter;

    public BillProductAdapter(Context context, List<Order> mCartProduct){

    }
    @SuppressLint("SetTextI18n")
    @Override
    public BillProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);

        return new BillProductAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull BillProductAdapter.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.order_rv_products_bill);
        }
    }
    private void getProduct(List<HashMap<String,String>> cartProducts){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProducts = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for(HashMap<String, String> item : cartProducts){
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
                orderProductAdapter = new OrderProductAdapter(context.getApplicationContext(), mProducts,cartProducts);
                Log.d("test",mProducts.toString());
                recyclerView.setAdapter(orderProductAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
