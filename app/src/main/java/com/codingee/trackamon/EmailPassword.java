package com.codingee.trackamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPassword extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private boolean loginBoolean = true;
    EditText emailEditText;
    EditText passwordEditText;
    TextView createAccount;
    Button loginButton;
    TextView loginText;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        editor = getSharedPreferences("emailv", MODE_PRIVATE).edit();

        //setting up toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //

        findViewById(R.id.forgotPassword).setOnClickListener(this);
        findViewById(R.id.createAccount).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        createAccount = (TextView) findViewById(R.id.createAccount);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginText = (TextView) findViewById(R.id.loginText);

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;

                }
                return false;
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (emailEditText.getText().toString().contains("@") && passwordEditText.getText() != null) {
                        if (loginBoolean == false) {
                        signup();
                        return true;
                    }
                    else {
                        login();
                        return true;
                    }
                }
                else {
                        return false;
                    }

                }
                return false;
            }
        });


    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            editor.putInt("email",1);
            editor.apply();
            if (user.isEmailVerified()) {
                Intent i = new Intent(this, UserActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(this, EmailVerification.class);
                startActivity(i);
                finish();
            }

        } else
            Toast.makeText(getApplicationContext(), "User not login", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createAccount) {
            if (loginBoolean) {
                createAccount.setText("Login Instead");
                loginButton.setText("Signup");
                loginText.setText("Signup");
                loginBoolean = false;

            } else {
                createAccount.setText("Create Account");
                loginButton.setText("Login");

                loginText.setText("Login");
                loginBoolean = true;

            }

        }

        if (v.getId() == R.id.forgotPassword) {
            startActivity(new Intent(this,ForgotPassword.class));


        }
        if (v.getId() == R.id.loginButton) {
            if (emailEditText.getText().toString().contains("@")&&passwordEditText.getText().toString()!=null) {
                if (loginBoolean) {
                    login();
                } else {
                    signup();
                }
            }
            else {
                Toast.makeText(this,"Enter Valid Email/Password",Toast.LENGTH_SHORT).show();
            }

        }
        if (v.getId() == R.id.relativeLayout || v.getId() == R.id.trackomon || v.getId() == R.id.spendit || v.getId() == R.id.recordit || v.getId() == R.id.loginText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    private void signup() {
        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(EmailPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });


    }

    private void login() {
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(EmailPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });


    }


}


