package com.codingee.trackamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Set;

public class SplashActivitu extends AppCompatActivity {
    FirebaseAuth mAuth;
    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        //SharedPreferences preferences=getSharedPreferences("emailv",MODE_PRIVATE);
       // int v=preferences.getInt("email",0);

//Enable if email login

      /*  if (user!=null) {
            if (v == 1) {
                if (user.isEmailVerified()) {
                    Intent intent = new Intent(this, UserActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, EmailVerification.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                finish();
                }
        }
        else {
            Intent intent = new Intent(this, SignupList.class);
            startActivity(intent);
            finish();
        }
        */

      preferences=getSharedPreferences("Firstrun",MODE_PRIVATE);
      boolean firstrun=preferences.getBoolean("firstrun",true);
      boolean locked=preferences.getBoolean("locked",false);


          if (firstrun) {
              if (user!=null){
                  mAuth.signOut();
              }
            //  startActivity(new Intent(SplashActivitu.this, onboardfragment.class));
              startActivity(new Intent(SplashActivitu.this, UserActivity.class));
              finish();
          }
          else {

              if (user==null) {

                //  startActivity(new Intent(SplashActivitu.this, PhoneLogin.class));
                 startActivity(new Intent(SplashActivitu.this, SignupList.class));
                  finish();
              }


              if (user!= null) {
                  //   startActivity(new Intent(SplashActivitu.this,PhoneLogin.class));

                  if (locked) {

                     // startActivity(new Intent(SplashActivitu.this, FingerprintAuth.class));
                      startActivity(new Intent(SplashActivitu.this, UserActivity.class));
                      finish();
                  } else {
                      Toast.makeText(SplashActivitu.this, "UserActive", Toast.LENGTH_LONG).show();
                      startActivity(new Intent(SplashActivitu.this, UserActivity.class));
                      finish();
                  }
              }
          }


    }

    @Override
    protected void onStart() {
        super.onStart();


        }

    }

