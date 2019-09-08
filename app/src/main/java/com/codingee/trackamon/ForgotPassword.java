package com.codingee.trackamon;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText forgotPasswordEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //setting up toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        forgotPasswordEditText=(EditText) findViewById(R.id.forgotPasswordEditText);

        forgotPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    if (!forgotPasswordEditText.getText().toString().isEmpty()) {
                        forgotpassword();
                    return true;
                }else return false;


                    }
                return false;
            }
        });




    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.resetPasswordButton){
            if (forgotPasswordEditText.getText()!=null) {
                forgotpassword();
            }
            else {
                Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void forgotpassword(){


        final TextView forgotPasswordStatus=(TextView)findViewById(R.id.forgotPasswordStatus);


        FirebaseAuth.getInstance().sendPasswordResetEmail(forgotPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    forgotPasswordStatus.setText("Reset Password Email Sent to- "+forgotPasswordEditText.getText());
                }
                else {
                    Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
