package com.pl.aerokrakmobile.sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.buyer.MainActivity;
import com.pl.aerokrakmobile.buyer.ProductDetailsActivity;
import com.pl.aerokrakmobile.model.Products;
import com.pl.aerokrakmobile.viewHolder.SellerItemViewHolder;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {
    private TextView mTextMessage;
    BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            Intent intent = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        case R.id.navigation_add:
                            Intent intent1 = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                            startActivity(intent1);
                            finish();
                            return true;
                        case R.id.navigation_logout:
                            FirebaseAuth fAuth = FirebaseAuth.getInstance();
                            fAuth.signOut();
                            Intent logoutIntent = new Intent(SellerHomeActivity.this, MainActivity.class);
                            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logoutIntent);
                            finish();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        recyclerView = findViewById(R.id.recycler_seller_home);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductsRef.orderByChild("sellerId")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, SellerItemViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NotNull SellerItemViewHolder productViewHolder, int i, @NotNull final Products products)
                    {
                        productViewHolder.txtProductName.setText(products.getProductname());
                      //  productViewHolder.txtProductDescription.setText(products.getDescription());
                        productViewHolder.productPrice.setText("Cena " + products.getPrice() + "zł");
                        productViewHolder.productState.setText("State " + products.getProductState());

                        if (products.getProductState().equals("Not Approved"))
                        {
                            productViewHolder.productState.setTextColor(getColor(R.color.red));
                        }

                        Picasso.get().load(products.getImage()).into(productViewHolder.image);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                final String productId = products.getProductid();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit" ,
                                                "Delete"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        SellerHomeActivity.this);
                                builder.setTitle("Do You want to Delete this Product?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which == 0)
                                        {
                                            Intent intent = new Intent(SellerHomeActivity.this, SellerMaintainProductsActivity.class);
                                            intent.putExtra(ProductDetailsActivity.PRODUCT_ID, products.getProductid());
                                            startActivity(intent);
                                        }
                                        if (which == 1)
                                        {
                                            deleteProduct(productId);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NotNull
                    @Override
                    public SellerItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.seller_item_view, parent, false);
                        return new SellerItemViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productId)
    {
        unverifiedProductsRef.child(productId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NotNull Task<Void> task)
            {
                Toast.makeText(SellerHomeActivity.this,
                        "Product has been Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
