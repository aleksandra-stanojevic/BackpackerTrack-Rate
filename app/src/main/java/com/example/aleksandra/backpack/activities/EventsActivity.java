package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.CommentModel;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.EventsAdapter;
import com.example.aleksandra.backpack.Models.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<EventModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        TextView name=(TextView)findViewById(R.id.events_name);
        TextView add=(TextView)findViewById(R.id.events_address);

        name.setText(FirebaseAccess.getInstance().getPlace(PlacesActivity.pos).getPlaceName());
        add.setText(FirebaseAccess.getInstance().getPlace(PlacesActivity.pos).getPlaceLocation());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventsActivity.this, AddEventActivity.class);
                startActivityForResult(i,16);
            }
        });
        testData();
    }

    private void testData() {
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new EventsAdapter(FirebaseAccess.getInstance().getEvents());
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 16) {
            if (resultCode == RESULT_OK) {
                initViews();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
