package com.example.jeros;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button SMSButtonDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //All Premissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        //Init GUI
        SMSButtonDe = (Button) findViewById(R.id.debugSMSButton);

        SMSButtonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugSMS();
            }
        });
    }

    public void debugSMS(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message using method of SmsManager object
            smsManager.sendTextMessage("0792987378",
                    null,
                    "Debug Text",
                    null,
                    null);

            Toast.makeText(this, "Message sent successfully", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
            if(permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Du musch mir erlaubnis geh zum SMS schrieba du pur", Toast.LENGTH_LONG).show();
            }
            // e.printStackTrace();
        }
    }
}
