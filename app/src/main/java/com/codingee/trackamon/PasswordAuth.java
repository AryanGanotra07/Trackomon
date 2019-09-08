package com.codingee.trackamon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PasswordAuth extends AppCompatActivity implements TextWatcher,View.OnClickListener,View.OnFocusChangeListener,View.OnKeyListener {
    private EditText et_digit1, et_digit2, et_digit3, et_digit4;
    private TextView passwordtitle;
    private int whoHasFocus;
    ArrayList<String> code = new ArrayList<String>(4);
    int inputpass=0;
    DatabaseReference reference;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_password_auth);

        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        database=FirebaseDatabase.getInstance();
        reference=database.getReference("profiles");

       Query passwordquery=reference.orderByChild("uid").equalTo("B9BZZZNmf8dsWC0UKApuDanNv8h1");

        code.add("0");
        code.add("0");
        code.add("0");
        code.add("0");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleshanpshot:dataSnapshot.getChildren()) {

                    if (singleshanpshot.child("uid").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))

                    Log.i("PasswordSaved", singleshanpshot.child("password").getValue().toString());



                        if (singleshanpshot.child("password").exists()){


                        }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        initializeView();
        et_digit1.requestFocus();






    }

    private void initializeView() {

        et_digit1 = (EditText) findViewById(R.id.ed_txt1);
        et_digit2 = (EditText)findViewById(R.id.ed_txt2);
        et_digit3 = (EditText) findViewById(R.id.ed_txt3);
        et_digit4 = (EditText) findViewById(R.id.ed_txt4);

        passwordtitle=(TextView) findViewById(R.id.passtitle);
        setListners();
    }

    private void setListners() {

        et_digit1.addTextChangedListener(this);
        et_digit2.addTextChangedListener(this);
        et_digit3.addTextChangedListener(this);
        et_digit4.addTextChangedListener(this);

        et_digit1.setOnKeyListener(this);
        et_digit2.setOnKeyListener(this);
        et_digit3.setOnKeyListener(this);
        et_digit4.setOnKeyListener(this);

        et_digit1.setOnFocusChangeListener(this);
        et_digit2.setOnFocusChangeListener(this);
        et_digit3.setOnFocusChangeListener(this);
        et_digit4.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.ed_txt1:
                whoHasFocus=1;
                break;
            case R.id.ed_txt2:
                whoHasFocus=2;
                break;
            case R.id.ed_txt3:
                whoHasFocus=3;
                break;
            case R.id.ed_txt4:
                whoHasFocus=4;
                break;
            default:
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {



            switch (whoHasFocus) {
                case 1:
                    if (!et_digit1.getText().toString().isEmpty()) {
                        code.set(0,et_digit1.getText().toString());
                            et_digit2.requestFocus();
                        }

                    break;

                case 2:
                    if (!et_digit2.getText().toString().isEmpty()) {
                        code.set(1,et_digit2.getText().toString());
                        et_digit3.requestFocus();
                    }
                    break;

                case 3:
                    if (!et_digit3.getText().toString().isEmpty()) {
                        code.set(2,et_digit3.getText().toString());
                        et_digit4.requestFocus();
                    }
                    break;

                case 4:
                    if (!et_digit4.getText().toString().isEmpty()) {
                        code.set(3,et_digit4.getText().toString());

                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError==null){
                                    Toast.makeText(PasswordAuth.this,"Successfuly added",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(PasswordAuth.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password").setValue(code.toString(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError==null){
                                    Toast.makeText(PasswordAuth.this,"Successfuly added",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(PasswordAuth.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError==null){
                                    Toast.makeText(PasswordAuth.this,"Successfuly added",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(PasswordAuth.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    break;


                default:
                    break;
            }
        }





    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (keyCode == KeyEvent.KEYCODE_FORWARD_DEL||keyCode == KeyEvent.KEYCODE_DEL)
            {
                switch(v.getId())
                {
                    case R.id.ed_txt2:
                        if (et_digit2.getText().toString().isEmpty())
                            et_digit1.requestFocus();
                        break;

                    case R.id.ed_txt3:
                        if (et_digit3.getText().toString().isEmpty())
                            et_digit2.requestFocus();
                        break;

                    case R.id.ed_txt4:
                        if (et_digit4.getText().toString().isEmpty())
                            et_digit3.requestFocus();
                        break;

                    default:
                        break;
                }
            }
              if (keyCode==KeyEvent.KEYCODE_ENTER) {

                  Log.i("password",code.toString());
              }


        }
        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


        }

    }





}



