package com.pl.aerokrakmobile.sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.buyer.MainActivity;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private ImageView paragliders, drives, harnesses, helmets;
    private ImageView parachutes, walkieTalkie, clothing, accessories;
    BottomNavigationView bottomNavigationView;



    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                            finish();
                            return true;

                        case R.id.navigation_logout:
                            final FirebaseAuth fAuth = FirebaseAuth.getInstance();
                            fAuth.signOut();
                            Intent logoutIntent = new Intent(SellerProductCategoryActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_seller_product_category);

        bottomNavigationView = findViewById(R.id.seller_category_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);



        paragliders = (ImageView) findViewById(R.id.paragliders);
        drives = (ImageView) findViewById(R.id.drives);
        harnesses = (ImageView) findViewById(R.id.harnesses);
        helmets = (ImageView) findViewById(R.id.helmets);
        parachutes = (ImageView) findViewById(R.id.parachutes);
        walkieTalkie = (ImageView) findViewById(R.id.walkie_talkie);
        clothing = (ImageView) findViewById(R.id.clothing);
        accessories = (ImageView) findViewById(R.id.accessories);


        paragliders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "paragliders");
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "drives");
                startActivity(intent);
            }
        });
        harnesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "harnesses");
                startActivity(intent);
            }
        });
        helmets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "helmets");
                startActivity(intent);
            }
        });
        parachutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "glasses");
                startActivity(intent);
            }
        });
        walkieTalkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "walkieTalkie");
                startActivity(intent);
            }
        });
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "clothing");
                startActivity(intent);
            }
        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra(SellerAddNewProductActivity.CATEGORY_NAME, "accessories");
                startActivity(intent);
            }
        });

    }
}
