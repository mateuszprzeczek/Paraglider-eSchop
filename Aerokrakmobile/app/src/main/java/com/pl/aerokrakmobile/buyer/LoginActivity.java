package com.pl.aerokrakmobile.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pl.aerokrakmobile.R;
import com.pl.aerokrakmobile.admin.AdminHomeActivity;
import com.pl.aerokrakmobile.sellers.SellerHomeActivity;
import com.pl.aerokrakmobile.sellers.SellerLoginActivity;
import com.pl.aerokrakmobile.sellers.SellerProductCategoryActivity;
import com.pl.aerokrakmobile.model.Users;
import com.pl.aerokrakmobile.prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputEmailAddress;
    private TextInputLayout inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private String dbParentName = "Users";
    private CheckBox chkBoxRememberMe;
    private TextView adminLink, notAdminLink, forgetPasswordLink;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        inputPassword = findViewById(R.id.login_text_input_password);
        inputEmailAddress = findViewById(R.id.login_text_input_email);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        forgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                dbParentName = "Admins";
                loginAdmin();
            }
        });
        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                dbParentName = "Users";
            }
        });
        forgetPasswordLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra(ResetPasswordActivity.CHECK_SETTINGS, "login");
                startActivity(intent);
            }
        });

    }




    private void loginUser() {
        String userEmail = inputEmailAddress.getEditText().getText().toString();
        String userPassword = inputPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Please write Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Please write Your Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credencial");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(userEmail, userPassword);
        }
    }

    private void allowAccessToAccount(final String userId, final String password) {
        if (chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.USER_ID, userId);
            Paper.book().write(Prevalent.USER_PASSWORD_KEY, password);
        }



        mAuth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            final DatabaseReference rootRef;
                            rootRef = FirebaseDatabase.getInstance().getReference();
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Users usersData = dataSnapshot.child(dbParentName).child(mAuth.getUid()).getValue(Users.class);

                                    Prevalent.currentOnlineUser = usersData;

                                    loadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                        }
                    }
                });


    }
    private void loginAdmin()
    {
        final String email = inputEmailAddress.getEditText().getText().toString();
        final String password = inputPassword.getEditText().getText().toString();
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(dbParentName).child(email).exists())
                {
                    Users usersData = dataSnapshot.child(dbParentName).child(email).getValue(Users.class);

                    if (usersData.getPhone().equals(email))
                    {
                        if (usersData.getPassword().equals(password)) {
                            if (dbParentName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Logged in successfully",
                                        Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            } if (dbParentName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "Logged in Users successfully",
                                        Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,
                                    "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account with this " + email +
                            " address do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "Please, create new account",
                    //Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}










