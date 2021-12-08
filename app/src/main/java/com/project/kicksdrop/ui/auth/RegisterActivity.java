package com.project.kicksdrop.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.project.kicksdrop.ui.product.ProductDetail;

public class RegisterActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword, cfPassword, numPhone, fullName, infoAddress;
    TextInputLayout layoutEmail, layoutPass, layoutCfPassword, layoutPhone, layoutFullName;
    Button btnSignIn, btnSignUp;
    ImageButton loginGoogle;
    RadioGroup groupCheck;
    RadioButton checkMale, checkFemale, checkother, checkAccept;
    FirebaseAuth auth;
    FirebaseUser fUser;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    private final LoadingScreen loading = new LoadingScreen(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        inputEmail = (EditText) findViewById(R.id.et_regisEmail);
        inputPassword = (EditText) findViewById(R.id.et_regisPassword);
        btnSignIn = (Button) findViewById(R.id.btn_goSignIn);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        numPhone = (EditText) findViewById(R.id.et_regisPhone);
        fullName = (EditText) findViewById(R.id.et_regisName);
        cfPassword = (EditText) findViewById(R.id.et_regisAgainPassword);

        groupCheck = (RadioGroup) findViewById(R.id.group_Radio_checkbox);
        checkMale = (RadioButton) findViewById(R.id.btn_check_male);
        checkFemale= (RadioButton) findViewById(R.id.btn_check_female);
        checkother = (RadioButton) findViewById(R.id.btn_check_other);
        checkAccept = (RadioButton) findViewById(R.id.btn_check_accept);

        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEmail);
        layoutCfPassword = (TextInputLayout) findViewById(R.id.layoutPassword);
        layoutPhone = (TextInputLayout) findViewById(R.id.layoutName);
        layoutPass = (TextInputLayout) findViewById(R.id.layoutPhone);
        layoutFullName = (TextInputLayout) findViewById(R.id.layoutAgainPassword);
        loginGoogle = findViewById(R.id.btn_register_google);
        disableEditText(layoutPhone);
        disableEditText(layoutEmail);
        disableEditText(layoutFullName);
        disableEditText(layoutCfPassword);
        disableEditText(layoutPass);



        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });
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

                String againPass = cfPassword.getText().toString().trim();

                String name = fullName.getText().toString().trim();
                String phone = numPhone.getText().toString().trim();

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

                if (TextUtils.isEmpty(againPass)){
                    cfPassword.setError("Enter again password");
                    return;
                }

                if(!againPass.equals(password)){
                    cfPassword.setError("Password does not match, Please enter again!");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(),Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        loading.startLoadingScreen();
                                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                                        createUser( fUser.getUid(),email, option, phone, name);
                                    }
                                }
                            });
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();


                        }
                    }
                });
            }


        });

    }


    private static void disableEditText(TextInputLayout textInputLayout) {
        textInputLayout.setBackgroundColor(Color.TRANSPARENT);
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
        myRef.child(userID).child("address").setValue(" ");
    }
    @Override
    protected void onResume() {
        super.onResume();
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
            Toast.makeText(RegisterActivity.this,"Sign In Successful",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(RegisterActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }
    private boolean hasChildren = false;

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        try{
            AuthCredential authCredential = GoogleAuthProvider.getCredential( acct.getIdToken(), null );

            auth.signInWithCredential( authCredential ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText( RegisterActivity.this, "Successful", Toast.LENGTH_SHORT ).show();
                        FirebaseUser user = auth.getCurrentUser();
                        UpdateUI( user );
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("account/"+ user.getUid());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    hasChildren = true;
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
                        Toast.makeText( RegisterActivity.this, "Failed", Toast.LENGTH_SHORT ).show();
                        UpdateUI( null );
                    }
                }
            } );
        }catch(Exception e){
            Toast.makeText(RegisterActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();

        }
    }
    private void UpdateUI(FirebaseUser user){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        Intent intent = getIntent();
        if(intent.getStringExtra("id") == null){
            if (account != null){
                Intent loginSuccess = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(loginSuccess);
            }
        }else{

            finish();

        }
        finish();

    }

}
