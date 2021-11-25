package com.project.kicksdrop.adapter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.promocode.CouponProduct;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private Context context;
    private static List<Coupon> mCoupon;
    private CouponAdapter.OnCouponListener mOnCouponListener;
    private double price;
    public double totalPayment;
    public int percent;
    public int maxprice;
    private String finaldiscount;
    Button accept;

    public CouponAdapter(Context context, List<Coupon> mCoupon, double totalPayment, Button accept,CouponAdapter.OnCouponListener onCouponListener) {
        this.context = context;
        this.mCoupon = mCoupon;
        this.totalPayment = totalPayment;
        this.accept = accept;
        this.mOnCouponListener = onCouponListener;
    }




    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);

        return new CouponAdapter.ViewHolder(view, mOnCouponListener);
    }


    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        final Coupon coupon = mCoupon.get(position);
        String couponCode = coupon.getCoupon_code();
        String couponDuration = coupon.getCoupon_duration();
        String couponMaxPrice = coupon.getCoupon_max_price();
        String couponPercent = coupon.getCoupon_percent();

        holder.couponContent.setText("Giam " + couponPercent + "toi da " + couponMaxPrice);
        holder.couponDate.setText("HSD: " + couponDuration);
        holder.couponCode.setText("MA " + couponCode);




        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int i1 = percent;
//                int i2 = maxprice;
//                if ( i1 != 0 && i2 != 0 ){
//                    finaldiscount =  calculateTotal(totalPayment, percent, maxprice);
//                }
               // Intent intent = new Intent(context.getApplicationContext(), CartAdapter.class);
                //intent.putExtra("discount", finaldiscount);

                //Log.d("test",finaldiscount);
//                Intent intent = new Intent();
//                resultIntent.putExtra("result", finaldiscount);

            }


        });



//        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return mCoupon == null ? 0 : mCoupon.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView couponContent, couponDate, couponCode;
        CheckBox couponCheckbox;
        CouponAdapter.OnCouponListener onCouponListener;
        double t1;
        //Button btn_accept;

        public ViewHolder(@NonNull View itemView, OnCouponListener onProductListener) {

            super(itemView);
            couponContent = itemView.findViewById(R.id.coupon_tv_content);
            couponDate = itemView.findViewById(R.id.coupon_tv_date);
            couponCode = itemView.findViewById(R.id.coupon_tv_code);
            couponCheckbox = itemView.findViewById(R.id.coupon_checkbox);
            //btn_accept = itemView.findViewById(R.id.coupon_btn_accept);
            this.onCouponListener = onProductListener;
            itemView.setOnClickListener(this);
            couponCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String id = mCoupon.get(position).getCoupon_id();
                    onCouponListener.onCouponClick(position, v, id);
                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnCouponListener {
        void onCouponClick(int position, View view, String id);
    }

    private String calculateTotal(double totalPayment, int percent, int maxprice) {
            double sPrice = totalPayment;
        double discount = (sPrice * percent) / 100;
        if (discount > maxprice) {
            discount = maxprice;
        }
        String finalTotalDiscount = String.valueOf(discount);
        return finalTotalDiscount;
    }

}