package com.codingee.trackamon;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class onboardfragment extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    LinearLayout dotslayout;
    TextView[] dots;
    Button nextb;
    FirebaseAuth mauth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.onboardfragment);
        super.onCreate(savedInstanceState);



        viewPager=(ViewPager)findViewById(R.id.viewpager);
        dotslayout=(LinearLayout) findViewById(R.id.dotslayout);
        sliderAdapter=new SliderAdapter(this);
        nextb=(Button) findViewById(R.id.nextb);

        mauth=FirebaseAuth.getInstance();


        viewPager.setAdapter(sliderAdapter);

        adddotsindicator(0);

        viewPager.addOnPageChangeListener(viewlistener);

    }

    public void adddotsindicator(int position){

        dots=new TextView[3];
        dotslayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(onboardfragment.this);
            dots[i].setPadding(10,10,10,10);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white));
            dotslayout.addView(dots[i]);
        }

        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
        }



    }

    ViewPager.OnPageChangeListener viewlistener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            adddotsindicator(i);
            if (i==2){
                nextb.setVisibility(View.VISIBLE);
            }
            else {nextb.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId()==nextb.getId()){
            if (mauth.getCurrentUser()!=null){
                startActivity(new Intent(onboardfragment.this,UserActivity.class));
                finish();
            }else {
                startActivity(new Intent(onboardfragment.this, PhoneLogin.class));
                finish();
            }
        }

    }
}
