package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.simpleratingbar.OnRatingChangedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{
    private Context context;
    private  List<HashMap<String,String>> mDetail;
    private List<Product> products;
    private List<Product> ratedProducts;
    private FirebaseUser fUser;
    private Button rate;
    private AlertDialog dialog;
    private String orderId;
    public RatingAdapter(Context context, List<HashMap<String, String>> mDetail, Button rate, ArrayList<Product> products, AlertDialog dialog,String orderId) {
        this.context = context;
        this.mDetail = mDetail;
        this.rate = rate;
        this.products = products;
        this.dialog = dialog;
        this.orderId = orderId;
        ratedProducts = new ArrayList<>();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_rating_product, parent, false);

        return new RatingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(holder.getAdapterPosition());

        holder.name.setText(product.getProduct_name());
        float rating = holder.rating.getRating();
        holder.rating.setOnRatingChangedListener(new OnRatingChangedListener() {
            @Override
            public void onRatingChange(float v, float v1) {
                product.setTemp_rating(rating);
            }
        });
        ratedProducts.add(product);
        rate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if( holder.rating.getRating() <1.0) {
                    Toast.makeText(context,"Please enter a rating between 1 and 5 inclusively!",Toast.LENGTH_SHORT).show();

                }
                else{
                    saveRating();

                }
            }
        });
    }
    private boolean rated;
    private void saveRating(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference myRef = database.getReference("order/" + fUser.getUid()  + "/" + orderId);
        for(Product product: ratedProducts){
            double avgRating = product.getProduct_rating();
            long ratingAmount = product.getRating_amount();
            double rating = product.getTemp_rating();
            double finalRating = 0.0;
            long newAmount= 0;
            if(ratingAmount >= 1){
                double totalRating = avgRating * ratingAmount;
                newAmount =  (ratingAmount + 1);
                double calculatedRating = totalRating +  rating;

                finalRating =  Math.floor((calculatedRating / newAmount) * 10) / 10;


            }else{
                newAmount= 1;
                finalRating =  Math.floor((rating) * 10) / 10;
            }


            DatabaseReference ref = database.getReference("product/" + product.getProduct_id());
            ref.child("rating_amount").setValue(newAmount);
            ref.child("product_rating").setValue(finalRating);
            myRef.child("status").setValue("Rated");
            Toast.makeText(context,"Rating Completed, Thanks you for trusting us!",Toast.LENGTH_SHORT).show();

            dialog.dismiss();



        }


    }
    @Override
    public int getItemCount() {
        return mDetail ==null? 0: mDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        com.borjabravo.simpleratingbar.SimpleRatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rating_tv_productName);
            rating = itemView.findViewById(R.id.rating_ratingStar);

        }


    }

}



