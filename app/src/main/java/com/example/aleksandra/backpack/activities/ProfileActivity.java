package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.CommentsAdapter;
import com.example.aleksandra.backpack.adapters.ProfileCommentAdapter;
import com.example.aleksandra.backpack.adapters.ProfileEventAdapter;
import com.example.aleksandra.backpack.adapters.ProfileNewPlaceAdapter;
import com.example.aleksandra.backpack.Models.CommentModel;
import com.example.aleksandra.backpack.Models.EventModel;
import com.example.aleksandra.backpack.Models.PlaceModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private List<CommentModel> commentsList = new ArrayList<>();
    private List<EventModel> eventList = new ArrayList<>();
    private List<PlaceModel> placeList = new ArrayList<>();

    private ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView img=(ImageView)findViewById(R.id.iv_profile_image);
        TextView poens=(TextView)findViewById(R.id.tw_poens);
        TextView phone=(TextView)findViewById(R.id.tw_profile_phone);
        TextView email=(TextView)findViewById(R.id.tw_profile_email);
        TextView name=(TextView)findViewById(R.id.tw_profile_name);
        LinearLayout  l_frinds=(LinearLayout)findViewById(R.id.friends_layout);
        Button friends=(Button) findViewById(R.id.btn_friend);
        Button unfriends=(Button) findViewById(R.id.btn_unfriend);

        if(getIntent().hasExtra("position")) {
            int i = getIntent().getIntExtra("position", 0);
            if(FirebaseAccess.getInstance().getUsers().size()!=0) {
                User u = FirebaseAccess.getInstance().getUser(i);
                poens.setText(u.getRank());
                phone.setText(u.getPhone());
                email.setText(u.getEmail());
                name.setText(u.getFirst_name() + " " + u.getLast_name());
            }

            if (GoogleMapActivity.allUserImages.size() != 0) {
                int p=FirebaseAccess.getInstance().getImages().indexOf("pslika"+i+".jpg");
                img.setImageBitmap(GoogleMapActivity.allUserImages.get(p));

        }

            l_frinds.setVisibility(View.VISIBLE);
            if(true)// prijatelji su
            {
                friends.setText("FRIEND");
                friends.setEnabled(false);
                unfriends.setText("UNFRIEND");
                unfriends.setVisibility(View.VISIBLE);
            }
            else
            {
                friends.setText("SEND REQUEST");
                friends.setEnabled(true);

                unfriends.setVisibility(View.INVISIBLE);


            }
        }
        else
        {

            int i=MainActivity.temporaryUser;
            User u=FirebaseAccess.getInstance().getUser(i);
            poens.setText(u.getRank());
            phone.setText(u.getPhone());
            email.setText(u.getEmail());

            name.setText(u.getFirst_name()+" "+u.getLast_name());
            l_frinds.setVisibility(View.INVISIBLE);

        }
        testData();


    }

    public void testData() {


        for(int i=0;i<FirebaseAccess.getInstance().getPlaces().size();i++)
            if(Integer.valueOf(FirebaseAccess.getInstance().getPlace(i).getUserId())==MainActivity.temporaryUser) placeList.add(FirebaseAccess.getInstance().getPlace(i));

        for(int i=0;i<FirebaseAccess.getInstance().getComments().size();i++)
          if(Integer.valueOf(FirebaseAccess.getInstance().getPlace(i).getUserId())==MainActivity.temporaryUser)
                         commentsList.add(FirebaseAccess.getInstance().getComments().get(i));


        initViews();
    }

    private void initViews() {
        RecyclerView recyclerViewEvents = findViewById(R.id.recycler_view_profile_events);
        recyclerViewEvents.setHasFixedSize(true);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileEventAdapter adapterEvents = new ProfileEventAdapter(eventList);
        recyclerViewEvents.setAdapter(adapterEvents);

        RecyclerView recyclerViewComments = findViewById(R.id.recycler_view_profile_comments);
        recyclerViewComments.setHasFixedSize(true);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileCommentAdapter adapterComments = new ProfileCommentAdapter(commentsList);
        recyclerViewComments.setAdapter(adapterComments);

        RecyclerView recyclerViewNewPlaces = findViewById(R.id.recycler_view_profile_new_places);
        recyclerViewNewPlaces.setHasFixedSize(true);
        recyclerViewNewPlaces.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileNewPlaceAdapter adapterNewPlace = new ProfileNewPlaceAdapter(placeList);
        recyclerViewNewPlaces.setAdapter(adapterNewPlace);

    }
}
