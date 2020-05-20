package com.example.aleksandra.backpack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference mountainsRef;
    StorageReference mountainImagesRef;
    private Uri captureImageUri;
    private static final int RESULT_LOAD_IMAGE = 777;
    private ImageView circularImageView;
    private ImageView smallView;
    private FirebaseAuth mAuth;


    String first_name;
    String last_name;
    String password;
    String comfirm;
    String ggender;
    String mgender;
    String email;
    String username;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        initialize();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mountainsRef = storageReference.child("slika" + String.valueOf(FirebaseAccess.getInstance().getUsers().size()));
        mountainImagesRef = storageReference.child(uploadingImageSetup().toString());

        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());

        final Button btnAddLocation = findViewById(R.id.btn_login);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name = ((EditText) findViewById(R.id.ed_fname)).getText().toString();
                last_name = ((EditText) findViewById(R.id.ed_lname)).getText().toString();
                phone = ((EditText) findViewById(R.id.ed_phone)).getText().toString();

                password = ((EditText) findViewById(R.id.ed_password)).getText().toString();
                comfirm = ((EditText) findViewById(R.id.ed_comfirm)).getText().toString();
                ggender = ((RadioButton) findViewById(R.id.radio_button_female)).getText().toString();
                mgender = ((RadioButton) findViewById(R.id.radio_button_male)).getText().toString();
                email = ((EditText) findViewById(R.id.ed_email)).getText().toString();
                username = ((EditText) findViewById(R.id.ed_uname)).getText().toString();

                ArrayList<User> users = FirebaseAccess.getInstance().getUsers();
                int check_unique_username = 0;
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().compareTo(username) == 0)
                        check_unique_username = 1;
                }
                if (first_name.isEmpty() || phone.isEmpty() || last_name.isEmpty() || password.isEmpty() || comfirm.isEmpty() || email.isEmpty() || username.isEmpty() || (ggender.isEmpty() && mgender.isEmpty())) {
                    Toast.makeText(LoginActivity.this, "Some fields are empty", Toast.LENGTH_SHORT);
                } else if (password.compareTo(comfirm) != 0) {
                    Toast.makeText(LoginActivity.this, "Please comfirm password", Toast.LENGTH_SHORT);

                } else if (check_unique_username == 1) {
                    Toast.makeText(LoginActivity.this, "Username already excists", Toast.LENGTH_SHORT);

                } else {
                    User u = new User();
                    u.setFirst_name(first_name);
                    u.setLast_name(last_name);
                    u.setPhone(phone);
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setEmail(email);
                    u.setGender(ggender.isEmpty() ? mgender : ggender);
                    u.setID(users.size());
                    u.setRank(new String("0"));
                    u.setLongitude("40");
                    u.setLatitude("20");
                    FirebaseAccess.getInstance().addNewUser(u);
                    try {
                        uploadImage();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            startActivity(new Intent(LoginActivity.this, SigninActivity.class));

                        } else {

                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(LoginActivity.this, "Failed Registation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    public void initialize() {
        circularImageView = findViewById(R.id.imw_profile_picture);
        smallView = findViewById(R.id.imw_small_picture);
        final TextView fname = findViewById(R.id.tw_fname);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(fname);
                getPickImageIntent();
            }
        });
        circularImageView.setClickable(true);
    }

    public void uploadImage() throws FileNotFoundException {
       ImageView imageView=(ImageView)findViewById(R.id.imw_profile_picture);
  /*      imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(LoginActivity.this,exception.getMessage().toString(),Toast.LENGTH_SHORT);
              }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // ...
                Toast.makeText(LoginActivity.this,"uploaded",Toast.LENGTH_SHORT);

            }
        });
*/
        mountainsRef = storageReference.child("pslika" + (FirebaseAccess.getInstance().getUsers().size()-1 ) + ".jpg");
        mountainImagesRef = storageReference.child(uploadingImageSetup().toString());
        FirebaseAccess.getInstance().addNewImage("pslika" +( FirebaseAccess.getInstance().getUsers().size()-1)+ ".jpg");
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());
        InputStream image_stream = getContentResolver().openInputStream(captureImageUri);
        Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // ...

            }
        });


    }


    public void closeKeyboard(View v) {                                                                  //view should be a button or radiobutton or checkbox or layout
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert mgr != null;
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void getPickImageIntent() {
        // start camera or gallery
        captureImageUri = uploadingImageSetup();
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
            captureImageUri=imageUri;
            Utils.loadImageWithGlideCircle(imageUri, circularImageView);
            smallView.setVisibility(View.INVISIBLE);
        }
    }

    public Uri uploadingImageSetup() {
        final String path = android.os.Environment.DIRECTORY_DCIM;
        String fname = ("profile"+(FirebaseAccess.getInstance().getUsers().size()-1) + ".jpg");
        File photo = new File(Environment.getExternalStorageDirectory(), path + fname);
        return Uri.fromFile(photo);
    }
}