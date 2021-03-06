package com.pl.aerokrakmobile.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.model.Products;
import com.pl.aerokrakmobile.prevalent.Prevalent;
import com.pl.aerokrakmobile.sellers.SellerMaintainProductsActivity;
import com.pl.aerokrakmobile.viewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements
                NavigationView.OnNavigationItemSelectedListener {

    public static final String ADMIN_EDIT = "extra.AdminEdit";
    public static final String CATEGORY_NAME = "extra.Category";
    String type = "";


    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();






        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        if (!type.equals("Admin"))
        {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                    drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            });
        }


        Paper.init(this);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        if (!type.equals("Admin"))
        {

        }



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);

        if (type.equals(""))
        {
            userNameTextView.setText(Prevalent.currentOnlineUser.getProfileName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        if (bundle != null)
        {
            type = getIntent().getExtras().get(CATEGORY_NAME).toString();


            FirebaseRecyclerOptions<Products> options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(productsRef
                                    .orderByChild("category").equalTo(type), Products.class)
                            .build();
            adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
            {
                @Override
                protected void onBindViewHolder(@NotNull ProductViewHolder productViewHolder, int i, @NotNull final Products products) {
                    productViewHolder.txtProductName.setText("name="+products.getProductname());
                    productViewHolder.productPrice.setText("Cena " + products.getPrice() + "zł");
                    productViewHolder.productId.setText("Id="+ products.getProductid());

                    Picasso.get().load(products.getImage()).into(productViewHolder.image);


                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (type.equals("Admin"))
                            {
                                Intent intent = new Intent(HomeActivity.this, SellerMaintainProductsActivity.class);
                                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, products.getProductid());
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, products.getProductid());
                                startActivity(intent);
                            }


                        }
                    });
                }

                @NotNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                    return new ProductViewHolder(view);
                }
            };
            recyclerView.setAdapter(adapter);
        }else
        {
            FirebaseRecyclerOptions<Products> options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(productsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                            .build();
            adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
            {
                @Override
                protected void onBindViewHolder(@NotNull ProductViewHolder productViewHolder, int i, @NotNull final Products products) {
                    productViewHolder.txtProductName.setText(products.getProductname());
                    productViewHolder.productPrice.setText("Cena " + products.getPrice() + "zł");

                    Picasso.get().load(products.getImage()).into(productViewHolder.image);


                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (type.equals("Admin"))
                            {
                                Intent intent = new Intent(HomeActivity.this, SellerMaintainProductsActivity.class);
                                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, products.getProductid());
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, products.getProductid());
                                startActivity(intent);
                            }


                        }
                    });
                }

                @NotNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                    return new ProductViewHolder(view);
                }
            };
            recyclerView.setAdapter(adapter);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();


        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_search)
        {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_categories)
        {
            Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Paper.book().write(Prevalent.USER_PASSWORD_KEY, "");
            Paper.book().write(Prevalent.USER_ID, "");
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
