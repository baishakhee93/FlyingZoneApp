package com.flyingzone.com;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FillDetailActivity extends AppCompatActivity {
    EditText username,userdob,useremail,userphone,useraddress,useraddhar,userpencard;
    RadioButton usermale,userfemale;
    Button sendDatabtn;
    ImageView back;
    private RadioGroup radioGroup;
    private BroadcastReceiver broadcastReceiver = null;
    String ueserFiled;
    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    Customer customer;
    final int[] mDay = new int[1];
    final int[] mMonth = new int[1];
    final int[] mYear = new int[1];
    private AdView adView,adView2;
    AdRequest adRequest,adRequest2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_person_details);
        // initializing our edittext and button
        username = findViewById(R.id.name);
        userdob = findViewById(R.id.dob);
        useremail = findViewById(R.id.email);
        userphone = findViewById(R.id.mobileNumber);
        useraddress = findViewById(R.id.address);
        useraddhar = findViewById(R.id.addharNumber);
        userpencard = findViewById(R.id.penCard);
        adView = findViewById(R.id.adView);
      //  usermale = findViewById(R.id.maleRadioButton);
      //  userfemale = findViewById(R.id.femaleRadioButton);
        sendDatabtn = findViewById(R.id.sendButton);
        radioGroup = findViewById(R.id.genderRadioGroup);
        back = findViewById(R.id.back);
        radioGroup.clearCheck();
        broadcastReceiver = new MyReceiver();
        SharePreferDb sharePreferDb=new SharePreferDb(FillDetailActivity.this);
        HashMap<String,String> userDetails=sharePreferDb.getUserDetails();
        String emailUser=userDetails.get(SharePreferDb.KEY_EMAIL);
        ueserFiled=emailUser;
        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Customer");

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                                userfemale
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });

        customer = new Customer();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("sOrder....send.....");

                // getting text from our edittext fields.
                String name = username.getText().toString();
                String dob = userdob.getText().toString();
                String email = useremail.getText().toString();
                String phone = userphone.getText().toString();
                String addhar = useraddhar.getText().toString();
                String pencard = userpencard.getText().toString();
                String address = useraddress.getText().toString();
                String gender ="";
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
                Calendar calendar = Calendar.getInstance();
                String ceatedDate = simpleDateFormat.format(calendar.getTime());
                // below line is for checking weather the
                // edittext fields are empty or not.
                if (username.getText().toString().trim().equalsIgnoreCase("")) {
                    username.setError("Please Enter Name");
                }
                else if (userdob.getText().toString().trim().equalsIgnoreCase("")) {
                    userdob.setError("Please Enter DOB");
                    // if the text fields are empty
                    // then show the below message.
                }else if (useremail.getText().toString().trim().equalsIgnoreCase("")) {
                    useremail.setError("Please Enter Email.");

                    // if the text fields are empty
                    // then show the below message.
                  //  Toast.makeText(FillDetailActivity.this, "Please Enter Email.", Toast.LENGTH_SHORT).show();
                }
                else if (userphone.getText().toString().trim().equalsIgnoreCase("")){
               //     Toast.makeText(FillDetailActivity.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
                    userphone.setError("Please Enter Phone Number.");

                }
                else if (useraddhar.getText().toString().trim().equalsIgnoreCase("")) {
                    useraddhar.setError("Please Enter Phone Number.");

                    // if the text fields are empty
                    // then show the below message.
                  //  Toast.makeText(FillDetailActivity.this, "Please Enter Addhar Card Number.", Toast.LENGTH_SHORT).show();
                } else if (useraddress.getText().toString().trim().equalsIgnoreCase("")) {
                    useraddress.setError("Please Enter Phone Number.");

                    // if the text fields are empty
                    // then show the below message.
                   // Toast.makeText(FillDetailActivity.this, "Please Enter Address.", Toast.LENGTH_SHORT).show();
                }else if (userfemale.getText().toString().trim().equalsIgnoreCase("")){
                   // Toast.makeText(FillDetailActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                    useraddress.setError("Please Select Gender");

                }
                else {
                    databaseReference.orderByChild("ueserFilled").equalTo(emailUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                System.out.println("sOrder....data ceatedDate....." + ceatedDate);
                                registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                                // else call the method to add
                                // data to our database.
                                addDatatoFirebase(name, phone, address, dob, email, addhar, pencard, userfemale.getText().toString(), ceatedDate, ueserFiled);
                            }
                            else {
                           //     Toast.makeText(FillDetailActivity.this, "You Are Not Member Of FlyingZone", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FillDetailActivity.this);
                                alertDialogBuilder.setMessage("Data not added , You Are Not Member Of FlyingZone, For that First you need to contact with Any Member of FlyingZone");
                                alertDialogBuilder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                finish();

                                            }
                                        });


                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        userdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (userdob.getText() != null && userdob.getText().toString().trim().length() != 0) {
                        String parts[] = userdob.getText().toString().split("-");
                        int i = 1;
                        mDay[0] = Integer.parseInt(parts[2]);
                        mMonth[0] = Integer.parseInt(parts[1]) - i;
                        mYear[0] = Integer.parseInt(parts[0]);
                    } else {
                        final Calendar c = Calendar.getInstance();
                        mDay[0] = c.get(Calendar.DAY_OF_MONTH);
                        mMonth[0] = c.get(Calendar.MONTH);
                        mYear[0] = c.get(Calendar.YEAR);
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(FillDetailActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    int month= monthOfYear+1;
                                    String fm=""+month;
                                    String fd=""+dayOfMonth;
                                    if(month<10){
                                        fm ="0"+month;
                                    }
                                    if (dayOfMonth<10){
                                        fd="0"+dayOfMonth;
                                    }
                                    String date= ""+fd+"-"+fm+"-"+year;
                                    userdob.setText(date);

                                }
                            }, mDay[0], mMonth[0], mYear[0]);
                    datePickerDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void addDatatoFirebase(String name, String phone, String address,String dob,String email,String addhar,String pencard,String gender,String ceatedDate,String ueserFiled) {
        // below 3 lines of code is used to set

        System.out.println("sOrder....data ....."+name);

        // data in our object class.
        customer.setName(name);
        customer.setEmail(email);
        customer.setDob(dob);
        customer.setAddhar(addhar);
        customer.setPen(pencard);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCeatedDate(ceatedDate);
        customer.setGender(gender);
        customer.setUeserFilled(ueserFiled);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(addhar).setValue(customer);
                System.out.println("sOrder....data added....."+customer);
                Intent intent1=new Intent(FillDetailActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
                // after adding this data we are showing toast message.
                Toast.makeText(FillDetailActivity.this, "Data added Successfully", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("sOrder....data error....."+error);

                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(FillDetailActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
