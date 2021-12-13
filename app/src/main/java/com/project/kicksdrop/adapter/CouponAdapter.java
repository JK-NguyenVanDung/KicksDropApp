package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private static Context context;
    private static List<Coupon> mCoupon;
    private CouponAdapter.OnCouponListener mOnCouponListener;
    FirebaseUser fUser;
    public static double totalPayment;
    private static ArrayList<Product> mProducts;
    private static List<CheckBoxGroup> mCheckbox;
    private static Button apply;
    private static boolean checkCoupon;
    public static void setCheck(boolean check) {
        CouponAdapter.check = check;
    }

    private static boolean check;
    public class CheckBoxGroup{
        public CheckBoxGroup(int adapterPosition, CheckBox couponCheckbox, TextView err) {
            this.pos = adapterPosition;
            this.err = err;
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

        public TextView getErr() {
            return err;
        }

        public void setErr(TextView err) {
            this.err = err;
        }

        private TextView err;
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
        check = true;
    }




    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);

        return new CouponAdapter.ViewHolder(view, mOnCouponListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        final Coupon coupon = mCoupon.get(holder.getAdapterPosition());
        String couponCode = coupon.getCoupon_code();
        String couponDuration = coupon.getCoupon_duration();
        double couponMinPrice = coupon.getCoupon_min_price();
        double couponMaxPrice = coupon.getCoupon_max_price();
        String couponPercent = coupon.getCoupon_percent();
        if(coupon.getCondition_text() != null){
            holder.couponContent.setText("Condition: "+ coupon.getCondition_text() + "\n\nGet " + couponPercent + "% off for an order from $" + couponMinPrice+"\nMaximum discount: $"+ couponMaxPrice);
        }else{
            holder.couponContent.setText("Get " + couponPercent + "% off for an order from $" + couponMinPrice+"\nMaximum discount: $"+ couponMaxPrice);
        }
        holder.couponDate.setText("Exp: " + couponDuration);
        holder.couponCode.setText(couponCode);
        if(coupon.getCoupon_checked()){
            holder.couponCheckbox.setChecked(true);
        }

        CheckBoxGroup cbg = new CheckBoxGroup(holder.getAdapterPosition(),holder.couponCheckbox, holder.err);
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon_id = coupon.getCoupon_id();
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                assert fUser != null;
                delCoupon(fUser.getUid(),coupon_id);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mCoupon == null ? 0 : mCoupon.size();
    }
    public static boolean noChecked = true;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton delete;
        TextView couponContent, couponDate, couponCode, err;
        CheckBox couponCheckbox;
        CouponAdapter.OnCouponListener onCouponListener;
        public ViewHolder(@NonNull View itemView, OnCouponListener onProductListener) {

            super(itemView);
            err = itemView.findViewById( R.id.coupon_tv_err );
            delete = itemView.findViewById(R.id.coupon_ibtn_remove);
            couponContent = itemView.findViewById(R.id.coupon_tv_content);
            couponDate = itemView.findViewById(R.id.coupon_tv_date);
            couponCode = itemView.findViewById(R.id.coupon_tv_code);
            couponCheckbox = itemView.findViewById(R.id.coupon_checkbox);
            this.onCouponListener = onProductListener;
            itemView.setOnClickListener(this);

            CouponAdapter.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    noChecked = true;

                    for(CheckBoxGroup cb : mCheckbox){
                        if(cb.getCheck().isChecked() ) {
                            int position = cb.getPos();
                            String id = mCoupon.get(position).getCoupon_id();
                            double min_price = mCoupon.get(position).getCoupon_min_price();
                            String coupon_condition = mCoupon.get(position).getCoupon_condition();
                            int today = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format( Calendar.getInstance().getTime()));
                            String day = mCoupon.get(position).getCoupon_duration().substring(0,2);
                            String month = mCoupon.get(position).getCoupon_duration().substring(3,5);
                            String year = mCoupon.get(position).getCoupon_duration().substring(6,10);

                            int couponDuration = Integer.parseInt( year + month + day );


                            if (totalPayment == 0){
                                Toast.makeText(v.getContext(), "Cart is empty",Toast.LENGTH_SHORT).show();
                            }
                            else if (couponDuration < today){
                                FirebaseUser fUser;
                                Toast.makeText(v.getContext(), "Coupon Expired",Toast.LENGTH_SHORT).show();
                                String coupon_id = mCoupon.get(position).getCoupon_id();
                                fUser = FirebaseAuth.getInstance().getCurrentUser();
                                assert fUser != null;
                                delCouponDuration(fUser.getUid(),coupon_id);
                            }
                            else if(CouponAdapter.totalPayment < min_price)
                            {
                                Toast.makeText(v.getContext(), "Your Total Payment is not enough to use this coupon",Toast.LENGTH_SHORT).show();
                            }else if ( coupon_condition != null){
                                //
                                FirebaseUser fUser;
                                fUser = FirebaseAuth.getInstance().getCurrentUser();
                                assert fUser != null;
                                String brand = mCoupon.get(position).getCoupon_condition().substring(2).toLowerCase();
                                int count =Integer.parseInt( mCoupon.get(position).getCoupon_condition().substring(0,1) );
                                getCart(fUser.getUid(), brand, count, onCouponListener, position, v, id, cb.getErr() );
                                noChecked = false;
                                break;
                            }
                            else if (CouponAdapter.totalPayment > min_price){
                                onCouponListener.onCouponClick(position, v, id,true);
                                noChecked = false;
                                break;
                            }
                        }
                    }
                    if(noChecked){
                        Toast.makeText(v.getContext(), "Select a coupon to use!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnCouponListener {
        void onCouponClick(int position, View view, String id, boolean check);
    }

    private void delCoupon(String idUser, String coupon_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+idUser+"/coupon");
        new AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage("Do you want to delete this coupon?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.child(coupon_id).removeValue();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private static void getProduct(List<HashMap<String, String>> cartProducts, String brand, int count, OnCouponListener onCouponListener, int position, View v, String id, TextView err){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProducts = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProducts.clear();
                for(HashMap<String, String> item : cartProducts){
                    for(DataSnapshot dtShot: snapshot.getChildren()){
                        Product product = dtShot.getValue(Product.class);
                        assert product != null;
                        product.setProduct_id(dtShot.getKey());
                        product.getProduct_colors().remove(0);
                        String id = product.getProduct_id().toLowerCase();
                        if(id.equals(Objects.requireNonNull(item.get("productId")).toLowerCase())){
                            if (product.getProduct_brand().toLowerCase().equals(brand)){
                                mProducts.add(product);
                            }
                        }
                    }
                }
                int size = mProducts.size();
                if (size >= count){
                    onCouponListener.onCouponClick(position, v, id,true);
                    check = false;
                    err.setVisibility(View.GONE);
                }else if (check){
                    err.setVisibility(View.VISIBLE);
                    check = false;
                }




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static void getCart(String user_Id, String brand, int count, OnCouponListener onCouponListener, int position, View v, String id, TextView err){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+user_Id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HashMap<String,String>> productsInCart = new ArrayList<HashMap<String,String>>();
                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                if(hashMap != null) {
                    HashMap<String, Object> listProduct = (HashMap<String, Object>) hashMap.get("product");
                    for (Map.Entry<String, Object> entry : listProduct.entrySet()) {
                        String key = entry.getKey();
                        HashMap<String, String> item = (HashMap<String, String>) listProduct.get(key);
                        item.put("cartProductID", key);
                        productsInCart.add(item);
                    }
                    getProduct(productsInCart, brand, count, onCouponListener, position, v , id, err);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private static void delCouponDuration(String idUser, String coupon_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/"+idUser+"/coupon");

        myRef.child(coupon_id).removeValue();

    }


}