package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Product;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{

    private Context context;
    private  List<Product> mWishlist;

    public WishlistAdapter(Context context, List<Product> mWishlist) {
        this.context = context;
        this.mWishlist = mWishlist;
    }



    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_circle_container, parent, false);

        return new WishlistAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
