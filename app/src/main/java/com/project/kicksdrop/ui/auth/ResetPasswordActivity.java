package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.kicksdrop.R;

import java.net.Authenticator;

public class ResetPasswordActivity extends AppCompatActivity {
    Button resetPass, cancelReset;
    EditText requestEmail;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_reset_password);
        resetPass = (Button) findViewById(R.id.btn_sendRequetResetPass);
        cancelReset = (Button) findViewById(R.id.btn_cancel_reset);
        requestEmail = (EditText) findViewById(R.id.et_requestEmail);

        auth = FirebaseAuth.getInstance();
        cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(goLogin);
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = requestEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    requestEmail.setError("enter your registered email !");
                    return;
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instruction to reset your password", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}