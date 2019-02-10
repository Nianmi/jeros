package com.example.jeros;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
    Button selectContact, testButton2, testButton3, testButton4;
    String[] contactNamesUse;
    boolean[] checkedItems;
    ArrayList<ArrayList<String>> contacts;
    ArrayList<String> contactsNames = new ArrayList<String>();
    ArrayList<String> outputNames = new ArrayList<>();
    ArrayList<Integer> outputIDs = new ArrayList<>();
    ArrayList<ContactsClass> listContacts = new ArrayList<>();

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
        selectContact = (Button) findViewById(R.id.button1);
        testButton2 = (Button) findViewById(R.id.button2);
        testButton3 = (Button) findViewById(R.id.button3);
        testButton4 = (Button) findViewById(R.id.button4);

        selectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContactFcn();
            }
        });
        testButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFinction2();
            }
        });
        testButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFinction3();
            }
        });
        testButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFinction4();
            }
        });

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

        checkedItems = new boolean[listContacts.size()];

        contactNamesUse = new String[listContacts.size()];

        for(int i =0; i< listContacts.size(); i++)
        {
            contactNamesUse[i] = listContacts.get(i).getName();
        }

    }

    public void selectContactFcn(){
        try{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Choose Contacts...");

            mBuilder.setMultiChoiceItems(contactNamesUse, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if (isChecked) {
                        outputIDs.add(position);
                    } else {
                        outputIDs.remove((Integer.valueOf(position)));
                    }
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    for (int i = 0; i < outputIDs.size(); i++) {
                        outputNames.add(contactNamesUse[outputIDs.get(i)]);
                        Log.i("debug", "Name:" + contactNamesUse[outputIDs.get(i)]);
                        Log.i("debug", "ID:" + outputIDs.get(i));
                        //String number = getContactNumber(outputIDs.get(i) - 1);  //getCOntactNumber funktioniert nicht
                        //Log.i("debug", "Nummer:" + number);
                    }
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    public void testFinction2(){
        for (ContactsClass conti : listContacts) {
            Log.i("Id and Name: ", conti.getId() + conti.getName());
        }
    }
    public void testFinction3(){
        Toast.makeText(getApplicationContext(), "testFunction3", Toast.LENGTH_LONG).show();
    }
    public void testFinction4(){
        Toast.makeText(getApplicationContext(), "testFunction4", Toast.LENGTH_LONG).show();
    }

    public void createContactsList(){
        try{
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor phoneCursor = resolver.query(    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                                        new String[]{id}, null);

                ContactsClass contactsClassTemp = new ContactsClass(id);

                contactsClassTemp.setName(name);
                //contactsClassTemp.setPhoneNumber1();

                listContacts.add(contactsClassTemp);
            }
        }
        catch (Exception e){
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erlaubnis zum Kontaktläsa bruch i", Toast.LENGTH_LONG).show();
            }
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

    public void debugContacts() {
        try {

            ContentResolver resolver = getContentResolver();
            ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();

            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // Für ID + Namensausgabe
                //Log.i("name", id + " = " + name);

                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                /* //Für Handy NUmmer Ausgabe
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

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id}, null);

            phoneCursor.moveToFirst();
            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.i("name", id + " = " + name);
            Log.i("number:", phoneNumber);

            //ArrayList check
            for(int i = 0; i < contacts.size(); i++)
            {
                Log.i("id", contacts.get(i).get(0));
                Log.i("name", contacts.get(i).get(1));
            }

        } catch (Exception e) {
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erlaubnis zum Kontaktläsa bruch i", Toast.LENGTH_LONG).show();
            }
        }


    }
}
