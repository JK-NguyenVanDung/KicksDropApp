package com.project.kicksdrop;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.kicksdrop.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth auth;

    String user_id = "AC1";
    HashMap<String,Object> hashMap;
    String coupon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        String username = "jackiedekingv@gmail.com";
        String pass = "123456";

        auth.signInWithEmailAndPassword(username,pass).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_wishlist, R.id.navigation_notifications,R.id.navigation_profile)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navCo = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navCo);

        //loadProduct("PD1");


        //addProductCart("AC3","PD1",5,"#333",42);
        //addProductCart("AC3","PD2",5,"#333",42);
        //delProductCart("AC3","PD1");

        //
        //getCart(user_id);
        //getProduct();


    }

    private void getCart(String user_Id){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("cart");
            String i_id = user_Id;


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot item : snapshot.getChildren()){
//                        Log.d("data",item.getValue().get(i_id).toString());
//                    }
                    HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    HashMap<String,Object> list =(HashMap<String, Object>) hashMap.get(i_id);
                    HashMap<String,Object> listProduct =  (HashMap<String, Object>) list.get("product");
                    String coupon_id = list.get("coupon_id").toString();
                    coupon = coupon_id;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


//        myRef.child(String.valueOf(i_id)).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try {
//                    HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
//                    Log.d("test", hashMap.toString());
//                } catch (Exception e){
//                    Log.d("Loi_js", e.toString());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    private void createAccount(EditText user, EditText password, TextView err, FirebaseAuth auth){
        String semail = user.getText().toString().trim();
        String spassword = password.getText().toString().trim();
        if (TextUtils.isEmpty(semail)){
            err.setText("Please Enter Email");
            return;
        }
        if (TextUtils.isEmpty(spassword)){
            err.setText("Please Enter Password");
            return;
        }
        if (spassword.length() <= 6){
            err.setText("Please Enter Password more than 6 char");
            return;
        }
        Activity RegisterActivity = new Activity();// lúc add thì xóa dòng này và thay RegisterActivity bên dưới thành RegisterActivity.thís
        auth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(RegisterActivity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            err.setText(task.getException().getMessage().toString());
                        } else {
                            Toast.makeText(getApplicationContext(),"Register Susscess", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity, MainActivity.class));
                            finish();
                        }

                    };

                });
    }
    private void getProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot item : snapshot.getChildren()){
//                        Log.d("data",item.getValue().get(i_id).toString());
//                    }
                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                Set<Map.Entry<String, Object> > entrySet = hashMap.entrySet();
                ArrayList<Map.Entry<String, Object> > listOfEntry = new ArrayList<Map.Entry<String, Object>>(entrySet);
                int i = 0;
                Object[] productArray = new Object[listOfEntry.size()];
                for (Object item : listOfEntry){
                    productArray[i] = item;
                    i++;
                }
                Object temp = productArray[0];
                Log.d("products",productArray[0].toString());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadImage(ImageView image, String imageName){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imageName);
        try {
            File file = File.createTempFile("tmp",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void loadProduct(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product/"+id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    item.toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Load Product", "Failed to read value.", error.toException());
            }
        });
    }


    private void addCart(String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");

        myRef.child(idUser).child("coupon_id").setValue("");
        myRef.child(idUser).child("product").setValue("");


    }


    private void addProductCart(String idUser,String idProduct,int amount, String color,int size){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");


        myRef.child(idUser).child("product").child(idProduct).child("amount").setValue(amount);
        myRef.child(idUser).child("product").child(idProduct).child("color").setValue(color);
        myRef.child(idUser).child("product").child(idProduct).child("size").setValue(size);


    }

    private void delProductCart(String idUser,String idProduct){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");


        myRef.child(idUser).child("product").child(idProduct).removeValue();


    }

    private void editProductCart(String idUser,String idProduct){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart");


        myRef.child(idUser).child("product").child(idProduct).removeValue();


    }

    private void addCoupon(String cp_id, String code, String duration, String max_price, String name, String percent, String time){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");

        myRef.child(cp_id).child("coupon_code").setValue(code);
        myRef.child(cp_id).child("coupon_duration").setValue(duration);
        myRef.child(cp_id).child("coupon_max_price").setValue(max_price);
        myRef.child(cp_id).child("coupon_name").setValue(name);
        myRef.child(cp_id).child("coupon_percent").setValue(percent);
        myRef.child(cp_id).child("coupon_time").setValue(time);

    }
    private void addOrder(String cp_id, String date, String details, String price, String user_id, String order_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order");


        myRef.child(order_id).child("coupon_id").setValue(cp_id);
        myRef.child(order_id).child("coupon_create_date").setValue(date);
        myRef.child(order_id).child("coupon_details").setValue(details);
        myRef.child(order_id).child("coupon_price").setValue(price);
        myRef.child(order_id).child("user_id").setValue(user_id);

    }
    private void  loadCoupon(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    item.toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Load Product", "Failed to read value.", error.toException());
            }
        });
    }

    private void delCoupon(String coupon_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");


        myRef.child(coupon_id).removeValue();


    }


    private void  loadOrder(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("order");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    item.toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Load Product", "Failed to read value.", error.toException());
            }
        });
    }



}