package com.example.aleksandra.backpack.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;

import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public static int temporaryUser=-1;
    static User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        int SPLASH_DISPLAY_LENGTH = 4000;

        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    //provera da li postoji user, zatim da li je ulogvan ili nije
                    if(mAuth.getCurrentUser()!=null)
                    {

                        Intent mainIntent = new Intent(MainActivity.this,GoogleMapActivity.class);
                        startActivity(mainIntent);
                        finish();

                   }
                    else
                    {

                        Intent mainIntent = new Intent(MainActivity.this,SigninActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);


    }
}
