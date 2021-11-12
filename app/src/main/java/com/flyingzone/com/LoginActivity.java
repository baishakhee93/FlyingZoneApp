package com.flyingzone.com;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private BroadcastReceiver broadcastReceiver = null;

    private static final String TAG = "LoginActivity";
    public static final String userEmail="";
    public FirebaseAuth mAuth;
    EditText emailTextInput;
    EditText passwordTextInput;
    Button signInButton;
    TextView forgotPasswordButton,regiter;
    Button sendVerifyMailAgainButton;
    private AdView adView,adView2;
    AdRequest adRequest,adRequest2;
    // TextView errorView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextInput = findViewById(R.id.signInEmailTextInput);
        passwordTextInput = findViewById(R.id.signInPasswordTextInput);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        regiter = findViewById(R.id.regiter);
        adView = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        MobileAds.initialize(this);

         adRequest = new AdRequest.Builder().build();
        adRequest2 = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView2.loadAd(adRequest2);
      /*  adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-7106981806500353/6722814428");*/
        broadcastReceiver = new MyReceiver();

        // errorView = findViewById(R.id.signInErrorView);

       // sendVerifyMailAgainButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (emailTextInput.getText().toString().contentEquals("")) {
                    emailTextInput.setError("Email cant be empty");

                   // Toast.makeText(LoginActivity.this,"Email cant be empty",Toast.LENGTH_SHORT).show();


                } else if (passwordTextInput.getText().toString().contentEquals("")) {
                   // Toast.makeText(LoginActivity.this,"Password cant be empty",Toast.LENGTH_SHORT).show();
                    passwordTextInput.setError("Password cant be empty");


                } else {

                    //registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

                    mAuth.signInWithEmailAndPassword(emailTextInput.getText().toString(), passwordTextInput.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();

                                      SharePreferDb sharePreferDb=new SharePreferDb(LoginActivity.this);
                                      sharePreferDb.createLogin(emailTextInput.getText().toString(),passwordTextInput.getText().toString());

                                        if (user != null) {
                                            System.out.println("sOrder.......user......"+user);
                                            if (user.isEmailVerified()) {
                                                System.out.println("sOrder.......Email Verified......"+user.isEmailVerified());


                                                System.out.println("Email Verified : " + user.isEmailVerified());
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra(userEmail,emailTextInput.getText().toString());

                                                setResult(RESULT_OK, null);
                                                startActivity(intent);
                                                LoginActivity.this.finish();


                                            }
                                        }

                                    } else {
                                        System.out.println("sOrder.......user......"+task.getException());

                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        if (task.getException() != null) {
                                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                }
                            });


                }


            }
        });


        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPasswordActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordActivity);
                LoginActivity.this.finish();

            }
        });
        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

            }
        });
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        if (adView2 != null) {
            adView2.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        if (adView2 != null) {
            adView2.pause();
        }
    }
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (adView2 != null) {
            adView2.pause();
        }
        super.onDestroy();
    }

}



