package com.example.basma.wishlist;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {


    EditText email;
    EditText password;
    String email_str;
    String password_str;
    Button signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.editTextEmail);
        password = (EditText)findViewById(R.id.editTextPassword);
        signup=(Button)findViewById(R.id.button2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email_str=email.getText().toString().trim();
                password_str=password.getText().toString();

                mAuth.createUserWithEmailAndPassword(email_str, password_str)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("success", "createUserWithEmail:success");
                                    Intent intent = new Intent(SignUpActivity.this,RegisterationActivity.class);
                                    startActivity(intent);
                                    finish();
                                    //  FirebaseUser user = mAuth.getCurrentUser();
                                    //   updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("failed", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    //   updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });



    }
}
