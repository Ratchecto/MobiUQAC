package com.amotte.mobiuqac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

    private Button btSignIn;
    private Button btSignUp;
    private EditText Email;
    private EditText Password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btSignIn = findViewById(R.id.btSignIn);
        btSignUp = findViewById(R.id.btSignUp);

        Email = findViewById(R.id.emailinput);
        Password = findViewById(R.id.passwordinput);


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = Password.getText().toString();
                if (!emptyValidation()) {
                    mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    Toast.makeText(LoginActivity.this, "Added User", Toast.LENGTH_SHORT).show();
                    Password.setText("");
                }else{
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = Password.getText().toString();

                if (!emptyValidation()) {
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(getApplicationContext(), "Authentication réussie !",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email ou mot de passe incorrect.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean emptyValidation() {
        if (TextUtils.isEmpty(Email.getText().toString()) || TextUtils.isEmpty(Password.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }
}