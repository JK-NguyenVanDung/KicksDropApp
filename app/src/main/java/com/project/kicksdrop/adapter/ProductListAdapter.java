
package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{


    private Context context;
    private static List<Product> mProductList;
    private ProductListAdapter.OnProductListener mOnProductListener;
    private LoadingScreen loading;
    private Integer resource;
    Boolean flag;
    public ProductListAdapter(Context context, List<Product> mProductList, ProductListAdapter.OnProductListener onProductListener, LoadingScreen loading){
        this.context = context;
        this.mProductList = mProductList;
        this.mOnProductListener = onProductListener;
        this.loading = loading;
    }

    public ProductListAdapter(Context context, List<Product> mProductList, ProductListAdapter.OnProductListener onProductListener, LoadingScreen loading,Boolean flag){

        this.context = context;
        this.mProductList = mProductList;
        this.mOnProductListener = onProductListener;
        this.loading = loading;
        this.flag = flag;
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

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {

        final Product product = mProductList.get(position);
        holder.productImage.setVisibility(View.INVISIBLE);
        holder.name.setVisibility(View.INVISIBLE);
        holder.price.setVisibility(View.INVISIBLE);
        String color = product.getProduct_images().get(1).get("color");
        String imageName = product.getProduct_images().get(1).get("image");
        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();

        backgroundGradient.setColor(Color.parseColor(color));

        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        if(product.getDiscount_price() >0){
            holder.discount.setVisibility(View.VISIBLE);
            String sPrice =format.format(product.getProduct_price());
            String discountedPrice =format.format(product.getDiscount_price());

            holder.price.setText(discountedPrice);
            holder.price.setGravity(Gravity.END);
            holder.discount.setPaintFlags(holder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.discount.setText(sPrice);

        }else{
            String sPrice =format.format(product.getProduct_price());
            holder.price.setText(sPrice);

        }
        holder.name.setText(product.getProduct_name());
        loadImage(holder.productImage,imageName,holder.shimmer,holder.name,holder.price);
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(fUser != null){
            getUserWishlist(fUser.getUid(), product, holder.heart);

        }
        holder.heart.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

                boolean condition = holder.heart.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.ic_heart)).getConstantState();

                if(getDeviceName().equals("Samsung"))
                {
                    boolean samsungCont = resource == R.drawable.ic_heart;
                    if(samsungCont ){
                    holder.heart.setImageResource(R.drawable.ic_heart_activated);
                    assert fUser != null;
                    String idUser = fUser.getUid();
                    addProductWishlist(idUser,product);
                    holder.heart.setTag(R.drawable.ic_heart_activated);
                    resource = (Integer) holder.heart.getTag();
                    }else{
                    holder.heart.setImageResource(R.drawable.ic_heart);
                    assert fUser != null;
                    String idUser = fUser.getUid();
                    delProductWishlist(idUser,product.getProduct_id());
                    holder.heart.setTag(R.drawable.ic_heart);

                }
                }else{
                    if (condition)
                    {
                        holder.heart.setImageResource(R.drawable.ic_heart_activated);
                        assert fUser != null;
                        String idUser = fUser.getUid();
                        addProductWishlist(idUser,product);
                        holder.heart.setTag(R.drawable.ic_heart_activated);

                    }else{
                        holder.heart.setImageResource(R.drawable.ic_heart);
                        assert fUser != null;
                        String idUser = fUser.getUid();
                        delProductWishlist(idUser,product.getProduct_id());
                        holder.heart.setTag(R.drawable.ic_heart);

                    }
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
                        heart.setTag(R.drawable.ic_heart_activated);
                    }else{
                        heart.setTag(R.drawable.ic_heart);
                    }
                    resource = (Integer) heart.getTag();

                }
                if(listWishlist.size() <=0){
                    heart.setTag(R.drawable.ic_heart);
                    resource = (Integer) heart.getTag();

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

    private void loadImage(ImageView image, String imageName,ShimmerFrameLayout shimmer,TextView name,TextView price){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imageName);
        try {
            File file = File.createTempFile("tmp",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    BitmapDrawable ob = new BitmapDrawable(bitmap);

                    image.setBackground(ob);
                    if(loading != null){
                        loading.dismissDialog();
                    }
                    shimmer.stopShimmer();
                    shimmer.hideShimmer();
                    shimmer.setVisibility(View.GONE);

                    image.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);

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
        TextView name,price, discount;
        ProductListAdapter.OnProductListener onProductListener;
        ShimmerFrameLayout shimmer;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            shimmer = itemView.findViewById(R.id.shimmer_product);
            discount = itemView.findViewById(R.id.product_tv_discount);
            heart = itemView.findViewById(R.id.product_btn_heart);
            colorCircle = itemView.findViewById(R.id.product_iv_circleColor);
            productImage = itemView.findViewById(R.id.product_iv_product);
            name = itemView.findViewById(R.id.product_tv_name);
            price = itemView.findViewById(R.id.product_tv_price);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            try{
                String id = mProductList.get(position).getProduct_id();
                onProductListener.onProductClick(position,v,id);
            }catch(Exception e){
                Toast.makeText(context,"Product currently unavailable!, Please try close the app and open it again.",Toast.LENGTH_SHORT).show();
            }

        }
    }
    public interface OnProductListener{
        void onProductClick(int position, View view, String id);
    }
}
