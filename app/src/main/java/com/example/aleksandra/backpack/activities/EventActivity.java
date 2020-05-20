package com.example.aleksandra.backpack.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.PlaceModel;
import com.example.aleksandra.backpack.R;

public class EventActivity extends AppCompatActivity {

    private TextView placeName, placeLocation, placePhone, placeWebPage, creatorName, creatorState, creatorComment, seeAll;
    private ImageView imgCreator, imgJoined1, imgJoined2, imgJoined3, imgPlace;
    private Button btnJoin;
    private static  boolean join = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initViews();
        setListeners();
    }

    public void initViews(){

        placeName = findViewById(R.id.tw_event_placeName);
        placeLocation = findViewById(R.id.tw_event_place_address);
        placePhone = findViewById(R.id.tw_event_place_phone);
        placeWebPage = findViewById(R.id.tw_event_place_web_page);
        creatorName = findViewById(R.id.tw_event_creator_name);
        creatorState = findViewById(R.id.tw_event_creator_state);
        creatorComment = findViewById(R.id.tw_event_comment);
        seeAll = findViewById(R.id.tw_event_see_all);
        imgCreator = findViewById(R.id.iv_event_creator_image);
        imgJoined1 = findViewById(R.id.iv_event_joined1);
        imgJoined2 = findViewById(R.id.iv_event_joined2);
        imgJoined3 = findViewById(R.id.iv_event_joined3);
        imgPlace = findViewById(R.id.iv_event_place);
        btnJoin = findViewById(R.id.bt_event_join);
    }

    public void setListeners(){
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(join) {
                    btnJoin.setBackgroundColor(getResources().getColor(R.color.blue));
                    btnJoin.setText("Joined");
                    join = false;
                } else {
                    btnJoin.setBackgroundColor(getResources().getColor(R.color.yellow));
                    btnJoin.setText(R.string.join);
                    join = true;
                }
            }
        });

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), PeopleGoingToEvent.class);
                startActivity(i);
            }
        });

        placeWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), PlacesActivity.class);
                startActivity(i);
            }
        });
    }
}
