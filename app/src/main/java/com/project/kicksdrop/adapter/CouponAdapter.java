package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{

    private Context context;
    private static List<Coupon> mCoupon;
    private CouponAdapter.OnCouponListener mOnCouponListener;

    public CouponAdapter(Context context, List<Coupon> mCoupon, CouponAdapter.OnCouponListener OnCouponListener){
        this.context = context;
        this.mCoupon = mCoupon;
        this.mOnCouponListener = OnCouponListener;
    }
    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);

        return new CouponAdapter.ViewHolder(view,mOnCouponListener);
    }


    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        final Coupon coupon = mCoupon.get(position);
        String couponCode = coupon.getCoupon_code();
        String couponDuration = coupon.getCoupon_duration();
        String couponMaxPrice = coupon.getCoupon_max_price();
        String couponPercent = coupon.getCoupon_percent();


    }


    @Override
    public int getItemCount() {
        return mCoupon ==null? 0: mCoupon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CouponAdapter.OnCouponListener onCouponListener;

        public ViewHolder(@NonNull View itemView, OnCouponListener OnCouponListener) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = mCoupon.get(position).getCoupon_id();
            onCouponListener.onCouponClick(position,v,id);
        }
    }
    public interface OnCouponListener{
        void onCouponClick(int position, View view, String id);
    }
}
