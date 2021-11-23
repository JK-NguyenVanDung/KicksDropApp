package com.project.kicksdrop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
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
        view = LayoutInflater.from(context).inflate(R.layout.item_cart_product_add, parent, false);
        return new WishlistAdapter.ViewHolder(view);
    }



    private void loadImage(ImageView image, String imageName){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imageName);
        try {
            File file = File.createTempFile("tmp",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    BitmapDrawable ob = new BitmapDrawable(bitmap);

                    image.setBackground(ob);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {


        final Product product = mWishlist.get(position);
        String color = product.getProduct_images().get(1).get("color");
        String imageName = product.getProduct_images().get(1).get("image");
        //holder.colorCircle.getForeground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);


        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(product.getProduct_price());
        holder.price.setText(sPrice);

        holder.name.setText(product.getProduct_name());
        loadImage(holder.avt,imageName);
    }

    @Override
    public int getItemCount() {
        return mWishlist ==null? 0: mWishlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avt;
        TextView name,price,type;
        Spinner size;
        Button moreOption;
        ImageButton remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avt = itemView.findViewById(R.id.iv_item_productCart_addToCart_image);
            name = itemView.findViewById(R.id.wishlist_tv_productName);
            price = itemView.findViewById(R.id.wishlist_tv_productCost);
            type = itemView.findViewById(R.id.wishlist_tv_productType);
            size = itemView.findViewById(R.id.wishlist_spinner_dropDownSize);
            moreOption = itemView.findViewById(R.id.wishList_btn_addToCart);
            remove = itemView.findViewById(R.id.wishlist_ibtn_remove);

        }
    }
}
