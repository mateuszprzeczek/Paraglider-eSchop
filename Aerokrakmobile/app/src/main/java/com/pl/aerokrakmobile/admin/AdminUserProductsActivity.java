package com.pl.aerokrakmobile.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.model.Cart;
import com.pl.aerokrakmobile.viewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity
{
    private RecyclerView adminProductsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    public static final String USER_ID = "extra.UserId";




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        String userId = getIntent().getStringExtra(USER_ID);

        adminProductsList = findViewById(R.id.recycler_admin_products_orders_list);
        adminProductsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adminProductsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View")
                .child(userId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NotNull CartViewHolder holder, int i, @NotNull Cart cart)
            {
                holder.txtProductName.setText("Nazwa " + cart.getProduct_name());
                holder.txtProductQuantity.setText("Ilość - " + cart.getQuantity());
                holder.txtProductPrice.setText("Cena " + cart.getPrice() + " zł");
            }

            @NotNull
            @Override
            public CartViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout
                        , parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        adminProductsList.setAdapter(adapter);
        adapter.startListening();
    }
}
