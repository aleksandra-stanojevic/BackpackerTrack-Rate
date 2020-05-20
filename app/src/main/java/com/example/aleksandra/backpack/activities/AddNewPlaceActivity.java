package com.example.aleksandra.backpack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.PlaceModel;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.GalleryAdapter;
import com.example.aleksandra.backpack.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddNewPlaceActivity extends AppCompatActivity {
    private List<Uri> list = new ArrayList<>();
    private Uri captureImageUri;
    private static final int RESULT_LOAD_IMAGE = 777;
    private GalleryAdapter adapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference mountainsRef;
    StorageReference mountainImagesRef;
    Bitmap my_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        final Button btnAddLocation = findViewById(R.id.bt_new_place_location);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(AddNewPlaceActivity.this, MainGoogleMapActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        String lat = "", lon = "";
        PlaceModel pm = new PlaceModel();
        if (getIntent().hasExtra("latitude")) {
            lat = getIntent().getStringExtra("latitude");
            lon = getIntent().getStringExtra("longitude");

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                if (Double.valueOf(lat) > -90 && Double.valueOf(lat) < 90 && Double.valueOf(lon) > -90 && Double.valueOf(lon) < 90)
                    addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null) {
               String address="";
               if(addresses.get(0)!=null&&addresses.get(0).getAddressLine(0)!=null){
                   address = addresses.get(0).getAddressLine(0);}
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                EditText ed_address = (EditText) findViewById(R.id.ed_place_address);
                String add = knownName + "," + address + "\n" + city + ", " + state + ", " + country;
                ed_address.setText(add);
            }

        }


        final Button btnAddNewPlace = findViewById(R.id.bt_new_place_add);
        final String lon1 = lon;
        final String lat1 = lat;
        btnAddNewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText name = (EditText) findViewById(R.id.ed_add_place_name);
                EditText comment = (EditText) findViewById(R.id.ed_add_place_comment);
                EditText adress = (EditText) findViewById(R.id.ed_place_address);
                EditText contact=(EditText)findViewById(R.id.ed_contact_info);
                if (!name.getText().toString().isEmpty() && !comment.getText().toString().isEmpty() && !adress.getText().toString().isEmpty()&&!contact.getText().toString().isEmpty()) {
                    PlaceModel pm = new PlaceModel();

                    pm.setPlaceName(name.getText().toString());
                    pm.setPlaceLocation(adress.getText().toString());
                    pm.setComment(comment.getText().toString());
                    pm.setPlacePhone(contact.getText().toString());
                    pm.setLongitute(lon1);
                    pm.setLatitude(lat1);
                    pm.setUserId(String.valueOf(MainActivity.temporaryUser));
                    if (list.size() != 0) {

                        User u=FirebaseAccess.getInstance().getUser(MainActivity.temporaryUser);
                        u.setRank(String.valueOf(Integer.valueOf(u.getRank())+10));
                        FirebaseAccess.getInstance().updateUser(MainActivity.temporaryUser,u);
                        FirebaseAccess.getInstance().addNewPlace(pm);
                        Toast.makeText(AddNewPlaceActivity.this, "New place added!!!!", Toast.LENGTH_SHORT);
                        try {
                            uploadImage();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent mainIntent = new Intent(AddNewPlaceActivity.this, GoogleMapActivity.class);
                        startActivity(mainIntent);

                    } else {
                        Toast.makeText(AddNewPlaceActivity.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT);
                    }
                }
                else
                {
                    Toast.makeText(AddNewPlaceActivity.this, "You must add at least one picture!!!!", Toast.LENGTH_SHORT);

                }
            }

        });
            initViews();
        }



    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);




        final Button btnCancel = findViewById(R.id.bt_new_place_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnPicture = findViewById(R.id.bt_new_place_pictures);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(btnCancel);
                getPickImageIntent(list.size());
            }
        });
    }

    public void closeKeyboard(View v) {                                                                  //view should be a button or radiobutton or checkbox or layout
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert mgr != null;
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void getPickImageIntent(int i) {
        // start camera or gallery
        captureImageUri = uploadingImageSetup(i);
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
        Intent pickerIntent = new Intent();
        pickerIntent.setType("image/*");
        pickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        // chooser intent
        Intent chooserIntent = Intent.createChooser(captureIntent, this.getString(R.string.title_image_picker));
        // add camera intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{pickerIntent});
        this.startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
    }

    public Uri uploadingImageSetup(int i) {
        final String path = android.os.Environment.DIRECTORY_DCIM;
        String fname = "slika" + FirebaseAccess.getInstance().getPlaces().size() + "_" + i+ ".jpg";
        File photo = new File(Environment.getExternalStorageDirectory(), path + fname);
        return Uri.fromFile(photo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri;
            if (data == null || MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction())) {
                imageUri = captureImageUri;
            } else {
                imageUri = data.getData();
            }
            list.add(imageUri);
            adapter.notifyDataSetChanged();
        }
    }

    public void uploadImage() throws FileNotFoundException {
        for (int i = 0; i < list.size(); i++) {
            mountainsRef = storageReference.child("slika" + (FirebaseAccess.getInstance().getPlaces().size()-1) + "_" + i + ".jpg");
            mountainImagesRef = storageReference.child(uploadingImageSetup(i).toString());
            FirebaseAccess.getInstance().addNewImage("slika" + (FirebaseAccess.getInstance().getPlaces().size()-1) + "_" + i + ".jpg");
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());
            InputStream image_stream = getContentResolver().openInputStream(list.get(i));
            Bitmap bitmap= BitmapFactory.decodeStream(image_stream );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(AddNewPlaceActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // ...
                    Toast.makeText(AddNewPlaceActivity.this, "uploaded", Toast.LENGTH_SHORT);

                }
            });

        }
    }
}
