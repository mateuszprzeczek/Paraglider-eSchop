package com.pl.aerokrakmobile.buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.pl.aerokrakmobile.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private TextInputLayout inputFullName, inputProfileName, inputEmail,  inputPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        inputFullName = findViewById(R.id.register_user_name_input);
        inputProfileName = findViewById(R.id.register_user_profile_name);
        inputEmail = findViewById(R.id.register_user_email);
        inputPassword = findViewById(R.id.register_user_password);
        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private boolean validateFullName()
    {
        String name = inputFullName.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            inputFullName.setError("Please write your full name");
            return false;
        }
        else
        {
            inputFullName.setError(null);
            return true;
        }
    }

    private boolean validateProfileName(final String profileName) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        String profileCheck = rootRef.child("Users").orderByChild(profileName).toString();
        if (!profileCheck.equals("")) {
            return false;
        }
        return true;

    }


        private void createAccount () {
            final String name = inputFullName.getEditText().getText().toString();
            final String profileName = inputProfileName.getEditText().getText().toString();
            final String email = inputEmail.getEditText().getText().toString();
            final String password = inputPassword.getEditText().getText().toString();

            if (TextUtils.isEmpty(name) | !validateFullName()) {
                inputFullName.setError("Please write Your full Name");

            } else if (TextUtils.isEmpty(profileName)) {
                inputProfileName.setError(getApplicationContext().getString(R.string.err_write_profile_name));
            } else if (validateProfileName(profileName)) {
                inputProfileName.setError("This Profile name already exists");
            } else if (TextUtils.isEmpty(email)) {
                inputEmail.setError(getApplicationContext().getString(R.string.err_write_email));
            } else if (TextUtils.isEmpty(password)) {
                inputPassword.setError(getApplicationContext().getString(R.string.err_write_password));
            } else {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validateUser(name, profileName, email, password);
            }
        }

        private void validateUser ( final String name, final String profileName, final String email, final String password){




            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                String userId = firebaseAuth.getCurrentUser().getUid();

                                HashMap<String, Object> userDataMap = new HashMap<>();
                                String userId2 = userId;
                                userDataMap.put("userId", userId2);
                                userDataMap.put("email", email);
                                userDataMap.put("password", password);
                                userDataMap.put("fullName", name);
                                userDataMap.put("profileName", profileName);
                                rootRef.child("Users").child(userId).updateChildren(userDataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NotNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Congratulations! Your account has been created",
                                                            Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                    Intent intent = new Intent(
                                                            RegisterActivity.this, LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                } else
                                                    {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Network error, " +
                                                            "please try again after some time...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "This " + profileName + " already exists", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this,
                                        "Please try again using another email address", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

        }


    }
