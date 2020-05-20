package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.CommentModel;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.PersonModel;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.CommentsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.CalendarContract.CalendarCache.URI;

public class PlacesActivity extends AppCompatActivity {

    private CommentsAdapter adapter;
    private List<CommentModel> commentsList = new ArrayList<>();
    Bitmap my_image;
    FirebaseStorage storage;
    public static int pos;
    public  static  ArrayList<Uri> list1=new ArrayList<Uri>();
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);



        if(getIntent().hasExtra("position"))
        {
            int i=getIntent().getIntExtra("position",0);
            pos=i;

            int pom=0;
            for(int j=0;j<FirebaseAccess.getInstance().getEvents().size();j++)
                if(pos==Integer.valueOf(FirebaseAccess.getInstance().getEvent(j).getPlaceID())) pom++;

            TextView bk=(TextView) findViewById(R.id.tw_backpack);
            bk.setText("backpack: "+String.valueOf(pom));
            TextView placeName=(TextView) findViewById(R.id.tw_place_name);
            placeName.setText(FirebaseAccess.getInstance().getPlace(i).placeName);

            TextView placeAddress=(TextView) findViewById(R.id.tw_place_address);
            placeAddress.setText(FirebaseAccess.getInstance().getPlace(i).getPlaceLocation());

            TextView placePhone=(TextView) findViewById(R.id.tw_place_phone);
            placePhone.setText(FirebaseAccess.getInstance().getPlace(i).getPlacePhone());

            TextView creatorText=(TextView) findViewById(R.id.creator_text);
            creatorText.setText(FirebaseAccess.getInstance().getPlace(i).getComment());

            TextView nameText=(TextView) findViewById(R.id.creator_name);
       //     nameText.setText(FirebaseAccess.getInstance().getUsers().get(Integer.valueOf(FirebaseAccess.getInstance().getPlace(i).getUserId())).getFirst_name());

            final ImageView imageView=(ImageView) findViewById(R.id.main_place_image);
            final ImageView imageView1=(ImageView) findViewById(R.id.iw_1);
            final ImageView imageView2=(ImageView) findViewById(R.id.iw_2);
            final ImageView imageView3=(ImageView) findViewById(R.id.iw_3);

            StorageReference storageRef=FirebaseStorage.getInstance().getReference().child("slika"+i+"_"+0+".jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
                    Glide.with(getApplicationContext()).load(uri.toString()).into(imageView1);
//                    list1.add(uri);
  //                  list1.add(uri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
             storageRef=FirebaseStorage.getInstance().getReference().child("slika"+i+"_"+1+".jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(getApplicationContext()).load(uri.toString()).into(imageView2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            storageRef=FirebaseStorage.getInstance().getReference().child("slika"+i+"_"+2+".jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri.toString()).into(imageView3);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        initViews();
    }

    private void testData() {



        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 16) {
            if (resultCode == RESULT_OK) {
                CommentModel cm2 = new CommentModel(data.getStringExtra("comment"),String.valueOf(MainActivity.temporaryUser), String.valueOf(pos));
                User u=FirebaseAccess.getInstance().getUser(MainActivity.temporaryUser);
                u.setRank(String.valueOf(Integer.valueOf(u.getRank())+5));
                FirebaseAccess.getInstance().updateUser(MainActivity.temporaryUser,u);
                FirebaseAccess.getInstance().addNewComment(cm2);
                Toast.makeText(PlacesActivity.this, "+ 5 points",Toast.LENGTH_SHORT);
                adapter.notifyDataSetChanged();
                initViews();
            }
        }
    }

    private void initViews() {
        commentsList = new ArrayList<>();
        for(int i=0;i<FirebaseAccess.getInstance().getComments().size();i++)
            if(Integer.valueOf(FirebaseAccess.getInstance().getComments().get(i).getIdPlace())==pos)
            {
                commentsList.add(FirebaseAccess.getInstance().getComments().get(i));
            }
        RecyclerView recyclerView = findViewById(R.id.recycler_view_comments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new CommentsAdapter(commentsList);
        recyclerView.setAdapter(adapter);

        ImageView add = findViewById(R.id.iw_places_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), NewCommentActivity.class);
                startActivityForResult(i, 16);
            }
        });

        Button btn = findViewById(R.id.bt_gallery);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(PlacesActivity.this, GalleryActivity.class);
                ii.putExtra("position", pos);
                startActivity(ii);
            }
        });

        Button btn2 = findViewById(R.id.bt_events);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), EventsActivity.class);
                startActivity(i);
            }
        });
    }

    public void showComments(List<CommentModel> comments) {
        this.commentsList = comments;
        initViews();
    }
}
