package com.codingee.trackamon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.firebase.auth.FirebaseAuth;

import com.tooltip.Tooltip;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener {
    FirebaseAuth auth;
    ImageView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logo=(ImageView) findViewById(R.id.logo);


        SharedPreferences preferences=getSharedPreferences("Firstrun",MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit().putBoolean("firstrun",false);

        editor.apply();

       Tooltip tooltip=new Tooltip.Builder(logo).setText("Click to lock").setCornerRadius(5.5f).setGravity(Gravity.BOTTOM).setTextColor(Color.WHITE).setPadding(20).setArrowEnabled(true).setBackgroundColor(Color.GRAY).setCancelable(true).show();





        auth=FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                break;

            case R.id.logoutmenu:
                auth.signOut();
                Toast.makeText(UserActivity.this,"Logged out",Toast.LENGTH_LONG).show();
                startActivity(new Intent(UserActivity.this,PhoneLogin.class));
                finish();
                break;
                }

        return true;
    }

    @Override
    public void onClick(View v) {
      /*  if (v.getId()==R.id.logoutButton){
            auth.getInstance().signOut();
            Intent intent=new Intent(this,SignupList.class);
            startActivity(intent);
        }
        */

        if (v.getId() == R.id.calenderview) {


            /*


            */
            showcalenderview();
        }

        if (v.getId() == R.id.logo) {

            if (!getSharedPreferences("Firstrun",Context.MODE_PRIVATE).getBoolean("locked",false)) {
                getSharedPreferences("Firstrun", MODE_PRIVATE).edit().putBoolean("locked", true).apply();
                Toast.makeText(UserActivity.this,"Locked",Toast.LENGTH_LONG).show();
            }


            logo.animate().rotation(360).setDuration(2000).withEndAction(new Runnable() {
                @Override
                public void run() {
                   Toast toast= Toast.makeText(UserActivity.this,"hello",Toast.LENGTH_LONG);
                           toast.setGravity(Gravity.TOP|Gravity.LEFT,logo.getLeft(),logo.getBottom());
                           toast.show();


                        startActivity(new Intent(UserActivity.this, FingerprintAuth.class));
                        finish();


                }
            }).start();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "You picked the following date: From- "+dayOfMonth+"/"+(++monthOfYear)+"/"+year+" To "+dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
        Toast.makeText(UserActivity.this,date,Toast.LENGTH_LONG).show();
    }


    public void showcalenderview(){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);
        builder.setTitle("Choose Date Range");

        String[] items={"Last 7 days","Last 30 days","Last 1 year","Lifetime","Custom"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




                Date today=new Date();
                Calendar cal=new GregorianCalendar();
                cal.setTime(today);


                switch (which) {
                    case 0:
                        //now.add(Calendar.DAY_OF_YEAR,-7);
                        cal.add(Calendar.DAY_OF_MONTH, -7);
                        Toast.makeText(UserActivity.this,String.valueOf(cal.getTime()),Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        cal.add(Calendar.DAY_OF_MONTH, -30);
                        Toast.makeText(UserActivity.this,String.valueOf(cal.getTime()),Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        cal.add(Calendar.YEAR, -1);
                        Toast.makeText(UserActivity.this,String.valueOf(cal.getTime()),Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        cal.add(Calendar.YEAR ,-2);
                        Toast.makeText(UserActivity.this,String.valueOf(cal.getTime()),Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                                UserActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        //dpd.setAutoHighlight(mAutoHighlight);
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                        break;



                }
                }


        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
