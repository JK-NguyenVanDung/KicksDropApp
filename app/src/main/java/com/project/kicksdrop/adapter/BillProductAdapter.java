package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

    List<Order> mOrderList;
    private ArrayList<Product> mProducts;
    private Context context;
    OrderProductAdapter orderProductAdapter;

    public BillProductAdapter(Context context, List<Order>  mOrderList){
        this.context = context;
        this.mOrderList = mOrderList;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public BillProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_product, parent, false);

        return new BillProductAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull BillProductAdapter.ViewHolder holder, int position) {
        final Order order = mOrderList.get(holder.getAdapterPosition());
        String totalPayment = String.valueOf(
                                    Double.parseDouble(order.getOrder_price()) -
                                    Double.parseDouble(order.getOrder_discount()) +
                                    Double.parseDouble(order.getShipping_price()));

        holder.tv_address.setText(order.getAddress());
        holder.tv_total.setText(order.getOrder_price());
        holder.tv_shipPrice.setText(order.getShipping_price());
        holder.tv_discount.setText(order.getOrder_discount());
        holder.tv_totalPayment.setText(totalPayment);


        //
        holder.recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);

        holder.recyclerView.setLayoutManager(linearLayoutManager);
        getProduct(order.getOrder_details(),holder.recyclerView);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_total, tv_shipPrice, tv_discount, tv_totalPayment;
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.order_rv_products_bill);
            tv_address = itemView.findViewById(R.id.customerOrder_tv_address);
            tv_total = itemView.findViewById(R.id.customerOrder_tv_total);
            tv_shipPrice = itemView.findViewById(R.id.customerOrder_tv_shipmentPrice);
            tv_discount = itemView.findViewById(R.id.customerOrder_tv_discount);
            tv_totalPayment = itemView.findViewById(R.id.customerOrder_tv_totalpayment);
        }
    }
    private void getProduct(List<HashMap<String,String>> OrderProductList,RecyclerView recyclerView){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProducts = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for(HashMap<String, String> item : OrderProductList){
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
                orderProductAdapter = new OrderProductAdapter(context,mProducts,OrderProductList);
                Log.d("test",mProducts.toString());
                recyclerView.setAdapter(orderProductAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mOrderList ==null? 0: mOrderList.size();
    }

}
