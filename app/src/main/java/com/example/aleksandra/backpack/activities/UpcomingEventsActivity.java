package com.example.aleksandra.backpack.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.EventModel;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.EventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<EventModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

    }
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_upc_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new EventsAdapter(FirebaseAccess.getInstance().getEvents());
        recyclerView.setAdapter(adapter);
    }

}
