package com.example.aleksandra.backpack.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.GalleryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    public static List<Uri> list= new ArrayList<>();
    Bitmap my_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
      //  list=new ArrayList<>();
        if (getIntent().hasExtra("position")) {

            int i = getIntent().getIntExtra("position", 0);
           for(int j=0;j< FirebaseAccess.getInstance().getImages().size();j++)
            {
                if(FirebaseAccess.getInstance().getImages().get(j).contains("slika"+i)&&!FirebaseAccess.getInstance().getImages().get(j).contains("pslika"+i))
                {
                    StorageReference storageRef=FirebaseStorage.getInstance().getReference().child(FirebaseAccess.getInstance().getImages().get(j));
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(!list.contains(uri)) list.add(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        }
        initViews();
    }


    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        GalleryAdapter adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
