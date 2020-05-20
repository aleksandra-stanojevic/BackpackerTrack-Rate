package com.example.aleksandra.backpack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.PeopleGoingToEventAdapter;
import com.example.aleksandra.backpack.adapters.RankAdapter;
import com.example.aleksandra.backpack.Models.PersonModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    private List<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        testData();
    }

    private void testData() {
        ArrayList<User> users=FirebaseAccess.getInstance().getUsers();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getRank().compareTo(o2.getRank());
            }
        });
        for(int i=0;i< users.size();i++)
        {
            list.add(FirebaseAccess.getInstance().getUser(i));
        }
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_people);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        RankAdapter adapter = new RankAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
