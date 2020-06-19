package com.pl.aerokrakmobile.sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerMaintainProductsActivity extends AppCompatActivity
{

    private ImageView image;
    private EditText name, price, description;
    private Button applyChangesBtn, deleteBtn;
    public static final String PRODUCT_ID = "extraId";
    private DatabaseReference productsRef;
    private String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra(PRODUCT_ID);
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        image = findViewById(R.id.maintain_product_image);
        name = findViewById(R.id.maintain_product_name);
        price = findViewById(R.id.maintain_product_price);
        description = findViewById(R.id.maintain_product_description);
        applyChangesBtn = findViewById(R.id.apply_changes_maintain_products_button);
        deleteBtn = findViewById(R.id.maintain_delete_products_button);

        displaySpecificProductDetail();


        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteThisProduct();
            }
        });



    }

    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NotNull Task<Void> task)
            {
                Toast.makeText(SellerMaintainProductsActivity.this, "Product removed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
            }
        });
    }


    private void applyChanges()
    {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals(""))
        {
            Toast.makeText(this, "Please, write Product Name", Toast.LENGTH_SHORT).show();
        }
        if (pPrice.equals(""))
        {
            Toast.makeText(this, "Please, write Product Price", Toast.LENGTH_SHORT).show();
        }
        if (pDescription.equals(""))
        {
            Toast.makeText(this, "Please, write Product Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productid", productId);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("productname", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NotNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SellerMaintainProductsActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }



    private void displaySpecificProductDetail()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("productname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(image);

                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SellerMaintainProductsActivity.this, SellerProductCategoryActivity.class);
        startActivity(intent);
    }
}
