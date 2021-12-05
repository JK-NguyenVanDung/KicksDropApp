package com.project.kicksdrop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Banner;
import com.project.kicksdrop.model.Brand;
import com.project.kicksdrop.ui.searchView.SearchViewProduct;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    Context context;
    ArrayList<Banner> mBanner;
    private LoadingScreen loading;

    public BannerAdapter(Context context, ArrayList<Banner> mBanner) {
        this.context = context;
        this.mBanner = mBanner;
        this.loading = loading;
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);

        return new BannerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Banner banner = mBanner.get(holder.getAdapterPosition());
        ImageButton image = holder.ImagesBanner;
        Button Text = holder.TextBanner;
        Text.setText(banner.getBannertitle());
        loadImage(image,banner.getBannerimages());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchViewProduct.class);
                intent.putExtra("keySearch", "");
                context.startActivity(intent);
            }
        });

        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchViewProduct.class);
                intent.putExtra("keySearch", "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
            return mBanner ==null? 0: mBanner.size();
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
                    if(loading != null){
                        loading.dismissDialog();

                    }
                    image.setBackground(ob);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutBanner;
        ImageButton ImagesBanner;
        Button TextBanner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutBanner = (LinearLayout) itemView.findViewById(R.id.lLayoutBanner);
            ImagesBanner = (ImageButton) itemView.findViewById(R.id.IButton_Banner);
            TextBanner = (Button) itemView.findViewById(R.id.btnTextBanner);
        }
    }
}
