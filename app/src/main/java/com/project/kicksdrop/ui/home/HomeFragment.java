package com.project.kicksdrop.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.ChatActivity;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.adapter.HomeCouponAdapter;
import com.project.kicksdrop.adapter.ProductListAdapter;
import com.project.kicksdrop.databinding.FragmentHomeBinding;
import com.project.kicksdrop.model.Coupon;
import com.project.kicksdrop.model.Product;
import com.project.kicksdrop.ui.auth.LoginActivity;
import com.project.kicksdrop.ui.cart.CartListView;
import com.project.kicksdrop.ui.product.ProductDetail;
import com.project.kicksdrop.ui.productBrands.ProductBrands;
import com.project.kicksdrop.ui.searchView.SearchViewProduct;

import java.util.ArrayList;
import java.util.Objects;

public class    HomeFragment extends Fragment implements ProductListAdapter.OnProductListener,HomeCouponAdapter.OnCouponListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ProductListAdapter productAdapter;
    HomeCouponAdapter homeCouponAdapter;
    private ArrayList<Product> mProduct;
    private ArrayList<Coupon> mCoupon;
    private TextView tvNumberCart;
    private int numberCart;
    ArrayList<Product> sProduct;
    RecyclerView recyclerView;
    RecyclerView CouponRecyclerView;
    FirebaseUser fUser;


    //    ImageButton productContentIbtn, newDropsIBtn, nikesIbtn, adidasIBtn;
//    Button productTitleBtn;
    private LoadingScreen loading = new LoadingScreen(HomeFragment.this);

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        if(fUser == null || GoogleSignIn.getLastSignedInAccount(requireContext()) == null){
            Intent login = new Intent(getActivity(), LoginActivity.class);

            if(this.getArguments() !=null && this.getArguments().getString("id") != null){
                String id = this.getArguments().getString("id");
                login.putExtra("id", id);
            }else{
                login.putExtra("register", true);
            }

            startActivity(login);
        }else{
            DatabaseReference ref = database.getReference("cart/"+fUser.getUid() + "/product");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getKey() != null) {
                        Long numberCart = snapshot.getChildrenCount();
                        if(tvNumberCart!= null){
                            tvNumberCart.setText(String.valueOf(numberCart));
                        }

                    } else {
                        loading.dismissDialog();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        loading = new LoadingScreen(HomeFragment.this);
        loading.startLoadingScreenFragment();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        tvNumberCart = binding.tvNumberCartHome;

        View root = binding.getRoot();
        recyclerView = binding.homeRvProducts;
        //recycler view
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        CouponRecyclerView = binding.homeRvCoupon;
        CouponRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this.getContext(), 4, GridLayoutManager.VERTICAL, false);
        CouponRecyclerView.setLayoutManager(manager);


        getProduct();
        getCoupon();
        final ImageButton nikesIbtn = binding.homeIbtnNikes;
        nikesIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductBrands.class);
                intent.putExtra("brand", "Nike");
                startActivity(intent);
            }
        });

        final ImageButton adidasIbtn = binding.homeIbtnAdidas;
        adidasIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductBrands.class);
                intent.putExtra("brand", "Adidas");
                startActivity(intent);
            }
        });

        final ImageButton vansIbtn = binding.homeIbtnVans;
        vansIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductBrands.class);
                intent.putExtra("brand", "Vans");
                startActivity(intent);
            }
        });

        final ImageButton slide = binding.homeIbtnProductContent;
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchViewProduct.class);
                intent.putExtra("keySearch", "");
                startActivity(intent);
            }
        });
        final ImageButton slide2 = binding.homeIbtnProductContent2;
        slide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchViewProduct.class);
                intent.putExtra("keySearch", "");
                startActivity(intent);
            }
        });
        final ImageButton chat = binding.homeBtnChat;
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        final ImageButton cart = binding.homeBtnCart;
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartListView.class);
                startActivity(intent);
            }
        });

        final AutoCompleteTextView search = binding.homeEtSearch;
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);

        //connect
        DatabaseReference myRef = database.getReference("product");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;
                    product.setProduct_id(dtShot.getKey());
                    adapter.add(product.getProduct_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.setAdapter(adapter);

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SearchViewProduct.class);
                intent.putExtra("keySearch", search.getText().toString());
                startActivity(intent);
                search.setText("");

            }
        });
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50) && !search.getText().toString().matches("")) {

                        Intent intent = new Intent(getContext(), SearchViewProduct.class);
                        intent.putExtra("keySearch", search.getText().toString());
                        startActivity(intent);

                        return true;

                    }
                }
                return false;
            }
        });

//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable edit) {
//                if (edit.length() != 0) {
//                    String keySearch = search.getText().toString();
//                    searchProduct(keySearch);
//                    Log.v("keySearch",keySearch);
//                }
//            }
//        });


        return root;

    }

    @Override
    public void onProductClick(int position, View view, String id) {
        Intent intent = new Intent(getContext(), ProductDetail.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    private void searchProduct(String keySearch) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        sProduct = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sProduct.clear();
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;

                    if (product.getProduct_name().toLowerCase().contains(keySearch.toLowerCase())) {
                        product.setProduct_id(dtShot.getKey());
                        sProduct.add(product);
                    }
                }
//                productAdapter = new ProductListAdapter(getContext(),mProduct, SearchViewProduct.this);
//                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");
        mProduct = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProduct.clear();
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Product product = dtShot.getValue(Product.class);
                    assert product != null;
                    product.setProduct_id(dtShot.getKey());
                    mProduct.add(product);
                }
                productAdapter = new ProductListAdapter(getContext(), mProduct, HomeFragment.this, loading);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCoupon() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("coupon");
        mCoupon = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCoupon.clear();
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Coupon coupon = dtShot.getValue(Coupon.class);
                    assert coupon != null;
                    coupon.setCoupon_id(dtShot.getKey());
                    mCoupon.add(coupon);
                }

                mCoupon.size();
                homeCouponAdapter = new HomeCouponAdapter(getContext(), mCoupon, HomeFragment.this);
                CouponRecyclerView.setAdapter(homeCouponAdapter);

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
    public void onCouponClick(int position, View view, String id) {

    }
}