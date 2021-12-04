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

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {

    List<Order> mOrderList;
    private ArrayList<Product> mProducts;
    private Context context;

    public UserOrderAdapter(Context context, List<Order>  mOrderList){
        this.context = context;
        this.mOrderList = mOrderList;
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
        if(order.getOrder_discount() != null){
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
        holder.tv_total.setText("$" +totalPayment);
        holder.tv_shipPrice.setText("$" +order.getShipping_price());
        if(order.getOrder_discount() != null){

        }
        holder.tv_totalPayment.setText("$" +order.getOrder_price());
        holder.tv_Status.setText(order.getStatus());
        holder.tv_orderId.setText(order.getOrder_id().substring(9));
        if (Integer.parseInt( order.getQuantity_product() ) > 1){
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

        getProduct(order.getAdapter(),holder.recyclerView);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_total, tv_shipPrice, tv_discount, tv_totalPayment, tv_orderId
                , tv_orderProduct, tv_Status, tv_timeOrder;
        RecyclerView recyclerView;
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
        }
    }
    private void getProduct(OrderProductAdapter adapter,RecyclerView recyclerView ){
        Log.d("d",adapter.toString());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public int getItemCount() {
        return mOrderList ==null? 0: mOrderList.size();
    }

}
