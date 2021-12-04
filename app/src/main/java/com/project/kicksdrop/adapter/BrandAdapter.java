package com.project.kicksdrop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Brand;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.productBrands.ProductBrands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder>{


    Context context;
    ArrayList<Brand> mBrand;

    public BrandAdapter(Context context, ArrayList<Brand> mBrand) {
        this.context = context;
        this.mBrand = mBrand;
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_home_brand, parent, false);
        return new BrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {
        final Brand brand = mBrand.get(holder.getAdapterPosition());
        ImageView img = holder.img;
        loadImage(img,brand.getImage());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductBrands.class);
                intent.putExtra("brand", brand.getName());
                context.startActivity(intent);
            }
        });

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
        return mBrand ==null? 0: mBrand.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.home_ibtn);

        }
    }
}