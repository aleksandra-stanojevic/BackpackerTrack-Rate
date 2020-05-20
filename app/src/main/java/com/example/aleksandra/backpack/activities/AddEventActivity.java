package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aleksandra.backpack.Models.EventModel;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initViews();
    }

    public void initViews(){


        Button btnCancel = findViewById(R.id.bt_new_event_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnLocation = findViewById(R.id.bt_new_event_add);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText d=(EditText)findViewById(R.id.ed_add_event_comment);
                EditText t=(EditText)findViewById(R.id.ed_time);
                EditText tt=(EditText)findViewById(R.id.ed_date);
                EditText n=(EditText)findViewById(R.id.ed_name);
                Intent data = new Intent();
                if(d.getText().toString().isEmpty())
                {
                    Toast.makeText(AddEventActivity.this,"Some fields are empty",Toast.LENGTH_SHORT);
                }
                else {

                    EventModel em=new EventModel();
                    em.setDescription(n.getText().toString()+"*"+d.getText().toString());
                    em.setTime(t.getText().toString()+" "+tt.getText().toString());
                    em.setPlaceID(String.valueOf(PlacesActivity.pos));
                    em.setUserID(String.valueOf(MainActivity.temporaryUser));
                    em.eventKey=String.valueOf( FirebaseAccess.getInstance().getEvents().size());

                    User u=FirebaseAccess.getInstance().getUser(MainActivity.temporaryUser);
                    u.setRank(String.valueOf(Integer.valueOf(u.getRank())+20));
                    FirebaseAccess.getInstance().updateUser(MainActivity.temporaryUser,u);
                    FirebaseAccess.getInstance().addNewEvent(em);


                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}
