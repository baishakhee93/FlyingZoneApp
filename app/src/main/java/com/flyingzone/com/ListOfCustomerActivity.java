package com.flyingzone.com;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfCustomerActivity extends AppCompatActivity {
    ListView listOfCutomer;
    DatabaseReference databaseReference;
    List<Customer> customerArrayList;
    ListOfCustomerAdapter listOfCustomerAdapter;
    Context context;
    TextView getTotalCount;
    ImageView back;
    private AdView adView,adView2;
    AdRequest adRequest;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.list_of_customer_activity);
        listOfCutomer=findViewById(R.id.listOfCutomer);
        back=findViewById(R.id.back);
        adView = findViewById(R.id.adView);
        getTotalCount=findViewById(R.id.totalJoiner);


        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customerArrayList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Customer customer=dataSnapshot.getValue(Customer.class);
                    customerArrayList.add(customer);
                }
                listOfCustomerAdapter=new ListOfCustomerAdapter(ListOfCustomerActivity.this,customerArrayList);
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
