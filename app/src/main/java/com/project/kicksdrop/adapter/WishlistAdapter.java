package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MessagePopUp;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Image;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{

    private Context context;
    private  List<Product> mWishlist;
    private Spinner sizeSpinner;
    private List<HashMap<String,String>> wishlistOptions;
    private int totalCount = 0;
    private TextView totalProducts;
    private LoadingScreen loading;
    public WishlistAdapter(Context context, List<Product> mWishlist, List<HashMap<String,String>> wishlistOptions, TextView totalProducts, LoadingScreen loading) {
        this.context = context;
        this.mWishlist = mWishlist;
        this.wishlistOptions = wishlistOptions;
        this.totalProducts = totalProducts;
        this.loading = loading;
    }



    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistAdapter.ViewHolder(view);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {


        final Product product = mWishlist.get(holder.getAdapterPosition());
        String opColor = "";
        if(wishlistOptions.get(holder.getAdapterPosition()).get("product_color") != null){
            opColor = Objects.requireNonNull(wishlistOptions.get(holder.getAdapterPosition()).get("product_color")).toLowerCase();
        }
        product.getProduct_images().remove(0);
        for(HashMap<String,String> temp: product.getProduct_images()){
            String lower = temp.get("color").toLowerCase();
            if(lower.equals(opColor)){
                String imageName = temp.get("image");
                loadImage(holder.image,imageName);
            }
        }
        totalCount= wishlistOptions.size();
        totalProducts.setText(totalCount > 1 ? totalCount + " ITEMS": totalCount + " ITEM");

        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(product.getProduct_price());
        holder.price.setText(sPrice);

        holder.name.setText(product.getProduct_name());

        holder.brand.setText(product.getProduct_brand());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, product.getProduct_sizes());

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

                assert fUser != null;
                String idUser = fUser.getUid();
                String color = Objects.requireNonNull(wishlistOptions.get(holder.getAdapterPosition()).get("product_color"));
                String size = Objects.requireNonNull(wishlistOptions.get(holder.getAdapterPosition()).get("product_size"));
                addProductCart(idUser,product.getProduct_id(),1,color ,size);
                delProductWishlist(idUser,product.getProduct_id(), holder.getAdapterPosition());
                MessagePopUp messagePopUp = new MessagePopUp();
                messagePopUp.show(context,"Thêm Thành Công");
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Cảnh Báo")
                        .setMessage("Bạn Có Muốn Xóa Không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                String idUser = fUser.getUid();
                                delProductWishlist(idUser,product.getProduct_id(),holder.getAdapterPosition());

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                }
        });
        LinearLayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        holder.mCirclesRecyclerView.setLayoutManager(layoutManager);
        WishListColorCircleAdapter circleAdapter;


        ArrayList<String> colors = product.getProduct_colors();
        colors.remove(0);

        //product.getProduct_images().remove(0);

        int size =product.getProduct_images().size();
        int count = 0;
        List<Image> images = new ArrayList<>(size);
        while (count < size){
            String tempImg = product.getProduct_images().get(count).get("image");
            String tempColor = product.getProduct_images().get(count).get("color");
            Image temp = new Image(tempImg,tempColor);
            images.add(temp);
            count++;
        }
        circleAdapter = new WishListColorCircleAdapter(context,colors,images,holder.image,product.getProduct_id(),wishlistOptions);
        holder.mCirclesRecyclerView.setAdapter(circleAdapter);


        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, product.getProduct_sizes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        product.getProduct_sizes().remove(0);
        for(int i = 0 ; i < product.getProduct_sizes().size(); i ++){
            String temp = wishlistOptions.get(holder.getAdapterPosition()).get("product_size");
            if(product.getProduct_sizes().get(i).equals(temp)){
                sizeSpinner.setSelection(i);
            }
        }
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveEdit(product.getProduct_id() ,parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        holder.dropDown.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                sizeSpinner.performClick();
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
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void delProductWishlist(String idUser, String idProduct, int position){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+idUser);
        myRef.child(idProduct).removeValue();
        if(!mWishlist.isEmpty()&& mWishlist.size() > position) {
            mWishlist.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
        if(mWishlist.isEmpty()){
            mWishlist.clear();
            totalProducts.setText("0 ITEM");
        }
    }

    private void saveEdit(String productId, String size ){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() + "/"+ productId);
        myRef.child("product_size").setValue(size);

    }

    private void addProductCart(String idUser,String idProduct,int amount, String color,String size){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");
        String idColor = color.substring(1);
        Log.d("SOMETHING",size);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("productId").setValue(amount);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("amount").setValue(amount);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("color").setValue(color);
        myRef.child(idUser).child("product").child(idProduct+idColor).child("size").setValue(size);
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

                    image.setBackground(ob);
                    loading.dismissDialog();

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
        RecyclerView mCirclesRecyclerView;
        ImageView image;
        TextView name,price,brand;
        Button addCart;
        ImageButton remove,dropDown;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image =(ImageView) itemView.findViewById(R.id.wishlist_iv_product_img);
            name = itemView.findViewById(R.id.wishlist_tv_productName);
            price = itemView.findViewById(R.id.wishlist_tv_productCost);
            brand = itemView.findViewById(R.id.wishlist_tv_brand);
            sizeSpinner = (Spinner) itemView.findViewById(R.id.wishlist_spinner_dropDownSize);
            addCart = itemView.findViewById(R.id.wishList_btn_addToCart);
            remove = itemView.findViewById(R.id.wishlist_ibtn_remove);
            mCirclesRecyclerView= itemView.findViewById(R.id.wishlist_rv_circles);
            dropDown = itemView.findViewById(R.id.wishlist_btn_dropDown);
        }
    }
}
