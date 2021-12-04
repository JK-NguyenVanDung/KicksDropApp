package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.kicksdrop.LoadingScreen;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;
import com.project.kicksdrop.ui.cart.CartListView;
import com.project.kicksdrop.ui.product.ProductDetail;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    EditText inputLoginEmail, inputLoginPassword;
    Button signIn, createAcc, forgot;
    CheckBox remember;
    ImageButton loginGoogle;
    FirebaseAuth mAuth;


    private final LoadingScreen loading = new LoadingScreen(LoginActivity.this);

    private AccountManagerFuture<Object> completedTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Intent registerIntent = getIntent();
        boolean justRegister =registerIntent.getBooleanExtra("register",false);
        if(justRegister){
            finish();
        }
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checked = preferences.getString("rememberMe","");
        if(checked.equals("true")){
            if(mAuth.getCurrentUser() !=null){
                Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


        setContentView(R.layout.activity_login);

        loginGoogle = findViewById(R.id.btn_google);
        createAcc = (Button) findViewById(R.id.btn_goRegister);
        inputLoginEmail = (EditText) findViewById(R.id.et_loginEmail);
        inputLoginPassword = (EditText) findViewById(R.id.et_loginPassword);
        signIn = (Button) findViewById(R.id.btn_login_signIn);
        forgot = (Button) findViewById(R.id.btn_forgot);
        remember = (CheckBox) findViewById(R.id.cb_remember);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
//        disableLayoutEditText(inputLoginEmail);
//        disableLayoutEditText(inputLoginPassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goForgot = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(goForgot);
            }
        });

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remember.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberMe","true");
                    editor.apply();
                }else{
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rememberMe","false");
                    editor.apply();
                }

            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                Intent intent = getIntent();
                if(intent.getStringExtra("id") != null) {
                    goSignUp.putExtra("id",intent.getStringExtra("id"));
                }
                startActivity(goSignUp);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputLoginEmail.getText().toString().trim();
                final String password = inputLoginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    inputLoginEmail.setError("Enter email address !");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    inputLoginPassword.setError("Enter password !");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(!task.isSuccessful()){
                            if(password.length()<6){
                                inputLoginPassword.setError("Password must be more than 6 characters");
                            }else {
                                Toast.makeText(LoginActivity.this, "ERROR ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            if(intent.getStringExtra("id") == null){

                                Intent loginSuccess = new Intent(LoginActivity.this,MainActivity.class);
                                loading.startLoadingScreen();
                                startActivity(loginSuccess);
                            }else{

                                finish();

                            }
                            finish();

                        }
                    }
                });
            }
        });
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

    }


    private void SignInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Sign In Successful",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }
    private boolean hasChildren = false;

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        try{
            AuthCredential authCredential = GoogleAuthProvider.getCredential( acct.getIdToken(), null );

            mAuth.signInWithCredential( authCredential ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText( LoginActivity.this, "Successful", Toast.LENGTH_SHORT ).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        UpdateUI( user );
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("account/"+ user.getUid());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    hasChildren = true;
                                }else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if(hasChildren = false){
                            DatabaseReference myRef = database.getReference("account");

                            myRef.child(user.getUid()).child("email").setValue(user.getEmail());
                            myRef.child(user.getUid()).child("gender").setValue("Male");
                            myRef.child(user.getUid()).child("mobile").setValue(" ");
                            myRef.child(user.getUid()).child("name").setValue(user.getEmail());
                            myRef.child(user.getUid()).child("address").setValue(" ");
                        }

                    } else {
                        Toast.makeText( LoginActivity.this, "Failed", Toast.LENGTH_SHORT ).show();
                        UpdateUI( null );
                    }
                }
            } );
        }catch(Exception e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();

        }
    }
    private void UpdateUI(FirebaseUser user){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        Intent intent = getIntent();
        if(intent.getStringExtra("id") == null){
            if (account != null){
                Intent loginSuccess = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(loginSuccess);
            }
        }else{

            finish();

        }
        finish();

    }
}