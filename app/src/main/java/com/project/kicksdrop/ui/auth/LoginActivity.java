package com.project.kicksdrop.ui.auth;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;

public class LoginActivity extends AppCompatActivity {
    EditText inputLoginEmail, inputLoginPassword;
    Button signIn, createAcc, forgot;
    CheckBox remember;
    ProgressBar progressBar;
    FirebaseAuth auth;
    Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        auth = FirebaseAuth.getInstance();
//
//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }


        setContentView(R.layout.activity_login);

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

    private void disableLayoutEditText(EditText editText) {
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}