package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.project.kicksdrop.R;

import java.net.Authenticator;

public class ResetPasswordActivity extends AppCompatActivity {
    Button resetPass;
    EditText requestEmail;
    FirebaseAuth auth;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_reset_password);
        resetPass = (Button) findViewById(R.id.btn_sendRequetResetPass);
        requestEmail = (EditText) findViewById(R.id.et_requestEmail);
        back = (ImageButton) findViewById(R.id.change_pass_ibtn_prev);

        auth = FirebaseAuth.getInstance();


        back.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

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