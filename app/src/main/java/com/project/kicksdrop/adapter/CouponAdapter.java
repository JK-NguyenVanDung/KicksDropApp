package com.project.kicksdrop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;

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
    private int checkedCoupon = -1;
    private static List<CheckBoxGroup> mCheckbox;
    private static Button apply;
    public class CheckBoxGroup{
        public CheckBoxGroup(int adapterPosition, CheckBox couponCheckbox) {
            this.pos = adapterPosition;
            this.check = couponCheckbox;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public CheckBox getCheck() {
            return check;
        }

        public void setCheck(CheckBox check) {
            this.check = check;
        }

        private int pos;
        private CheckBox check;


    }
    public CouponAdapter(Context context, List<Coupon> mCoupon, double totalPayment, Button apply,CouponAdapter.OnCouponListener onCouponListener) {
        this.context = context;
        this.mCoupon = mCoupon;
        this.totalPayment = totalPayment;
        CouponAdapter.apply = apply;
        this.mOnCouponListener = onCouponListener;
        mCheckbox = new ArrayList<>();
    }




    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);

        return new CouponAdapter.ViewHolder(view, mOnCouponListener);
    }

    public void check(String id, int pos){
        for(Coupon cp : mCoupon){
            if(cp.getCoupon_id().equals(id)){
                cp.setCoupon_checked(true);
//                Toast.makeText(context,String.valueOf(cp.getCoupon_checked()),Toast.LENGTH_SHORT).show();
            }else{
                cp.setCoupon_checked(false);
            }

        }
    }
    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        final Coupon coupon = mCoupon.get(holder.getAdapterPosition());
        String couponCode = coupon.getCoupon_code();
        String couponDuration = coupon.getCoupon_duration();
        String couponMaxPrice = coupon.getCoupon_max_price();
        String couponPercent = coupon.getCoupon_percent();

        holder.couponContent.setText("Giam " + couponPercent + "toi da " + couponMaxPrice);
        holder.couponDate.setText("HSD: " + couponDuration);
        holder.couponCode.setText("MA " + couponCode);
        if(coupon.getCoupon_checked()){
            holder.couponCheckbox.setChecked(true);
        }

        CheckBoxGroup cbg = new CheckBoxGroup(holder.getAdapterPosition(),holder.couponCheckbox);
        mCheckbox.add(cbg);

        holder.couponCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CheckBoxGroup cb : mCheckbox){
                    if(holder.couponCheckbox.isChecked() && cb.getPos() == holder.getAdapterPosition()) {
                        cb.getCheck().setChecked(true);
                        mCoupon.get(holder.getAdapterPosition()).setCoupon_checked(true);
                    }else{
                        cb.getCheck().setChecked(false);
                    }

                }

            }
        });


//        apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                int i1 = percent;
////                int i2 = maxprice;
////                if ( i1 != 0 && i2 != 0 ){
////                    finaldiscount =  calculateTotal(totalPayment, percent, maxprice);
////                }
//               // Intent intent = new Intent(context.getApplicationContext(), CartAdapter.class);
//                //intent.putExtra("discount", finaldiscount);
//
//                //Log.d("test",finaldiscount);
////                Intent intent = new Intent();
////                resultIntent.putExtra("result", finaldiscount);
//
//            }
//
//
//        });



//        holder.btn_apply.setOnClickListener(new View.OnClickListener() {
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
        public ViewHolder(@NonNull View itemView, OnCouponListener onProductListener) {

            super(itemView);
            couponContent = itemView.findViewById(R.id.coupon_tv_content);
            couponDate = itemView.findViewById(R.id.coupon_tv_date);
            couponCode = itemView.findViewById(R.id.coupon_tv_code);
            couponCheckbox = itemView.findViewById(R.id.coupon_checkbox);
            this.onCouponListener = onProductListener;
            itemView.setOnClickListener(this);
            CouponAdapter.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(CheckBoxGroup cb : mCheckbox){
                        if(cb.getCheck().isChecked() ) {
                            int position = cb.getPos();
                            String id = mCoupon.get(position).getCoupon_id();
                            onCouponListener.onCouponClick(position, v, id);
                            break;
                        }
                    }

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