package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.activities.GoogleMapActivity;
import com.example.aleksandra.backpack.activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class getImagesAsyn extends AsyncTask<Context, Void,Integer>  {
int p=0;
int q=0;
String error;
    @Override
    protected Integer doInBackground(final Context... voids) {


        for(int i = 0; i< FirebaseAccess.getInstance().getUsers().size(); i++) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("slika" + 0/*+".jpg"*/);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    p++;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    error=exception.getMessage().toString();
                    q++;
                }
            });
        }
        return  1;
    }
}
