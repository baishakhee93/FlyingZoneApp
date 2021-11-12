package com.flyingzone.com;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;

public class SearchWithEmailActivity extends AppCompatActivity {
    ListView listOfCutomer;
    DatabaseReference databaseReference;
    List<Customer> customerArrayList;
    ListOfCustomerAdapter listOfCustomerAdapter;
    Context context;
    TextView getTotalCount;
    EditText all;
    ImageView back, search_badge;
    private String addhar;
    private AdView adView,adView2;
    AdRequest adRequest;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_with_email_activity);
        context=this;
        listOfCutomer=findViewById(R.id.listOfCutomer);
        back=findViewById(R.id.back);
        search_badge=findViewById(R.id.search_badge);
        getTotalCount=findViewById(R.id.totalJoiner);
        all=findViewById(R.id.all);
        adView = findViewById(R.id.adView);
        //all.setText("My Joiners");
        SharePreferDb sharePreferDb=new SharePreferDb(SearchWithEmailActivity.this);
        HashMap<String,String> userDetails=sharePreferDb.getUserDetails();
        String emailUser=userDetails.get(SharePreferDb.KEY_EMAIL);

        MobileAds.initialize(this);

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        String em=all.getText().toString();
        System.out.println("sOrder.........em.........."+em);
        System.out.println("sOrder.........em.........."+all.getText().toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customerArrayList=new ArrayList<>();
        search_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("sOrder.........em.........."+em);
                if (!all.getText().toString().contentEquals("")) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Customer");
                    databaseReference.orderByChild("ueserFilled").equalTo(all.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            customerArrayList.clear();
                            if (snapshot.exists()) {
                                System.out.println("sOrder.........em......2...." + em);
                                System.out.println("sOrder.........em......all.getText().toString()...." + all.getText().toString());

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Customer customer = dataSnapshot.getValue(Customer.class);
                                    customerArrayList.add(customer);
                                }
                            }
                            listOfCustomerAdapter = new ListOfCustomerAdapter(SearchWithEmailActivity.this, customerArrayList);
                            listOfCutomer.setAdapter(listOfCustomerAdapter);
                            getTotalCount.setText("Total Joiners : " + listOfCutomer.getCount());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    all.setError("Enter Currect Email Id");

                }
            }
        });
        System.out.println("sOrder.........em......3..."+em);

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
