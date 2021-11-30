package com.project.kicksdrop.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.project.kicksdrop.model.Cart;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.cart.CartListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    private Cart cart;
    private List<Product> mCartProduct;
    private List<Coupon> mCoupon;
    private List<HashMap<String,String>> productOptions;
    private Coupon coupon;
    //private long currentAmount = 1;
    private TextView totalPayment,totalProduct,totalPaymentHead;

    public static double getTotalAmount() {
        return totalAmount;
    }

    public static void setTotalAmount(double totalAmount) {
        CartAdapter.totalAmount = totalAmount;
    }

    private static double totalAmount = 0.0;
    private int totalProducts = 0;
    private String coupon_id;
    private int maxprice = 0;
    private int percent = 0;
    private LoadingScreen loading;
    public CartAdapter(Context context, List<Product> mCartProduct, List<HashMap<String,String>> productOptions, TextView totalPayment, TextView totalProduct, TextView totalPaymentHead, String coupon_id, LoadingScreen loading) {
        this.context = context;
        //this.cart = cart;
        this.mCartProduct = mCartProduct;
        this.productOptions = productOptions;
        this.totalPayment = totalPayment;
        this.totalProduct = totalProduct;
        this.totalPaymentHead = totalPaymentHead;
        this.coupon_id = coupon_id;
        totalAmount = 0.0;
        this.loading = loading;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        return new CartAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Product product = mCartProduct.get(holder.getAdapterPosition());

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getUserWishlist(fUser.getUid(), product, holder.heart);
        String opColor = "#ffffff";
        if(productOptions.get(holder.getAdapterPosition()).get("color") != null){
            opColor = productOptions.get(holder.getAdapterPosition()).get("color").toLowerCase();
        }

        GradientDrawable backgroundGradient = (GradientDrawable)holder.colorCircle.getBackground();

        backgroundGradient.setColor(Color.parseColor(opColor));

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

        for(HashMap<String,String> temp: product.getProduct_images()){
            String lower = temp.get("color").toLowerCase();
            if(lower.equals(opColor)){
                String imageName = temp.get("image");
                loadImage(holder.productImage,imageName);
            }
        }
        totalProducts= productOptions.size();
        totalProduct.setText(totalProducts > 1 ? (totalProducts + " ITEMS"): totalProducts + " ITEM");
        holder.productCartName.setText(product.getProduct_name());
        holder.productCartType.setText(product.getProduct_brand());

        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(product.getProduct_price());
        holder.productCartPrice.setText(sPrice);

        String amountS = String.valueOf( productOptions.get(position).get("amount"));
        final long[] currentAmount = {Long.parseLong(amountS)};
        holder.productCartAmount.setText(Long.toString(currentAmount[0]));
        //Toast.makeText(context,"yes",Toast.LENGTH_LONG).show();
        product.getProduct_sizes().remove(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, product.getProduct_sizes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(adapter);

        holder.dropDown.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                holder.sizeSpinner.performClick();
            }
        });
        for(int i = 0 ; i < product.getProduct_sizes().size(); i ++){
            String size = productOptions.get(holder.getAdapterPosition()).get("size");
            if(product.getProduct_sizes().get(i).equals(size)){
                holder.sizeSpinner.setSelection(i);

            }

        }
        holder.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveToCart(productOptions.get(holder.getAdapterPosition()).get("cartProductID"), currentAmount[0],parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

       holder.delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new AlertDialog.Builder(context)
                       .setTitle("Warning")
                       .setMessage("Do you want to delete this?")
                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               deleteFromCart(productOptions.get(holder.getAdapterPosition()).get("cartProductID"),holder.getAdapterPosition());
                           }
                       })
                       .setNegativeButton(android.R.string.no, null)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .show();

               //deleteFromCart(productOptions.get(holder.getAdapterPosition()).get("cartProductID"),holder.getAdapterPosition());



           }
       });

        holder.increase.setOnClickListener(new  View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(Long.parseLong(amountS) < product.getProduct_quantity()){
                    currentAmount[0]++;
                    holder.productCartAmount.setText(Long.toString(currentAmount[0]));
                    saveToCart(productOptions.get(position).get("cartProductID"), currentAmount[0], holder.sizeSpinner.getSelectedItem().toString());
                }else{
                    Toast.makeText(context,"Reach maximum available product",Toast.LENGTH_SHORT).show();

                }

            }
        });
        holder.decrease.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentAmount[0] >1){
                    currentAmount[0]--;
                    holder.productCartAmount.setText(Long.toString(currentAmount[0]));
                    saveToCart(productOptions.get(position).get("cartProductID"), currentAmount[0], holder.sizeSpinner.getSelectedItem().toString());

                }else{
                    Toast.makeText(context,"Minimum amount is 1",Toast.LENGTH_SHORT).show();
                }
            }
        });


        calculateTotal(product.getProduct_price(), currentAmount[0]);

