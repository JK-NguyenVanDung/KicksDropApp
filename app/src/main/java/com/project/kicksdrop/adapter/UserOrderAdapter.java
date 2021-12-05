package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {

    List<Order> mOrderList;
    private ArrayList<Product> mProducts;
    private Context context;
    private LoadingScreen loading;
    public UserOrderAdapter(Context context, List<Order>  mOrderList, LoadingScreen loading){
        this.context = context;
        this.mOrderList = mOrderList;
        this.loading = loading;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public UserOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);

        return new UserOrderAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.ViewHolder holder, int position) {
        final Order order = mOrderList.get(holder.getAdapterPosition());
        String totalPayment;
        if(order.getOrder_discount() != null && order.getOrder_price() != null && order.getShipping_price() != null){
            totalPayment= String.valueOf(
                    Double.parseDouble(order.getOrder_price()) -
                            Double.parseDouble(order.getOrder_discount()) +
                            Double.parseDouble(order.getShipping_price()));
            holder.tv_discount.setText("-$" +order.getOrder_discount());

        }else{
            totalPayment= String.valueOf(
                    Double.parseDouble(order.getOrder_price()) +
                            Double.parseDouble(order.getShipping_price()));
            holder.tv_discount.setText("$0.00");

        }
        holder.tv_address.setText(order.getAddress());
        holder.tv_total.setText("$" +order.getOrder_price());
        holder.tv_shipPrice.setText("$" +order.getShipping_price());
        if(order.getOrder_discount() != null){

        }
        holder.tv_totalPayment.setText("$" +totalPayment);
        holder.tv_Status.setText(order.getStatus());
        if(order.getOrder_id() != null) {
            holder.tv_orderId.setText(order.getOrder_id().substring(9));
        }

        if (order.getQuantity_product() != null && Integer.parseInt( order.getQuantity_product() ) > 1){
            holder.tv_orderProduct.setText("  ("+order.getQuantity_product()+" product)");
        }
        else {
            holder.tv_orderProduct.setText("  ("+order.getQuantity_product()+" products)");
        }
        holder.tv_timeOrder.setText(order.getOrder_create_date());
        //
        holder.recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);

        holder.recyclerView.setLayoutManager(linearLayoutManager);

        if(order.getStatus().equals("Ordered")){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.receivedBtn.setVisibility(View.GONE);
        }else if (order.getStatus().equals("Shipping")){
            holder.deleteBtn.setVisibility(View.GONE);
            holder.receivedBtn.setVisibility(View.GONE);
        }else if (order.getStatus().equals("Shipped")){
                holder.deleteBtn.setVisibility(View.GONE);
                holder.receivedBtn.setVisibility(View.VISIBLE);
        }else if (order.getStatus().equals("Received")){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.receivedBtn.setVisibility(View.GONE);
        }
//        switch (order.getStatus()){
//            case "Ordered":
//                holder.deleteBtn.setVisibility(View.VISIBLE);
//                holder.receivedBtn.setVisibility(View.GONE);
//            case "Shipping":
//                holder.deleteBtn.setVisibility(View.GONE);
//                holder.receivedBtn.setVisibility(View.GONE);
//            case "Shipped":
//                holder.deleteBtn.setVisibility(View.GONE);
//                holder.receivedBtn.setVisibility(View.VISIBLE);
//            case "Received":
//                holder.deleteBtn.setVisibility(View.VISIBLE);
//                holder.receivedBtn.setVisibility(View.GONE);
//        }

        getProduct(order,holder.recyclerView, holder.getAdapterPosition());


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_total, tv_shipPrice, tv_discount, tv_totalPayment, tv_orderId
                , tv_orderProduct, tv_Status, tv_timeOrder;
        RecyclerView recyclerView;
        AppCompatButton deleteBtn,receivedBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.order_rv_products_bill);
            tv_timeOrder = itemView.findViewById( R.id.customerOrder_tv_hour );
            tv_Status = itemView.findViewById( R.id.customerOrder_tv_state );
            tv_orderProduct = itemView.findViewById( R.id.customerOrder_tv_amountProduct );
            tv_orderId = itemView.findViewById( R.id.customerOrder_tv_billCode );
            tv_address = itemView.findViewById(R.id.customerOrder_tv_address);
            tv_total = itemView.findViewById(R.id.customerOrder_tv_total);
            tv_shipPrice = itemView.findViewById(R.id.customerOrder_tv_shipmentPrice);
            tv_discount = itemView.findViewById(R.id.customerOrder_tv_discount);
            tv_totalPayment = itemView.findViewById(R.id.customerOrder_tv_totalpayment);

            deleteBtn = itemView.findViewById(R.id.customerOrder_btn_delete);
            receivedBtn = itemView.findViewById(R.id.customerOrder_btn_received);

        }
    }
    private void getProduct(Order order,RecyclerView recyclerView,int position){
        ArrayList<Product> products = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(HashMap<String, String> item : order.getOrder_details()){
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

                OrderProductAdapter adapter = new OrderProductAdapter(context,products, order.getOrder_details(),loading);
                recyclerView.setAdapter(adapter);
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
