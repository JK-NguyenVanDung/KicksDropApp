package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
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




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {


        final Product product = mWishlist.get(holder.getAdapterPosition());
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


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, product.getProduct_sizes());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        holder.sizeSpinner.setAdapter(adapter);

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                String idUser = fUser.getUid().toString();
                addProductCart(idUser,product.getProduct_id(),1,"ffffff","40");
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                String idUser = fUser.getUid().toString();
                delProductWishlist(idUser,product.getProduct_id());
            }
        });




//        private void  load(){
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("account/"+idUser+"/wishlist");
//
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    for (DataSnapshot item : dataSnapshot.getChildren()){
//                        item.toString();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w("Load Product", "Failed to read value.", error.toException());
//                }
//            });
//        }


    }
    private void delProductWishlist(String idUser,String idProduct){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+idUser);


        myRef.child(idProduct).removeValue();

    }


    private void addProductCart(String idUser,String idProduct,int amount, String color,String size){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");
        String idColor = color.substring(1);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("productId").setValue(amount);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("amount").setValue(amount);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("color").setValue(color);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("size").setValue(size);
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
    public int getItemCount() {
        return mWishlist ==null? 0: mWishlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avt;
        TextView name,price,type;
        Spinner sizeSpinner;
        Button addCart;
        ImageButton remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avt =(ImageView) itemView.findViewById(R.id.iv_item_productCart_addToCart_image);
            name = itemView.findViewById(R.id.wishlist_tv_productName);
            price = itemView.findViewById(R.id.wishlist_tv_productCost);
            type = itemView.findViewById(R.id.wishlist_tv_productType);
            sizeSpinner = (Spinner) itemView.findViewById(R.id.wishlist_spinner_dropDownSize);
            addCart = itemView.findViewById(R.id.wishList_btn_addToCart);
            remove = itemView.findViewById(R.id.wishlist_ibtn_remove);

        }
    }
}
