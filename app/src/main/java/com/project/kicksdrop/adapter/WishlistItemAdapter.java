package com.project.kicksdrop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.kicksdrop.model.Product;

import java.util.List;

public class WishlistItemAdapter extends RecyclerView.Adapter<WishlistItemAdapter.ViewHolder>{
    private Context context;
    private List<Product> mWishlist;


    public WishlistItemAdapter(Context context, List<Product> mWishlist) {

        this.context = context;
        this.mWishlist = mWishlist;
    }

    @NonNull
    @Override
    public WishlistItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistItemAdapter.ViewHolder holder, int position) {

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
