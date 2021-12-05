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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.project.kicksdrop.model.Cart;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {
    private Context context;
    private Cart cart;
    private List<Product> mCartProduct;
    private java.util.List<Coupon> mCoupon;
    private List<HashMap<String,String>> productOptions;
    private Spinner sizeSpinner;
    private Coupon coupon;
    //private long currentAmount = 1;
    private TextView totalPayment,totalProduct;
    public static double getTotalAmount() {
        return totalAmount;
    }

    public static void setTotalAmount(double totalAmount) {
        OrderProductAdapter.totalAmount = totalAmount;
    }
    private int totalProducts = 0;
    private String coupon_id;
    private int maxprice = 0;
    private int percent = 0;
    LoadingScreen loading;
    private static double totalAmount = 0.0;
    public OrderProductAdapter(Context context, List<Product> mCartProduct, List<HashMap<String,String>> productOptions, LoadingScreen loading) {
        this.context = context;
        //this.cart = cart;
        this.mCartProduct = mCartProduct;
        this.productOptions = productOptions;
        this.totalProduct = totalProduct;
        totalAmount = 0.0;
        this.coupon_id = coupon_id;
        this.loading = loading;
    }
    public OrderProductAdapter(Context context, List<Product> mCartProduct, List<HashMap<String,String>> productOptions) {
        this.context = context;
        //this.cart = cart;
        this.mCartProduct = mCartProduct;
        this.productOptions = productOptions;
        this.totalProduct = totalProduct;

        this.coupon_id = coupon_id;
    }




    @SuppressLint("SetTextI18n")
    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order, parent, false);

        return new OrderProductAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.ViewHolder holder, int position) {
        final Product product = mCartProduct.get(holder.getAdapterPosition());



            String opColor = productOptions.get(holder.getAdapterPosition()).get("color").toLowerCase();
            for(HashMap<String,String> temp: product.getProduct_images()){
                String lower = temp.get("color").toLowerCase();
                if(lower.equals(opColor)){
                    String imageName = temp.get("image");
                    loadImage(holder.productImage,imageName);
                }
            }
            GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();
    
            backgroundGradient.setColor(Color.parseColor(opColor));

            java.util.Currency usd = java.util.Currency.getInstance("USD");
            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
            format.setCurrency(usd);
            String sPrice =format.format(product.getProduct_price());

            //
            holder.productCartName.setText(product.getProduct_name());
            holder.productCartPrice.setText(sPrice);
            holder.productCartAmount.setText(product.getAmount());
            holder.getProductCartSize.setText(product.getProduct_size());


    }
    @SuppressLint("NotifyDataSetChanged")
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productCartName,  productCartPrice, productCartAmount, getProductCartSize;

        ImageButton makeOrder, decrease, delete;
        //Spinner productCartDropDownSize;
        ImageView productImage, colorCircle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            getProductCartSize = itemView.findViewById(R.id.order_tv_size);
            productImage = itemView.findViewById(R.id.order_tv_image);
            productCartName = itemView.findViewById(R.id.order_tv_name);
            productCartAmount  = itemView.findViewById(R.id.order_tv_amount);
            productCartPrice = itemView.findViewById(R.id.order_tv_price);
            colorCircle = itemView.findViewById(R.id.order_iv_circle_color);
        }

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
                    if(loading != null){
                        loading.dismissDialog();

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mCartProduct ==null? 0: mCartProduct.size();
    }
}
