package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Image;
import com.project.kicksdrop.model.Product;


import java.util.List;

public class HomeCouponAdapter extends RecyclerView.Adapter<HomeCouponAdapter.ViewHolder>{

    private Context context;
    private List<Coupon> mCoupon;
    HomeCouponAdapter.OnCouponListener mOnCouponListener;

    public HomeCouponAdapter(Context context, List<Coupon> mCoupon, OnCouponListener mOnCouponListener) {
        this.context = context;
        this.mCoupon = mCoupon;
        this.mOnCouponListener = mOnCouponListener;
    }

    @NonNull
    @Override
    public HomeCouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_home_coupon, parent, false);

        return new HomeCouponAdapter.ViewHolder(view,mOnCouponListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCouponAdapter.ViewHolder holder, int position) {
        final Coupon coupon = mCoupon.get(position);

    }

    @Override
    public int getItemCount() {
        return mCoupon ==null? 0: mCoupon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton icon;
        Button btn;
        HomeCouponAdapter.OnCouponListener onCouponListener;
        public ViewHolder(@NonNull View itemView, OnCouponListener onCouponListener) {
            super(itemView);
            icon = (ImageButton)itemView.findViewById(R.id.home_ibtn_freeShipment);
            btn= (Button) itemView.findViewById(R.id.home_btn_freeShipment);
            this.onCouponListener = onCouponListener;

        }
    }
    public interface OnCouponListener{
        void onCouponClick(int position, View view, String id);
    }
}
