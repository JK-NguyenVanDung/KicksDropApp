package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.kicksdrop.MainActivity;
import com.project.kicksdrop.R;

public class RegisterActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword, cfPassword, numPhone, fullName;
    Button btnSignIn, btnSignUp;
    RadioGroup groupCheck;
    RadioButton checkMale, checkFemale, checkother, checkAccept;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.et_regisEmail);
        inputPassword = (EditText) findViewById(R.id.et_regisPassword);
        btnSignIn = (Button) findViewById(R.id.btn_goSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        numPhone = (EditText) findViewById(R.id.et_regisPhone);
        fullName = (EditText) findViewById(R.id.et_regisName);
        groupCheck = (RadioGroup) findViewById(R.id.group_Radio_checkbox);
        checkMale = (RadioButton) findViewById(R.id.btn_check_male);
        checkFemale= (RadioButton) findViewById(R.id.btn_check_female);
        checkother = (RadioButton) findViewById(R.id.btn_check_other);
        checkAccept = (RadioButton) findViewById(R.id.btn_check_accept);

        auth = FirebaseAuth.getInstance();

//        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext() , MainActivity.class));
//            finish();
//        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(goLogin);
            }
        });


        int radioId = groupCheck.getCheckedRadioButtonId();
        RadioButton groupCheck = findViewById(radioId);
        String option = groupCheck.getText().toString();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                String name = fullName.getText().toString().trim();
                String phone = numPhone.getText().toString().trim();
//                String againPass = cfPassword.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    inputEmail.setError("Enter email address !");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter password !");
                    return;
                }
                if(password.length() < 6){
                    inputPassword.setError("Password too short, enter minimum 6 characters !");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                                        createUser( fUser.getUid(),name,email, option, phone);
                                    }
                                }
                            });
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }


        });

    }
    private void createUser(String userID, String email, String gender,String mobile,String name){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account");


        myRef.child(userID).child("coupon").child("CP1").setValue("CP1");
        myRef.child(userID).child("coupon").child("CP2").setValue("CP2");

        myRef.child(userID).child("email").setValue(email);
        myRef.child(userID).child("gender").setValue(gender);
        myRef.child(userID).child("mobile").setValue(mobile);
        myRef.child(userID).child("name").setValue(name);
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
