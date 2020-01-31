package com.pl.aerokrakmobile.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pl.aerokrakmobile.buyer.HomeActivity;
import com.pl.aerokrakmobile.buyer.MainActivity;
import com.pl.aerokrakmobile.R;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView paragliders, drives, harnesses, helmets;
    private ImageView parachutes, walkieTalkie, clothing, accessories;
    private Button maintainProductsBtn, logoutBtn, checkOrderBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        maintainProductsBtn = findViewById(R.id.admin_maintain_products_button);
        logoutBtn = findViewById(R.id.admin_logout_button);
        checkOrderBtn = findViewById(R.id.check_orders_button);

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.ADMIN_EDIT, "Admin");
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Paper.book().destroy();
                Toast.makeText(AdminCategoryActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminOrdersActivity.class);
                startActivity(intent);

            }
        });

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
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "paragliders");
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "drives");
                startActivity(intent);
            }
        });
        harnesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "harnesses");
                startActivity(intent);
            }
        });
        helmets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "helmets");
                startActivity(intent);
            }
        });
        parachutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "glasses");
                startActivity(intent);
            }
        });
        walkieTalkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "walkieTalkie");
                startActivity(intent);
            }
        });
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "clothing");
                startActivity(intent);
            }
        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra(AdminAddNewProductActivity.CATEGORY_NAME, "accessories");
                startActivity(intent);
            }
        });

    }
}
