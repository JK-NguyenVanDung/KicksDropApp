package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    EditText inputLoginEmail, inputLoginPassword;
    Button signIn, createAcc, forgot;
    CheckBox remember;
    FloatingActionButton loginGoogle;
    ProgressBar progressBar;
    FirebaseAuth auth;
    private AccountManagerFuture<Object> completedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        loginGoogle = (FloatingActionButton) findViewById(R.id.fab_google);
        createAcc = (Button) findViewById(R.id.btn_goRegister);
        inputLoginEmail = (EditText) findViewById(R.id.et_loginEmail);
        inputLoginPassword = (EditText) findViewById(R.id.et_loginPassword);
        signIn = (Button) findViewById(R.id.btn_login_signIn);
        progressBar = findViewById(R.id.progressBar2);
        forgot = (Button) findViewById(R.id.btn_forgot);
        remember = (CheckBox) findViewById(R.id.cb_remember);
        disableLayoutEditText(inputLoginEmail);
        disableLayoutEditText(inputLoginPassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goForgot = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(goForgot);
            }
        });

        auth = FirebaseAuth.getInstance();

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
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

                progressBar.setVisibility(View.VISIBLE);
                //firebase.auth().setPersistence(this.remember.checked ? fireauth.Auth.Persistence.LOCAL : fireauth.Auth.Persistence.SESSION)

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       progressBar.setVisibility(View.GONE);
                       if(!task.isSuccessful()){
                        if(password.length()<6){
                            inputLoginPassword.setError("Password must be more than 6 characters");
                        }else {
                            Toast.makeText(LoginActivity.this, "ERROR ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                       }else{
                           Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                           Intent loginSuccess = new Intent(LoginActivity.this,MainActivity.class);
                           startActivity(loginSuccess);
                           finish();
                       }
                    }
                });
            }
        });



    }
    private void signInGoogle(FirebaseAuth auth){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        SignIn();
    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void disableLayoutEditText(EditText editText) {
        editText.setBackgroundColor(Color.TRANSPARENT);
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
            GoogleSignInAccount acc = (GoogleSignInAccount) completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Sign In successful",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successfull",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    UpdateUI(user);
                }else {
                    Toast.makeText(LoginActivity.this, "Failed",Toast.LENGTH_SHORT).show();
                    UpdateUI(null);
                }
            }
        });

    }
    private void UpdateUI(FirebaseUser user){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            Intent loginSuccess = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(loginSuccess);
            finish();
        }
    }
}