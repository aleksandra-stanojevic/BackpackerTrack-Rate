package com.example.aleksandra.backpack.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.PeopleGoingToEventAdapter;
import com.example.aleksandra.backpack.Models.PersonModel;

import java.util.ArrayList;
import java.util.List;

public class PeopleGoingToEvent extends AppCompatActivity {
    private List<PersonModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_going_to_event);

        testData();
    }

    private void testData() {
        list.add(new PersonModel("Anne Smith", "England", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England","https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England","https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England","https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        list.add(new PersonModel("Anne Smith", "England","https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg","10"));
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_people_going_to_event);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        PeopleGoingToEventAdapter adapter = new PeopleGoingToEventAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
