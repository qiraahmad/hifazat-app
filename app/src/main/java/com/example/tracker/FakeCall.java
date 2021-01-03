package com.example.tracker;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FakeCall extends AppCompatActivity {
    private String networkCarrier;
    private MediaPlayer ringTone;
    ImageButton answerCall;
    ImageButton rejectCall;
    TextView titleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fake_call);

        GetNetworkOperatorName();
        StartRingTone();

        answerCall = findViewById(R.id.answercall);
        rejectCall = findViewById(R.id.rejectcall);

        answerCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ringTone.stop();
            }
        });

        rejectCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ringTone.stop();
                Intent homeIntent= new Intent(FakeCall.this, MainActivity.class);
                //homeIntent.addCategory(Intent.CATEGORY_HOME);
                //homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
            }
        });

    }

    private void StartRingTone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringTone = MediaPlayer.create(getApplicationContext(), notification);
        ringTone.start();
    }


    private void GetNetworkOperatorName() {
        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        networkCarrier = tm.getNetworkOperatorName();

        titleBar = (TextView)findViewById(R.id.textView1);
        if(networkCarrier != null){
            titleBar.setText("Incoming call - " + networkCarrier);
        }else{
            titleBar.setText("Incoming call");
        }
    }

}
