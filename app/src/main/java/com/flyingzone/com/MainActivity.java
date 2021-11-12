package com.flyingzone.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    LinearLayout allList,form,myJoinersLl,contactLayout,listCard,searchLayout;
    ImageView singOut;
    TextView userName;
    CardView myJoinersCard;

    private AdView adView,adView2;
    AdRequest adRequest,adRequest3;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allList=findViewById(R.id.allList);
        listCard=findViewById(R.id.listCard);
        myJoinersCard=findViewById(R.id.myJoinersCard);
        myJoinersLl=findViewById(R.id.myJoinersLl);
        searchLayout=findViewById(R.id.searchLayout);
        form=findViewById(R.id.form);
        singOut=findViewById(R.id.singOut);
        userName=findViewById(R.id.userName);
        adView = findViewById(R.id.adView);
        contactLayout=findViewById(R.id.contactLayout);
        SharePreferDb sharePreferDb=new SharePreferDb(MainActivity.this);
        HashMap<String,String> userDetails=sharePreferDb.getUserDetails();
        String emailUser=userDetails.get(SharePreferDb.KEY_EMAIL);
        userName.setText(emailUser);
        listCard.setVisibility(View.GONE);

        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
        if (emailUser.equalsIgnoreCase("flyingzone30@gmail.com")){
            listCard.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(LoginActivity.userEmail);
        allList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,ListOfCustomerActivity.class);
                startActivity(intent1);
            }
        });
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,FillDetailActivity.class);
                startActivity(intent1);
            }
        });
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,ContactDetailsActivity.class);
                startActivity(intent1);
            }
        });
        myJoinersLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,CustomerDetailsctivity.class);
                startActivity(intent1);
            }
        }); searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,SearchWithEmailActivity.class);
                startActivity(intent1);
            }
        });
        singOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AlertDialog.Builder adb1 = new AlertDialog.Builder(MainActivity.this);
                    adb1.setTitle("LogOut");
                    adb1.setMessage("Are you sure you want to LogOut ?");
                    adb1.setNegativeButton("Cancel", null);
                    adb1.setPositiveButton("Okay", (dialog, which) -> new Handler().postDelayed(() -> {
                        sharePreferDb.logOutUser();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }, 1500));
                    adb1.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
               // sharePreferDb.logOutUser();


            }
        });
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