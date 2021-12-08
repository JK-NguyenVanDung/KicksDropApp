package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kicksdrop.MessagePopUp;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;


import java.util.ArrayList;
import java.util.HashMap;
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
        String type =  coupon.getCoupon_type();
//        HashMap<String,String> types = new HashMap<String,String>(){{
//            put("FREE SHIP","ic_car");
//            put("DISCOUNT PERCENT","ic_discount");
//            put("DAILY","ic_calendar");
//            put("COMBO","ic_ticket_star");
//        }};
//        String icon = types.get(type);

        if (type.equals("FREE SHIP")){
            holder.icon.setImageResource(R.drawable.ic_car);
        }
        else if (type.equals("DISCOUNT PERCENT")){
            holder.icon.setImageResource(R.drawable.ic_discount);
        }
        else if (type.equals("DAILY")){
            holder.icon.setImageResource(R.drawable.ic_calendar);
        }
        else {
            holder.icon.setImageResource(R.drawable.ic_ticket_star);
        }
        holder.btn.setText(coupon.getCoupon_name());

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String idUser = fUser.getUid().toString();
                addCoupon(idUser,coupon.getCoupon_id());
                MessagePopUp messagePopUp = new MessagePopUp();
                messagePopUp.show(context,"Added Coupon Successfully");

            }
        });

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String idUser = fUser.getUid().toString();
                addCoupon(idUser,coupon.getCoupon_id());
                MessagePopUp messagePopUp = new MessagePopUp();
                messagePopUp.show(context,"Add To Coupon Successfully");

            }
        });



    }

    public void addCoupon(String userID, String couponID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("account/"+userID);
        myRef.child("coupon").child(couponID).setValue(couponID);
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
