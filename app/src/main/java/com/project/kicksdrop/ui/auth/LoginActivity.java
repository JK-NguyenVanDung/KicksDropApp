package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    EditText inputLoginEmail, inputLoginPassword;
    Button signIn, createAcc, forgot;
    CheckBox remember;
    FloatingActionButton loginGoogle;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
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
//        disableLayoutEditText(inputLoginEmail);
//        disableLayoutEditText(inputLoginPassword);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goForgot = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(goForgot);
            }
        });

        mAuth = FirebaseAuth.getInstance();

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

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
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
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("534353802380-4okktqkcuoi27jk645pa7e28e28nksmi.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this,gso);
                mAuth = FirebaseAuth.getInstance();
                signIn();
            }
        });

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UpdateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            UpdateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    private void UpdateUI(FirebaseUser user){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            Intent loginSuccess = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(loginSuccess);
            finish();
        }
    }
}