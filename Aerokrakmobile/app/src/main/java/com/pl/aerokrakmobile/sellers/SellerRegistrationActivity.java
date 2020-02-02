package com.pl.aerokrakmobile.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.buyer.MainActivity;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity
{
    private EditText sellerNameInput, sellerPhoneInput, sellerEmailInput, sellerPasswordInput, sellerAddressInput;
    private Button registerButton;
    private Button sellerLoginBegin;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        sellerNameInput = findViewById(R.id.seller_name);
        sellerPhoneInput = findViewById(R.id.seller_phone);
        sellerEmailInput = findViewById(R.id.seller_email);
        sellerPasswordInput = findViewById(R.id.seller_password);
        sellerAddressInput = findViewById(R.id.seller_address);
        registerButton = findViewById(R.id.seller_register_btn);
        sellerLoginBegin = findViewById(R.id.seller_already_registered_btn);
        loadingBar = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerSeller();
            }
        });

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerSeller()
    {
        final String name = sellerNameInput.getText().toString();
        final String phone = sellerPhoneInput.getText().toString();
        final String email = sellerEmailInput.getText().toString();
        final String password = sellerPasswordInput.getText().toString();
        final String address = sellerAddressInput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {
            loadingBar.setTitle("Create Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credencial");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                                String sellerId = mAuth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                String sellerId2 = sellerId;
                                sellerMap.put("sellerId", sellerId2);
                                sellerMap.put("phone", phone);
                                sellerMap.put("email", email);
                                sellerMap.put("address", address);
                                sellerMap.put("name", name);

                                rootRef.child("Sellers").child(sellerId).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this, "You have Registered", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(SellerRegistrationActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        else 
        {
            Toast.makeText(this, "Please complete the Registration Form ", Toast.LENGTH_SHORT).show();
        }
    }
}
