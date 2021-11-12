package com.flyingzone.com;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDetailsctivity  extends AppCompatActivity {
    ListView listOfCutomer;
    DatabaseReference databaseReference;
    List<Customer> customerArrayList;
    ListOfCustomerAdapter listOfCustomerAdapter;
    Context context;
    TextView getTotalCount,all;
    ImageView back;
    private String addhar;
    private AdView adView,adView2;
    AdRequest adRequest,adRequest3;
    InterstitialAd mInterstitialAd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_customer_activity);
        context=this;
        setContentView(R.layout.list_of_customer_activity);
        listOfCutomer=findViewById(R.id.listOfCutomer);
        back=findViewById(R.id.back);
        getTotalCount=findViewById(R.id.totalJoiner);
        all=findViewById(R.id.all);
        adView = findViewById(R.id.adView);
        all.setText("My Joiners");
        SharePreferDb sharePreferDb=new SharePreferDb(CustomerDetailsctivity.this);
        HashMap<String,String> userDetails=sharePreferDb.getUserDetails();
        String emailUser=userDetails.get(SharePreferDb.KEY_EMAIL);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customerArrayList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Customer");
        databaseReference.orderByChild("ueserFilled").equalTo(emailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerArrayList.clear();
                if(snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Customer customer = dataSnapshot.getValue(Customer.class);
                        customerArrayList.add(customer);
                    }
                }
                listOfCustomerAdapter=new ListOfCustomerAdapter(CustomerDetailsctivity.this,customerArrayList);
                listOfCutomer.setAdapter(listOfCustomerAdapter);
                getTotalCount.setText("Total Joiners : "+listOfCutomer.getCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listOfCustomerAdapter=new ListOfCustomerAdapter(context,customerArrayList);
        listOfCutomer.setAdapter(listOfCustomerAdapter);
        getTotalCount.setText("Total Joiners : "+listOfCutomer.getCount());
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
