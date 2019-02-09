package com.example.jeros;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button SMSButtonDe, ContacsButtonDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //All Premissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        //Init GUI
        SMSButtonDe = (Button) findViewById(R.id.debugSMSButton);
        ContacsButtonDe = (Button) findViewById(R.id.debugContacs);

        SMSButtonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugSMS();
            }
        });

        ContacsButtonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugContacts();
            }
        });

        createContactsList();
    }

    public void createContactsList()
    {

    }

    public void getContactNumber(int index){

    }

    public void debugContacts() {
        try {

            ContentResolver resolver = getContentResolver();
        ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();
        ContentResolver resolver = getContentResolver();

            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //Log.i("name", id + " = " + name);

                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                /*
                while (phoneCursor.moveToNext()) {
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("number:", phoneNumber);
                }*/

                ArrayList<String> temp = new ArrayList<String>();
                temp.add(id);
                temp.add(name);
                contacts.add(temp);
            }
        //Versuch mit spezifischer ID
        int find = 12;
        cursor.moveToFirst();
        cursor.move(find - 1);
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        //Log.i("debug", "cursor ready");
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
        //Log.i("debug", "phonecursor ready");

        phoneCursor.moveToFirst();
        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        Log.i("name", id + " = " + name);
        Log.i("number:", phoneNumber);

        } catch (Exception e) {
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erlaubnis zum Kontaktläsa bruch i", Toast.LENGTH_LONG).show();
            }
        }

        //ArrayList check
        for(int i = 0; i < contacts.size(); i++)
        {
            Log.i("id", contacts.get(i).get(0));
            Log.i("name", contacts.get(i).get(1));
        }
    }

    public void debugSMS() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message using method of SmsManager object
            smsManager.sendTextMessage("0792987378",
                    null,
                    "Debug Text",
                    null,
                    null);

            Toast.makeText(this, "Message sent successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Du musch mir erlaubnis geh zum SMS schrieba du pur", Toast.LENGTH_LONG).show();
            }
            // e.printStackTrace();
        }
    }
}
