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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private TextView tologin;
    private TextInputLayout mEmail,mPassword,mCPassword;
    private Button btnregister;
    private boolean isvalidEmail=false,isvalidPassword = false, ispasswordmatch=false;
    private FirebaseDatabase database;
    private AVLoadingIndicatorView avi;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private UserClass userClass;

    private String fname="",lname="",email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tologin = (TextView)findViewById(R.id.tologin);
        btnregister = (Button)findViewById(R.id.register);
        mEmail = (TextInputLayout)findViewById(R.id.email);
        mPassword = (TextInputLayout)findViewById(R.id.pass);
        mCPassword = (TextInputLayout)findViewById(R.id.cpass);
        avi = (AVLoadingIndicatorView)findViewById(R.id.aviregister);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();

        //disabling button and enabling it
        btnregister.setEnabled(false);



        //checking for form input validations
        mEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ignore
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







        //checking for form input validations
        mPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ignore
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





        //checking for form input validations
        mCPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatecpassword(s);
            }
        });

        mCPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validatecpassword(((EditText)v).getText());
                }
            }
        });



        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                //check if email already exist
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            if (ds.child("email").getValue().equals(mEmail.getEditText().getText().toString())){
                                Snackbar.make(v,"Email exist",Snackbar.LENGTH_LONG).show();
                                mEmail.setError("Email exist");
                                mEmail.requestFocus();
                                stopAnim();
                            }else {
                                //register user with email and password
                                mAuth.createUserWithEmailAndPassword(mEmail.getEditText().getText().toString(),mPassword.getEditText().getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    saveUserInfo(v);
                                                }else {
                                                    task.addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            stopAnim();
                                                            Snackbar snackbar = Snackbar.make(v,"Error: "+e,Snackbar.LENGTH_LONG);
                                                            snackbar.show();
                                                        }
                                                    });
                                                }
                                            }
                                        });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });




        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
                finish();
            }
        });
    }

    private void startAnim() {
        avi.smoothToShow();
    }

    private void saveUserInfo(View view) {
        String uid = reference.push().getKey();
        email = mEmail.getEditText().getText().toString();

        userClass = new UserClass(fname,lname,email,uid);

        reference.child(uid).setValue(userClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                stopAnim();
               //go to dashboard activity
                Intent intent = new Intent(Register.this,Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                //Snackbar.make(view,"successful",Snackbar.LENGTH_LONG).show();
            }
        });
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG).show();
//            }
//        });

    }

    private void stopAnim() {
        avi.hide();
    }


    private void validatecpassword(Editable s) {
        if (!TextUtils.isEmpty(s) & s.toString().length()>=6 & s.toString().equals(mPassword.getEditText().getText().toString())){
            ispasswordmatch = true;
            mCPassword.setError(null);
            if (isvalidPassword & isvalidEmail & ispasswordmatch){
                btnregister.setEnabled(true);
            }else {
                btnregister.setEnabled(false);
            }
        }else {
            mCPassword.setError("passwords do not match");
            //ispasswordmatch=false;
            btnregister.setEnabled(false);
        }
    }

    private void validatepassword(Editable s) {
        if (!TextUtils.isEmpty(s) & s.toString().length()>=6){
            isvalidPassword=true;
            mPassword.setError(null);
            if (isvalidPassword & isvalidEmail & ispasswordmatch){
                btnregister.setEnabled(true);
            }else {
                btnregister.setEnabled(false);
            }
        }
        else {
            mPassword.setError("too short");
            //isvalidPassword=false;
            btnregister.setEnabled(false);
        }

        if (!TextUtils.isEmpty(mCPassword.getEditText().getText().toString())){
            if (s.toString().equals(mCPassword.getEditText().getText().toString())){
                ispasswordmatch=true;
                mCPassword.setError(null);
                if (isvalidPassword & isvalidEmail & ispasswordmatch){
                    btnregister.setEnabled(true);
                }else {
                    btnregister.setEnabled(false);
                }
            }
            else {
                mCPassword.setError("passwords do not match");
                //ispasswordmatch=false;
                btnregister.setEnabled(false);
            }
        }
    }

    private void validateemail(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            if (!s.toString().contains("@")) {
                mEmail.setError("Invalid email");
                btnregister.setEnabled(false);
            } else {
                isvalidEmail = true;
                mEmail.setError(null);
                if (isvalidPassword & isvalidEmail & ispasswordmatch){
                    btnregister.setEnabled(true);
                }else {
                    btnregister.setEnabled(false);
                }
            }
        } else {
            mEmail.setError("Invalid email");
            //isvalidEmail=false;
            btnregister.setEnabled(false);
        }
    }
}