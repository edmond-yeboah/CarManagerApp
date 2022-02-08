package com.example.carmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

public class Login extends AppCompatActivity {
    private TextView toregister;
    private AVLoadingIndicatorView avi;
    private TextInputLayout mEmail,mPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Button btnlogin;
    private boolean isvalidemail=false,isvalidpassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        toregister = (TextView)findViewById(R.id.toregister);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avilogin);
        mEmail = (TextInputLayout)findViewById(R.id.lemail);
        mPassword = (TextInputLayout)findViewById(R.id.lpass);
        btnlogin = (Button)findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        btnlogin.setEnabled(false);

        toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                mAuth.signInWithEmailAndPassword(mEmail.getEditText().getText().toString(),mPassword.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    stopAnim();
                                    //go to dashboard
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email",mEmail.getEditText().getText().toString());

                                    Intent intent = new Intent(Login.this,Dashboard.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                                    finish();
                                }else {
                                    task.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            stopAnim();
                                            Snackbar.make(v,"Oops! Email and Password new to me.",Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        });

        //checking form input validations
        mEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateemail(s);
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validateemail(((EditText)v).getText());
                }
            }
        });



        //checking form input validaton
        mPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatepassword(s);
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validatepassword(((EditText)v).getText());
                }
            }
        });
    }

    private void stopAnim() {
        avi.hide();
    }

    private void startAnim() {
        avi.smoothToShow();
    }

    private void validatepassword(Editable s) {
        if (TextUtils.isEmpty(s)){
            mPassword.setError("");
            btnlogin.setEnabled(false);
        }else {
            mPassword.setError(null);
            isvalidpassword=true;
            if (isvalidpassword & isvalidemail){
                btnlogin.setEnabled(true);
            }
        }
    }

    private void validateemail(Editable s) {
        if (TextUtils.isEmpty(s)){
            mEmail.setError("");
            btnlogin.setEnabled(false);
        }else {
            mEmail.setError(null);
            isvalidemail = true;
            if (isvalidemail & isvalidpassword){
                btnlogin.setEnabled(true);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser !=null){
           String email = currentuser.getEmail();
            Bundle bundle = new Bundle();
            bundle.putString("email",email);

            Intent intent = new Intent(Login.this,Dashboard.class);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
            finish();
        }
    }
}