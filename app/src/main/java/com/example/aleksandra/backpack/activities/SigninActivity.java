package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String email;
    String password;
    TextView noaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        noaccount=((TextView)findViewById(R.id.et_username));

        initialize();
        mAuth=FirebaseAuth.getInstance();

        final Button btnAddLocation = findViewById(R.id.button_signin);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=((EditText)findViewById(R.id.et_username)).getText().toString();
                password=((EditText)findViewById(R.id.et_password)).getText().toString();
                if(!email.isEmpty()&&!password.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SigninActivity.this, "sign in error   " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            } else
                                startActivity(new Intent(SigninActivity.this, GoogleMapActivity.class));
                            finish();
                        }
                    });
                }

            }
        });
        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent mainIntent = new Intent(SigninActivity.this,LoginActivity.class);
                //startActivity(mainIntent);
                //finish();
            }
        });

    }

    public void initialize(){
        //Button buttonSignin = (Button) findViewById(R.id.button_signin);

        TextView textViewNoAccount = findViewById(R.id.tw_noaccount);
        textViewNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
