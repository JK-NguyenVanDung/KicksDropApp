package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListColorCircleAdapter extends RecyclerView.Adapter<WishListColorCircleAdapter.ViewHolder>{
    private Context context;

    private  List<String> mColor;
    private  List<Image> mImages;
    private static String pickedColor;
    private  List<ImageView> img;
    public  static String getPickedColor() {
        return pickedColor;
    }

    public static void setPickedColor(String pickedColor) {
        WishListColorCircleAdapter.pickedColor = pickedColor;
    }
    private String product_id;

    List<ImageView> borders;
    List<HashMap<String,String>> options;

    public WishListColorCircleAdapter(Context context, List<String> mColor, List<Image> images, ImageView img, String product_id,List<HashMap<String,String>> options){
        this.context = context;
        this.mColor = mColor;
        this.mImages = images;
        this.borders = new ArrayList<>();
        this.img = new ArrayList<>();
        if(this.img != null){
            this.img.add(img);
        }
        this.product_id = product_id;
        this.options = options;
    }

    @NonNull
    @Override
    public WishListColorCircleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_small_circle_container, parent, false);

        return new WishListColorCircleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListColorCircleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();
        String color = mColor.get(position);
        backgroundGradient.setColor(Color.parseColor(color));
//        if(position ==0){
//            //holder.selected.setVisibility(View.VISIBLE);
//            setPickedColor(mColor.get(0));
//        }

        if(holder.selected != null){
            ImageView iv = holder.selected;
            borders.add(iv);
        }
        holder.colorCircle.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for(ImageView img: borders){
                    if(img != holder.selected){
                        img.setVisibility(View.INVISIBLE);
                    }else{
                        holder.selected.setVisibility(View.VISIBLE);
                        setPickedColor(mColor.get(holder.getAdapterPosition()));
                    }
                }
                setPickedColor(mColor.get(position));
                for (int i = 0 ; i< mImages.size(); i ++) {
                    String temp  = mImages.get(i).getColor().toLowerCase();
                    if(temp.equals(color.toLowerCase())){
                        loadImage(img.get(0),mImages.get(i).getImage());
                        saveEdit(product_id,getPickedColor());
                        break;
                    }
                }
            }
        });
    }
    private void saveEdit(String productId,String color){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("wishlist/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() + "/"+ productId);
        myRef.child("product_color").setValue(color);

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
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return mColor ==null? 0: mColor.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton colorCircle;
        ImageView selected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCircle = itemView.findViewById(R.id.ibtn_small_circle);
            selected = itemView.findViewById(R.id.ibtn_small_circle_border);
        }


    }
}
