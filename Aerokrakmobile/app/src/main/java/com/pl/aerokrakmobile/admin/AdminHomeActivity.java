package com.pl.aerokrakmobile.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.buyer.HomeActivity;
import com.pl.aerokrakmobile.buyer.MainActivity;
import com.pl.aerokrakmobile.sellers.SellerProductCategoryActivity;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity
{
    private Button logoutBtn, checkOrderBtn, checkApproveProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        logoutBtn = findViewById(R.id.admin_logout_button);
        checkOrderBtn = findViewById(R.id.admin_check_orders_button);
        checkApproveProductsBtn = findViewById(R.id.admin_check_approve_products_button);


        checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Paper.book().destroy();
                Toast.makeText(AdminHomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminOrdersActivity.class);
                startActivity(intent);

            }
        });
    }
}
