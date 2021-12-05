package com.project.kicksdrop.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.model.Order;
import com.project.kicksdrop.model.Product;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private List<Order> mOrderList;
    private Context context;
    private static String productName = "";

    public NotificationsAdapter(Context context, List<Order>  mOrderList){
        this.context = context;
        this.mOrderList = mOrderList;
        productName = "";
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Order order = mOrderList.get(holder.getAdapterPosition());


        holder.tv_order.setText(order.getOrder_id().substring(0,8));
        holder.tv_price.setText(order.getOrder_price());
        int dayExpect = Integer.valueOf(order.getOrder_create_date().substring(6,8));
        dayExpect += 7;
        String sDayExpect = order.getOrder_create_date().substring(0,6) + dayExpect + order.getOrder_create_date().substring(8,15);
        holder.tv_expecttedToArrive.setText(sDayExpect);
        getProductName(order.getOrder_details(),holder.tv_productName);

    }

    @Override
    public int getItemCount() {
        return mOrderList ==null? 0: mOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_order, tv_price, tv_expecttedToArrive, tv_productName;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            tv_productName = itemView.findViewById( R.id.notification_tv_nameProduct );
            tv_order = itemView.findViewById( R.id.notification_tv_name);
            tv_price = itemView.findViewById( R.id.notification_tv_price );
            tv_expecttedToArrive = itemView.findViewById(R.id.notification_tv_date);
        }
    }
   private void getProductName(List<HashMap<String,String>> options, TextView tv_order){
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("product");
       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               int i = 1;
               for(HashMap<String, String> item : options){
                   for(DataSnapshot dtShot: snapshot.getChildren()){
                       Product product = dtShot.getValue(Product.class);
                       assert product != null;
                       product.getProduct_colors().remove(0);
                       String productId = dtShot.getKey();
                       if(productId.equals(item.get("productId"))){
                           HashMap<String,Object> hashMap = (HashMap<String, Object>) dtShot.getValue();
                            if (productName.equals("")){
                                productName += i + ". " + hashMap.get("product_name") +
                                        " x " + item.get("amount");
                                i++;
                            }else{
                                productName += "\n" + i + ". " + hashMap.get("product_name") +
                                        " x " + item.get("amount");
                                i++;
                            }

                            tv_order.setText( productName );
                       }
                   }
               }
               productName ="";
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }
}