//
//        if (!coupon_id.equals("")){
//            if(totalAmount != 0){
//                getCoupon(coupon_id);
//            }
//
//        }


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
    @SuppressLint("SetTextI18n")
    private void calculateTotal(double price, long amount){
        totalAmount += price * amount;
        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(totalAmount);
        setTotalAmount(totalAmount);
        totalPayment.setText(sPrice);
        totalPaymentHead.setText(sPrice);
    }
    private void calculateTotalCoupon(int percent, int maxprice){
        double discount = (totalAmount * percent) / 100;
        if(discount > maxprice){
            totalAmount = totalAmount - (maxprice/5);
        }else {
            totalAmount = totalAmount - discount;
        }
        java.util.Currency usd = java.util.Currency.getInstance("USD");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        format.setCurrency(usd);
        String sPrice =format.format(totalAmount);
        totalPayment.setText(sPrice);
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
                    loading.dismissDialog();

                    image.setBackground(ob);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void saveToCart(String cartProductId, Long amount, String size ){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+ "/product/" + cartProductId);
        myRef.child("amount").setValue(amount);
        myRef.child("size").setValue(size);

    }
    @SuppressLint("SetTextI18n")
    private void deleteFromCart(String cartProductId, int position ){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+ "/product/" + cartProductId);
        myRef.removeValue();
        if(!mCartProduct.isEmpty()&& mCartProduct.size() > position) {
            mCartProduct.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
        if(mCartProduct.isEmpty()){
            mCartProduct.clear();
            totalPayment.setText("$00.00");
            totalProduct.setText("0 ITEM");
            totalPaymentHead.setText("$00.00");
        }
    }
    @Override
    public int getItemCount() {
        return mCartProduct ==null? 0: mCartProduct.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productCartName, productCartType, productCartAmount, productCartPrice;
        ImageButton increase, decrease, delete, dropDown;
        //Spinner productCartDropDownSize;
        ImageButton heart;
        Spinner sizeSpinner;
        ImageView productImage, colorCircle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productCartName = (TextView) itemView.findViewById(R.id.ProductCart_tv_name);
            productCartType = (TextView) itemView.findViewById(R.id.ProductCart_tv_category);
            productCartPrice = itemView.findViewById(R.id.ProductCart_tv_price);
            sizeSpinner = (Spinner) itemView.findViewById(R.id.itemProductCart_sp_listViewDropDownSize);
            productCartAmount =  itemView.findViewById(R.id.ProductCart_tv_amount_numb);
            increase = itemView.findViewById(R.id.ProductCart_btn_increase);
            decrease = itemView.findViewById(R.id.ProductCart_btn_decrease);
            delete = (ImageButton) itemView.findViewById(R.id.ProductCart_delete);
            productImage = itemView.findViewById(R.id.productCart_iv_cart_image);
            dropDown = itemView.findViewById(R.id.ProductCart_btn_drop_down);
            colorCircle= itemView.findViewById(R.id.productCart_iv_color_circle);
            heart = itemView.findViewById(R.id.productCart_btn_heart);
        }

    }
//    private void getCoupon(String coupon_id){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("coupon");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dtShot: snapshot.getChildren()){
//
//                    Coupon temp = dtShot.getValue(Coupon.class);
//                    assert temp != null;
//                    temp.setCoupon_id(dtShot.getKey());
//                    if(coupon_id.equals(dtShot.getKey())){
//                        coupon = temp;
//                        break;
//                    }
//                }
//                maxprice = Integer.parseInt(coupon.getCoupon_max_price());
//                percent = Integer.parseInt(coupon.getCoupon_percent());
////
////                if(totalAmount < maxprice){
////                    double discount = totalAmount * ((double) percent/100);
////                    totalAmount -= discount;
////                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}
