package com.abdulaziz.gentotp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GenTOTP extends AppCompatActivity {

    TextView textTOTP;
    String mTOTP;
    Boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_totp);
        textTOTP = (TextView)findViewById(R.id.totp_text);
        visible = true;
        genOTPRegularly();
    }


    private void setTextTOTP(){
        mTOTP = Integer.toString(TOTP.genTOTP());
        if( mTOTP.length()!=5){
            for(int i=0;i<5- mTOTP.length();i++){
                mTOTP = "0" + mTOTP;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTOTP.setText(mTOTP);
                if(visible) {
                    textTOTP.setVisibility(View.INVISIBLE);
                    visible = false;
                }
                else {
                    textTOTP.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        });
    }

    private void genOTPRegularly(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setTextTOTP();
            }
        },0,1000);

    }
}
