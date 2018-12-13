package com.amotte.mobiuqac;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rebillard.mobiuqac.User;

import java.util.Calendar;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private Button btSignIn;
    private Button btSignUp;
    private SignInButton btGoogle;
    private EditText Email;
    private EditText Password;
    private TextView twInfo;
    private FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btSignIn = findViewById(R.id.btSignIn);
        btSignUp = findViewById(R.id.btSignUp);
        btGoogle = findViewById(R.id.googleSignin);
        twInfo = findViewById(R.id.tw_info) ;

        Email = findViewById(R.id.emailinput);
        Password = findViewById(R.id.passwordinput);

        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twInfo.setVisibility(View.INVISIBLE);
                String email = Email.getText().toString();
                String pass = Password.getText().toString();
                if (isEmailValid()) {
                    if (isPasswordalid()){
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                                        mDatabase.child("users").child(user.getUid()).setValue(new User(user.getEmail()));
                                                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));finish();
                                                    } else {
                                                        twInfo.setText(R.string.error_incorrect_password);
                                                        twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                                                        twInfo.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });



                                    twInfo.setText(R.string.user_created);
                                    twInfo.setTextColor(getResources().getColor(R.color.colorWhite));
                                    twInfo.setVisibility(View.VISIBLE);
                                }
                                else{
                                    twInfo.setText(R.string.error_email_exist);
                                    twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                                    twInfo.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }else {
                        twInfo.setText(R.string.error_invalid_password);
                        twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                        twInfo.setVisibility(View.VISIBLE);
                    }
                }else{
                    twInfo.setText(R.string.error_invalid_email);
                    twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                    twInfo.setVisibility(View.VISIBLE);
                }
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twInfo.setVisibility(View.INVISIBLE);
                String email = Email.getText().toString();
                String pass = Password.getText().toString();
                if (isEmailValid()) {
                    if (isPasswordalid()){
                        mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(),CalendarActivity.class));finish();
                                    } else {
                                        twInfo.setText(R.string.error_incorrect_password);
                                        twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                                        twInfo.setVisibility(View.VISIBLE);
                                    }
                                }
                        });
                    }else {
                        twInfo.setText(R.string.error_invalid_password);
                        twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                        twInfo.setVisibility(View.VISIBLE);
                    }
                }else{
                    twInfo.setText(R.string.error_invalid_email);
                    twInfo.setTextColor(getResources().getColor(R.color.colorRed));
                    twInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        configureSignIn();


    }


    boolean isEmailValid() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches();
    }
    boolean isPasswordalid() {
        if (Password.getText().toString().length() >= 6){
            return true;
        }
        return false;
    }
    public void configureSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(user.getUid()).setValue(new User(user.getEmail()));
                            startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Raté :( ",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}