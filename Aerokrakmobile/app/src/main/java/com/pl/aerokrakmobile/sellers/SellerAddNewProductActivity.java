package com.pl.aerokrakmobile.sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pl.aerokrakmobile.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {
    public static final String CATEGORY_NAME = "extra_category_id";
    private String categoryName, description, price, productName, saveCurrentDate, saveCurrentTime;
    private String sellerName, sellerPhone, sellerAddress, sellerEmail, sellerId;
    private ImageView productImageInput;
    private EditText productNameInput, productDescriptionInput, productPriceInput;
    private Button addNewProductButton;
    private static final int galleryPic = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef, sellersRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        categoryName = getIntent().getExtras().get(CATEGORY_NAME).toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        productImageInput = (ImageView) findViewById(R.id.select_product_image);
        productNameInput = (EditText) findViewById(R.id.product_name);
        productDescriptionInput = (EditText) findViewById(R.id.product_description);
        productPriceInput = (EditText) findViewById(R.id.product_price);
        addNewProductButton = (Button) findViewById(R.id.add_new_product_btn);
        loadingBar = new ProgressDialog(this);

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            sellerName = dataSnapshot.child("name").getValue().toString();
                            sellerAddress = dataSnapshot.child("address").getValue().toString();
                            sellerPhone = dataSnapshot.child("phone").getValue().toString();
                            sellerEmail = dataSnapshot.child("email").getValue().toString();
                            sellerId = dataSnapshot.child("sellerId").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        productImageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });





        //Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            productImageInput.setImageURI(imageUri);
        }
    }
    private void validateProductData() {
        description = productDescriptionInput.getText().toString();
        price = productPriceInput.getText().toString();
        productName = productNameInput.getText().toString();

        if (imageUri == null){
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please, write product description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please, write product price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productName)){
            Toast.makeText(this, "Please, write product name", Toast.LENGTH_SHORT).show();
        }
        else {
            storeProductInformation();
        }

    }

    private void storeProductInformation() {

        loadingBar.setTitle("Add new Product");
        loadingBar.setMessage("Please wait, while we are adding the new Product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + "jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errMessage = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error" + errMessage, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "" +
                                    "got Product image Url successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        String productId1 = productRandomKey;
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productid", productId1);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", price);
        productMap.put("productname", productName);

        productMap.put("sellerName", sellerName);
        productMap.put("sellerAddress", sellerAddress);
        productMap.put("sellerPhone", sellerPhone);
        productMap.put("sellerId", sellerId);
        productMap.put("sellerEmail", sellerEmail);
        productMap.put("productState", "Not Approved");

        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(SellerAddNewProductActivity.this,
                                    SellerProductCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "" +
                                    "Product is added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            String errMsg = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "" +
                                    "Error: " + errMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
