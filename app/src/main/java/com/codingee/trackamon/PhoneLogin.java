package com.codingee.trackamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity implements View.OnClickListener {

    CountryCodePicker cpp;
    EditText phoneedittext;
    FirebaseAuth mAuth;
    private String mVerificationId;
    LinearLayout ll;
    EditText otpedittext;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    SignupList signupList;
    PhoneAuthCredential credential;
    EmailPassword emailPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        mAuth=FirebaseAuth.getInstance();

        ll=(LinearLayout) findViewById(R.id.ll);
        signupList=new SignupList();
        emailPassword=new EmailPassword();
        otpedittext=(EditText) findViewById(R.id.otpedittext);

        cpp = (CountryCodePicker) findViewById(R.id.cpp);
        phoneedittext = (EditText) findViewById(R.id.phoneedittext);
        cpp.registerCarrierNumberEditText(phoneedittext);


        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences preferences=getSharedPreferences("Firstrun",MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit().putBoolean("firstrun",false);

        editor.apply();

    }

    public void changevisibility(Boolean b){

        if (b){
            ll.setVisibility(View.GONE);
            otpedittext.setVisibility(View.VISIBLE);
            findViewById(R.id.resendotpbutton).setVisibility(View.VISIBLE);
            findViewById(R.id.submitotpbutton).setVisibility(View.VISIBLE);


        }
        else {

            otpedittext.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);

            findViewById(R.id.resendotpbutton).setVisibility(View.GONE);
            findViewById(R.id.submitotpbutton).setVisibility(View.GONE);

        }


    }


    public void phonesignin() {
        Toast.makeText(this, cpp.getFullNumberWithPlus(), Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                cpp.getFullNumberWithPlus(),        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();

                        if (code != null) {
                            otpedittext.setText(code);
                            //verifying the code
                            verifyVerificationCode(code);
                        }
                        else {
                            code=otpedittext.getText().toString();
                            if (code!=null) {
                                verifyVerificationCode(code);
                            }

                        }

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(PhoneLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        changevisibility(true);
                        mVerificationId = s;
                        mResendToken = forceResendingToken;


                    }
                });



    }


    private void verifyVerificationCode(String otp) {
        //creating the credential
        credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
       // signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            emailPassword.editor= getSharedPreferences("emailv", MODE_PRIVATE).edit();
                            emailPassword.editor.putInt("email",0);
                            emailPassword.editor.apply();
                            startActivity(new Intent(PhoneLogin.this,UserActivity.class));
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }



        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.sendotpbutton:
                    phonesignin();
                    break;

                case R.id.submitotpbutton:
                    signInWithPhoneAuthCredential(credential);
                    break;

                case R.id.resendotpbutton:
                    changevisibility(false);
                    break;

            }

        }
    }

