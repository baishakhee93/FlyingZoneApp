package com.flyingzone.com;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.annotations.NotNull;

public class ContactDetailsActivity extends AppCompatActivity {
    TextView phoneNo,phoneNo2,emailID,emailID2;
    private AdView adView,adView2;
    AdRequest adRequest,adRequest3;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        phoneNo=findViewById(R.id.phoneNo);
        phoneNo2=findViewById(R.id.phoneNo2);
        emailID=findViewById(R.id.emailID);
        adView = findViewById(R.id.adView);
        // adView2 = findViewById(R.id.adView2);
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        adRequest3 = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest3);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);




        emailID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent emailIntent=new Intent(Intent.ACTION_SEND);
                    String[] recipients={"flyingzone30@gmail.com"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Want to become a FlyingZone Member");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Type here you message");
                     emailIntent.setType("text/plain");
                    emailIntent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(emailIntent, "Send mail"));
                }catch(ActivityNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(ContactDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        phoneNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ContactDetailsActivity.this, Manifest.permission.CALL_PHONE )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ContactDetailsActivity.this,new String[]{Manifest.permission.CALL_PHONE},100);
                }
                String number= "8789486091";
               System.out.println("sOrder....number......."+number);
                    Intent sIntent = new Intent(Intent.ACTION_CALL);
                    sIntent.setData(Uri.parse("tel:" + number));

                    startActivity(sIntent);
            }
        });
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ContactDetailsActivity.this, Manifest.permission.CALL_PHONE )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ContactDetailsActivity.this,new String[]{Manifest.permission.CALL_PHONE},100);
                }
                String number= "9113191504";
               System.out.println("sOrder....number......."+number);
                    Intent sIntent = new Intent(Intent.ACTION_CALL);
                    sIntent.setData(Uri.parse("tel:" + number));

                    startActivity(sIntent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String number= "9113191504";
            Intent sIntent = new Intent(Intent.ACTION_CALL);
            //  sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sIntent.setData(Uri.parse("tel:" + number));

            startActivity(sIntent);
        } else {
            Toast.makeText(ContactDetailsActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(ContactDetailsActivity.this, Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactDetailsActivity.this,new String[]{Manifest.permission.READ_CONTACTS},100);

        }else {
            String number= "9113191504";
            Intent sIntent = new Intent(Intent.ACTION_CALL);
            //  sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sIntent.setData(Uri.parse("tel:" + number));

            startActivity(sIntent);
        }
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
