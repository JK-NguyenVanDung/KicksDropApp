
package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Chat;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{


    private Context context;
    private static List<Product> mProductList;
    private ProductListAdapter.OnProductListener mOnProductListener;
    private LoadingScreen loading;
    public ProductListAdapter(Context context, List<Product> mProductList, ProductListAdapter.OnProductListener onProductListener, LoadingScreen loading){

        this.context = context;
        this.mProductList = mProductList;
        this.mOnProductListener = onProductListener;
        this.loading = loading;
    }
    public ProductListAdapter(Context context, List<Product> mProductList, ProductListAdapter.OnProductListener onProductListener){

        this.context = context;
        this.mProductList = mProductList;
        this.mOnProductListener = onProductListener;
    }
    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new ProductListAdapter.ViewHolder(view,mOnProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        final Product product = mProductList.get(position);
        String color = product.getProduct_images().get(1).get("color");
        String imageName = product.getProduct_images().get(1).get("image");
        //holder.colorCircle.getForeground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();

        backgroundGradient.setColor(Color.parseColor(color));

        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(product.getProduct_price());
        holder.price.setText(sPrice);

        holder.name.setText(product.getProduct_name());
        loadImage(holder.productImage,imageName);
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getUserWishlist(fUser.getUid(), product, holder.heart);

        holder.heart.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (holder.heart.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_heart).getConstantState()){
                    holder.heart.setImageResource(R.drawable.ic_heart_activated);
                    String idUser = fUser.getUid();
                    addProductWishlist(idUser,product);
                    Toast.makeText(context,"Product is saved into Wishlist", Toast.LENGTH_LONG).show();

                }else{
                holder.heart.setImageResource(R.drawable.ic_heart);
                String idUser = fUser.getUid();
                delProductWishlist(idUser,product.getProduct_id());
                Toast.makeText(context,"Product is removed into Wishlist", Toast.LENGTH_LONG).show();

                }

        }});

    }
    private void getUserWishlist(String user_id, Product product , ImageButton heart){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+user_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> listWishlist =new ArrayList<String>();
                for (DataSnapshot item : snapshot.getChildren()){
                    listWishlist.add(item.getKey());
                }
                for (int i =0 ; i < listWishlist.size();i++){
                    if(product.getProduct_id().equals(listWishlist.get(i))){
                        heart.setImageResource(R.drawable.ic_heart_activated);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addProductWishlist(String idUser,Product product){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+idUser);
        myRef.child(product.getProduct_id()).child("product_id").setValue(product.getProduct_id());
        myRef.child(product.getProduct_id()).child("product_size").setValue(product.getProduct_sizes().get(1));
        myRef.child(product.getProduct_id()).child("product_color").setValue(product.getProduct_colors().get(1));

    }

    private void delProductWishlist(String idUser,String idProduct){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+idUser);
        myRef.child(idProduct).removeValue();

    }

    private void loadImage(ImageView image, String imageName){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imageName);
        try {
            File file = File.createTempFile("tmp",".png");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    BitmapDrawable ob = new BitmapDrawable(bitmap);
                    loading.dismissDialog();

                    image.setBackground(ob);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return mProductList ==null? 0: mProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageButton heart;
        ImageView colorCircle,productImage;
        TextView name,price;
        ProductListAdapter.OnProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            heart = itemView.findViewById(R.id.btn_itemProduct_heart);
            colorCircle = itemView.findViewById(R.id.imgv_itemProduct_circle);
            productImage = itemView.findViewById(R.id.imgv_productImg);
            name = itemView.findViewById(R.id.tv_itemProduct_name);
            price = itemView.findViewById(R.id.tv_itemProduct_cost);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = mProductList.get(position).getProduct_id();
            onProductListener.onProductClick(position,v,id);

        }
    }
    public interface OnProductListener{
        void onProductClick(int position, View view, String id);
    }
}
