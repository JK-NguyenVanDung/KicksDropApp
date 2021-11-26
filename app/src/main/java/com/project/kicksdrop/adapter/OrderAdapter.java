package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Cart;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Order;

import java.util.HashMap;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private static List<Order> mOrder;
    private OrderAdapter.OnOrderListener mOnOrderListener;
    public OrderAdapter(Context context, List<Order> mOrder, OrderAdapter.OnOrderListener OnOrderListener){
        this.context = context;
        this.mOrder = mOrder;
        this.mOnOrderListener = OnOrderListener;
    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);

        return new OrderAdapter.ViewHolder(view,mOnOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        final Cart cart;
        String coupon_id;
        String order_Create_Date;
        String order_price;
        String user_id;
        List<HashMap<String,String>> Product;


    }


    @Override
    public int getItemCount() {
        return mOrder ==null? 0: mOrder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        OrderAdapter.OnOrderListener onOrderListener;

        public ViewHolder(@NonNull View itemView, OrderAdapter.OnOrderListener OnOrderListener) {
            super(itemView);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = mOrder.get(position).getOrder_id();
            onOrderListener.onOrderClick(position,v,id);
        }
    }
    public interface OnOrderListener extends CouponAdapter.OnCouponListener {
        void onOrderClick(int position, View view, String id);
    }
}
