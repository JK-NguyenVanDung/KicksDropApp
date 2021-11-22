package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;



import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Chat;
import com.project.kicksdrop.model.Image;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorCircleAdapter extends RecyclerView.Adapter<ColorCircleAdapter.ViewHolder>{

    private Context context;

    private static List<String> mColor;
    private static List<Image> mImages;
    private static ViewPager mPager;

    public static String getPickedColor() {
        return pickedColor;
    }

    public static void setPickedColor(String pickedColor) {
        ColorCircleAdapter.pickedColor = pickedColor;
    }

    private static String pickedColor;

    List<ImageView> borders;
    public ColorCircleAdapter(Context context, List<String> mColor, List<Image> images, ViewPager viewPager){
        this.context = context;
        this.mColor = mColor;
        this.mImages = images;
        this.borders = new ArrayList<>();
        this.mPager = viewPager;
    }
    @NonNull
    @Override
    public ColorCircleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_circle_container, parent, false);

        return new ColorCircleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorCircleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();
        String color = mColor.get(position);
        backgroundGradient.setColor(Color.parseColor(color));
        if(position ==0){
            holder.selected.setVisibility(View.VISIBLE);
            setPickedColor(mColor.get(position));
        }
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

                for (int i = 0 ; i< mImages.size(); i ++) {
                    String temp  = mImages.get(i).getColor().toLowerCase();
                    if(temp.equals(color.toLowerCase())){
                        mPager.setCurrentItem(i);

                        break;
                    }
                }
            }
        });
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
            colorCircle = itemView.findViewById(R.id.ibtn_color_circle);
            selected = itemView.findViewById(R.id.ibtn_color_circle_selected);
        }


    }


}
