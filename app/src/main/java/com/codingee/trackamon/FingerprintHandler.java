package com.codingee.trackamon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.hardware.fingerprint.FingerprintManager.*;

public class FingerprintHandler extends AuthenticationCallback {


    private Context context;

    public FingerprintHandler(Context context) {
        this.context=context;
    }

    public void startAuth(FingerprintManager fingerprintManager,FingerprintManager.CryptoObject cryptoObject){

        android.os.CancellationSignal cancellationSignal= new android.os.CancellationSignal();

        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        this.update("There was an auth error",false);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        this.update("There was an auth failure",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        this.update("Error: "+helpString,false);
    }

    @Override
    public void onAuthenticationSucceeded(AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        this.update("You can now access the app",true);
        if (((Activity)context).getSharedPreferences("Firstrun",Context.MODE_PRIVATE).getBoolean("locked",true)) {
            ((Activity) context).getSharedPreferences("Firstrun", Context.MODE_PRIVATE).edit().putBoolean("locked", false).apply();
            ((Activity)context).startActivity(new Intent(context,UserActivity.class));
            Toast.makeText(context,"Unlocked",Toast.LENGTH_LONG).show();
            ((Activity)context).finish();
        }

    }



    private void update(String s, boolean b) {


        TextView fptext=(TextView) ((Activity)context).findViewById(R.id.fingerprinttext);
        ImageView fpimg=(ImageView)((Activity)context).findViewById(R.id.fingerprintimage);
        fptext.setText(s);

        if (b==false){
            fptext.setTextColor(Color.RED);
            fpimg.setImageResource(R.drawable.cross);
        }
        else {

            fpimg.setImageResource(R.drawable.check);
            fptext.setTextColor(Color.GREEN);
        }


    }
}
