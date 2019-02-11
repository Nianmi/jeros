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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    Button SMSButtonDe, ContacsButtonDe;
    Button selectContact, testButton2, testButton3, testButton4;
    String[] contactNamesUse;
    boolean[] checkedItems;

    ArrayList<Integer> outputIDs = new ArrayList<>();
    ArrayList<ContactsClass> listContacts = new ArrayList<>();
    ArrayList<ContactsClass> listSelectedContacts = new ArrayList<>();
    ArrayList<ContactsClass> listShuffledContacts = new ArrayList<>();

    //views
    ScrollView scrollView;
    LinearLayout linearLayout;

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

        // Find the ScrollView
          scrollView = (ScrollView) findViewById(R.id.scrollView1);
        // Create a LinearLayout element
        linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

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
                startGame();
            }
        });

        SMSButtonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ContacsButtonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    scrollView.removeAllViews();
                    linearLayout.removeAllViews();
                    listSelectedContacts.clear();
                    for (int i = 0; i < outputIDs.size(); i++) {
                        Log.i("debug", "Name:" + listContacts.get(outputIDs.get(i)).getName());
                        Log.i("debug", "ID:" + listContacts.get(outputIDs.get(i)).getId());
                        Log.i("debug", "Nummer:" + listContacts.get(outputIDs.get(i)).getPhoneNumber1());
                        listSelectedContacts.add(listContacts.get(outputIDs.get(i)));
                        addTextView(listContacts.get(outputIDs.get(i)).getName(),  listContacts.get(outputIDs.get(i)).getPhoneNumber1());
                    }
                    // Add the LinearLayout element to the ScrollView
                    scrollView.addView(linearLayout);
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void addTextView(String text, String phoneNumber){
        EditText editText = new EditText(getApplicationContext());
        editText.setKeyListener(null);
        editText.setText(text + "\n" + phoneNumber);

        linearLayout.addView(editText);
    }

    public void testFinction2(){
        for (ContactsClass conti : listContacts) {
            Log.i("Id and Name: ", conti.getId() + conti.getName());
        }
    }
    public void testFinction3(){
        Toast.makeText(getApplicationContext(), "testFunction3", Toast.LENGTH_LONG).show();
    }
    public void startGame(){
        if(listSelectedContacts.size() < 3){
            Toast.makeText(this, "You need at least 3 players", Toast.LENGTH_LONG).show();
        }
        else{
            //Log.i("SizeCheck", ""+listShuffledContacts.size());
            listShuffledContacts.clear();
            //Log.i("SizeCheck", ""+listShuffledContacts.size());
            listShuffledContacts.addAll(listSelectedContacts);
            //Log.i("SizeCheck", ""+listShuffledContacts.size());
            Collections.shuffle(listShuffledContacts);
            for(int i = 0; i< listShuffledContacts.size(); i++)
            {
                if(i == 0)
                {
                    sendSmS(listShuffledContacts.get(i).getName(),listShuffledContacts.get(listShuffledContacts.size()-1).getPhoneNumber1());
                    //Log.i("SMS: ", "1 Sending: " + listShuffledContacts.get(i).getName()+ " to " + listShuffledContacts.get(listShuffledContacts.size()-1).getName());
                }
                else{
                    sendSmS(listShuffledContacts.get(i).getName(),listShuffledContacts.get(i-1).getPhoneNumber1());
                    //Log.i("SMS: ", "2 Sending: " + listShuffledContacts.get(i).getName()+ " to " + listShuffledContacts.get(i-1).getName());
                }
            }
        }
    }

    public void createContactsList(){
        try{
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                Cursor phones = resolver.query(    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                                        new String[]{id}, null);

                String phoneNumber = "";

                while (phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            // do something with the Home number here...
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            phoneNumber = number;
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            // do something with the Work number here...
                            break;
                    }
                }
                ContactsClass contactsClassTemp = new ContactsClass(id);
                contactsClassTemp.setName(name);
                contactsClassTemp.setPhoneNumber1(phoneNumber);

                listContacts.add(contactsClassTemp);
            }
            //macht das ganza langsam
            if (listContacts.size() > 0) {
                Collections.sort(listContacts, new Comparator<ContactsClass>() {
                    @Override
                    public int compare(final ContactsClass object1, final ContactsClass object2) {
                        return object1.getName().compareTo(object2.getName());
                    }
                });
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

    public void sendSmS(String messageText, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message using method of SmsManager object
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    messageText,
                    null,
                    null);

            //Toast.makeText(this, "Message sent successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Du musch mir erlaubnis geh zum SMS schrieba du pur", Toast.LENGTH_LONG).show();
            }
        }
    }
}
