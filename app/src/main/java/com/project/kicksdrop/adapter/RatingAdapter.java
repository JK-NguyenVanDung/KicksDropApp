package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Product;

import java.util.HashMap;
import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{
    private Context context;
    private  List<HashMap<String,String>> mRating;
    private RatingAdapter.OnRatingListener mOnRatingListener;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_rating_product, parent, false);

        return new RatingAdapter.ViewHolder(view,mOnRatingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRating ==null? 0: mRating.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameProduct;
        com.borjabravo.simpleratingbar.SimpleRatingBar rating;
        RatingAdapter.OnRatingListener OnRatingListener;
        public ViewHolder(@NonNull View itemView, OnRatingListener mOnRatingListener) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.rating_tv_productName);
            rating = itemView.findViewById(R.id.rating_ratingStar);
            this.OnRatingListener = mOnRatingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }

    }
    public interface OnRatingListener{
        void onRatingClick(int position, View view, String id);
    }
}



