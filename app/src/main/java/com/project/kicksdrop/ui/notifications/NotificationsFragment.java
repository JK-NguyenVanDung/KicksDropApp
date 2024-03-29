package com.project.kicksdrop.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.R;
import com.project.kicksdrop.adapter.NotificationsAdapter;
import com.project.kicksdrop.databinding.FragmentNotificationsBinding;
import com.project.kicksdrop.model.Order;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    FirebaseUser fUser;
    RecyclerView recyclerView;
    NotificationsAdapter notificationsAdapter;
    LinearLayout linearLayout, layoutNoAnyThing;
    private  ArrayList<Order> mOrder;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //recycler view
        linearLayout = binding.llNotification;
        recyclerView =binding.rvNotifications;
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        getOrder(fUser.getUid());

        layoutNoAnyThing = binding.layoutNotificationNoAnyThing;



        return root;
    }
    private void getOrder(String user_Id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "order/" + user_Id );
        mOrder = new ArrayList<>();
        DatabaseReference ref = database.getReference( "product" );

        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrder.clear();
                for (DataSnapshot dtShot : snapshot.getChildren()) {
                    Order order = dtShot.getValue( Order.class );
                    assert order != null;
                    if (order.getNotification()){
                        if(order.getStatus().toLowerCase().equals("shipping")){
                            if (order.getOrder_details() != null) {
                                mOrder.add( order );
                            }
                        }
                    }
                }
                if(mOrder.size() == 0){
                    layoutNoAnyThing.setVisibility(View.VISIBLE);
                }else {
                    layoutNoAnyThing.setVisibility(View.GONE);
                }
                notificationsAdapter = new NotificationsAdapter( getContext(), mOrder );
                recyclerView.setAdapter( notificationsAdapter );

                ItemTouchHelper helper = new ItemTouchHelper( callback );
                helper.attachToRecyclerView( recyclerView );
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
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        boolean isDeleting = true;
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (isDeleting){
                Snackbar snackbar = Snackbar.make(linearLayout,"Item DELETED",Snackbar.LENGTH_LONG);
                snackbar.show();
                TextView tv_orderID = viewHolder.itemView.findViewById(R.id.notification_tv_name);
                String orderId = tv_orderID.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("order/"+fUser.getUid()+"/"+orderId);
                myRef.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Boolean> hashMap =(HashMap<String, Boolean>) snapshot.getValue();
                        Boolean check = false;
                                check =hashMap.get("notification");
                        if (check){
                            myRef.child( "notification" ).setValue( false );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mOrder.remove(viewHolder.getAdapterPosition());
                notificationsAdapter.notifyDataSetChanged();
                isDeleting = false;
            }
        }
    };
}