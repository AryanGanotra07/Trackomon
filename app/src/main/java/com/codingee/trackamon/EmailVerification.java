package com.codingee.trackamon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();

        }

    private void emailVerify(){

        final TextView emailverificaionStatus=(TextView) findViewById(R.id.emailVerificationStatus);
        if (currentuser!=null){
            currentuser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            emailverificaionStatus.setText("Email verification link has been sent to- "+currentuser.getEmail());

                            }
                            else {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }

                    }
                });

            }

        }

        private void logout(){
        mAuth.getInstance().signOut();
        startActivity(new Intent(this,SignupList.class));
        finish();
        }





    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.emailverificationTextView){
            if (!currentuser.isEmailVerified())
            emailVerify();
            else {
                startActivity(new Intent(EmailVerification.this, UserActivity.class));
                finish();
            }
        }
        else if (v.getId()==R.id.logoutButtonEmailVerification){
            logout();
            }
    }
}
