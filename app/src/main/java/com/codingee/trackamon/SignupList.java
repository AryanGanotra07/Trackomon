package com.codingee.trackamon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class SignupList extends AppCompatActivity implements View.OnClickListener {

    int RC_SIGN_IN=1;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_list);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        findViewById(R.id.emailButton).setOnClickListener(this);
        findViewById(R.id.googleButton).setOnClickListener(this);

GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
mAuth=FirebaseAuth.getInstance();






    }



    public void signin(){
        Intent intent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                
            }catch (ApiException e){
                Toast.makeText(SignupList.this, e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential=GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SignupList.this,UserActivity.class));
                    Toast.makeText(SignupList.this,"Signup successful",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignupList.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void updateUI(FirebaseUser user) {

        if (user!=null){
            startActivity(new Intent(this,UserActivity.class));
        }
        else {
            Toast.makeText(this,"User not logged in",Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.emailButton){
            loadIntent(EmailPassword.class);
            }

         if(v.getId()==R.id.googleButton){
            signin();

            }

            if(v.getId()==R.id.phonenumberButton){
            startActivity(new Intent(SignupList.this,PhoneLogin.class));
            }



    }

    private void phonesignin() {

        Toast.makeText(SignupList.this,"pressed",Toast.LENGTH_SHORT).show();


    }

    public void loadIntent(Class<EmailPassword> activity){
        Intent i=new Intent(this,activity);
        startActivity(i);
    }
}
