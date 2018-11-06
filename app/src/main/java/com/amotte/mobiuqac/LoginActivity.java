package com.amotte.mobiuqac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btSignIn;
    private Button btSignUp;
    private EditText edtEmail;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btSignIn = findViewById(R.id.btSignIn);
        btSignUp = findViewById(R.id.btSignUp);

        edtEmail = findViewById(R.id.emailinput);
        edtPassword = findViewById(R.id.passwordinput);


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emptyValidation()) {
                    Toast.makeText(LoginActivity.this, "Added User", Toast.LENGTH_SHORT).show();
                    edtEmail.setText("");
                    edtPassword.setText("");
                }else{
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emptyValidation()) {
                    if (true) {
                        Bundle mBundle = new Bundle();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Welcome  user", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        edtPassword.setText("");
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean emptyValidation() {
        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }
}