package com.example.basma.wishlist;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText email;
    EditText password;
    String email_str;
    String password_str;
    Button login;
    Button signUp;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.editTextEmail);
        password = (EditText)findViewById(R.id.editTextPassword);
        login=(Button)findViewById(R.id.button2);
        signUp=(Button)findViewById(R.id.signupBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_str=email.getText().toString().trim();
                password_str=password.getText().toString();
                mAuth.signInWithEmailAndPassword(email_str, password_str)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("s", "signInWithEmail:success");
                                    Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                    // FirebaseUser user = mAuth.getCurrentUser();
                                    //   updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("f", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //     updateUI(null);
                                }

                            }
                        });

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            //    String e = currentUser.getEmail();
            //  Toast.makeText(LoginActivity.this, currentUser.getEmail()+" j ",
            //        Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(LoginActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();

        }
        // updateUI(currentUser);
    }

}
