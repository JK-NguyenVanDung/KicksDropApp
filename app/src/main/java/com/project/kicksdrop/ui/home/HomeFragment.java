package com.project.kicksdrop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.databinding.FragmentHomeBinding;
import com.project.kicksdrop.model.Product;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ProductListAdapter.OnProductListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ProductListAdapter productAdapter;
    private ArrayList<Product> mProduct;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.homeRvProducts;

        //recycler view
        recyclerView.setHasFixedSize(true);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        getProduct();
        final ImageButton button = binding.homeBtnChat;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                button.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });
        return root;

    }
    private void getProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProduct =new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProduct.clear();
                for(DataSnapshot dtShot: snapshot.getChildren()){
                    Product product = dtShot.getValue(Product.class);
                    mProduct.add(product);
                }

//                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
//                Set<Map.Entry<String, Object> > entrySet = hashMap.entrySet();
//                ArrayList<Map.Entry<String, Object> > listOfEntry = new ArrayList<Map.Entry<String, Object>>(entrySet);
//                int i = 0;
//                Object[] productArray = new Object[listOfEntry.size()];
//                //Product[] objects = ArrayUtils.toObject(productArray);
//
//                for (Object item : listOfEntry){
//                    productArray[i] = item;
//                    mProduct.add((Product) productArray[i]);
//                    i++;
//                }
                productAdapter = new ProductListAdapter(getContext(),mProduct,HomeFragment.this );
                recyclerView.setAdapter(productAdapter);
                //Object temp = productArray[0];
//                HashMap<String,Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
//                Set<Map.Entry<String, Object> > entrySet = hashMap.entrySet();
//                ArrayList<Map.Entry<String, Object> > listOfEntry = new ArrayList<Map.Entry<String, Object>>(entrySet);
//                int i = 0;
//                Object[] productArray = new Object[listOfEntry.size()];
//                for (Object item : listOfEntry){
//                    productArray[i] = item;
//                    i++;
//                }
                //Object temp = productArray[0];
                //Log.d("products",productArray[0].toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onProductClick(int position, View view, String id) {
        Toast.makeText(getContext(),"yeah", Toast.LENGTH_SHORT).show();

    }

}