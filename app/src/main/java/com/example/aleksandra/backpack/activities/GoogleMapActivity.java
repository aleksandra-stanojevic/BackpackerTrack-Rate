package com.example.aleksandra.backpack.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.aleksandra.backpack.Models.PlaceModel;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.adapters.ProfileNewPlaceAdapter;
import com.example.aleksandra.backpack.adapters.getImagesAsyn;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener{

    static boolean service_started=false;
    Uri u;
    private GoogleMap mMap;
    String provider;
    double lat;
    double lng;
    Marker marker;
    private LocationManager lm;
    Bitmap my_image;
    float RADIUS=5000;
    static Location myLocation;
    boolean Enabled=false;
    boolean FiltersEnabled;
    String FiltersString;
    ImageView img;
    String error;
    public static ArrayList<Bitmap> allUserImages=new ArrayList<Bitmap>();
    private ArrayList<User> temp = new ArrayList<User>();
    String CHANNEL_ID="1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = lm.getBestProvider(new Criteria(), true);

        //  Location location = lm.getLastKnownLocation(provider);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        for(int i=0;i<FirebaseAccess.getInstance().getImages().size();i++) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(FirebaseAccess.getInstance().getImages().get(i));
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    u = uri;
                 //   Glide.with(getApplicationContext()).load(uri.toString()).into(img);


                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            try {
                                my_image = Glide.
                                        with(GoogleMapActivity.this).
                                        load(u).
                                        asBitmap().
                                        into(-1, -1).
                                        get();
                                GoogleMapActivity.allUserImages.add(my_image);
                            } catch (final ExecutionException e) {
                            } catch (final InterruptedException e) {
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void dummy) {
                            // if (null != theBitmap)
                            {
                                // The full bitmap should be available here
                                /// image.setImageBitmap(theBitmap);*/
                            }
                            ;
                        }
                    }.execute();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        if(getIntent().hasExtra("sw1"))
        {
            RADIUS=Float.valueOf(getIntent().getStringExtra("sw1"));
            Enabled=true;


        }
        if(getIntent().hasExtra("rad"))
        {
            RADIUS=Float.valueOf(getIntent().getStringExtra("rad"));


        }
        if(getIntent().hasExtra("sw2"))
        {
            FiltersEnabled=true;
            FiltersString=  getIntent().getStringExtra("sw2");

        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GoogleMapActivity.this, AddNewPlaceActivity.class);
                startActivity(i);
            }
        });
        //searchView.setSuggestionsAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        lm.requestLocationUpdates(provider, 400, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private  static HashMap<Marker,Integer> markerUserIDMap;
    private  static HashMap<Integer,Marker> markerPlaceIDMap;


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {

            LatLng sydney = new LatLng(lat, lng);

            mMap.setMyLocationEnabled(true);
           // marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location is here...!!! "));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String a =marker.getTitle();
                    if(a.contains("user"))
                    {
                        Intent ii = new Intent(GoogleMapActivity.this, ProfileActivity.class);
                        ii.putExtra("position",a.charAt(0)-'0');
                        startActivity(ii);
                    }
                    else
                    {
                        Intent ii = new Intent(GoogleMapActivity.this, PlacesActivity.class);
                        ii.putExtra("position", Integer.valueOf(Integer.valueOf(a.charAt(0))-'0'));
                        startActivity(ii);
                    }


               //    Toast.makeText(GoogleMapActivity.this,marker.getPosition().latitude+" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            addPlaceMarkers(myLocation);
            //addUsersMarkers();

        }

    }



    ArrayList<Marker> markers=new ArrayList<Marker>();
    int currentPosition=-1;
    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        String[] elements=location.split(" ");
        List<Address> addressList;

      Marker marker=null;
        for(int i=0;i<markerPlaceIDMap.size();i++)
        {
            for(int j=0;j<elements.length;j++) {
                if (markerPlaceIDMap.get(i).getTag().toString().contains(elements[j])) {
                    marker = markerPlaceIDMap.get(i);
                    markers.add(marker);
                    currentPosition=0;
                }
            }
        }
        if(marker!=null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),1000));

        }
        else
        {
            Toast.makeText(GoogleMapActivity.this,"Not found matching object! Try again!",Toast.LENGTH_SHORT);
        }
        }
    public void onNext(View view)
    {
        if(markers.size()>currentPosition&&currentPosition>0)
        {  currentPosition++;

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(currentPosition).getPosition(),4000));
        }
    }




    public   void addUsersMarkers()
    {
        mMap.clear();
        //ovde treba skloniti
        ArrayList<User> users = FirebaseAccess.getInstance().getUsers();
        markerUserIDMap = new HashMap<Marker, Integer>((int) ((double) users.size() * 1.2));
        for (int i = 0; i < users.size(); i++) {
            User place = users.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitude();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.title(String.valueOf(i)+"user");



            int height = 80;
            int width = 80;



                if ((my_image!=null&allUserImages.size() == FirebaseAccess.getInstance().getImages().size())) {
                    int   p = FirebaseAccess.getInstance().getImages().indexOf("pslika" + i + ".jpg");
                   if(p!=-1) markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(allUserImages.get(p), 100, 100, true)));
                }

            Marker marker = mMap.addMarker(markerOptions);

            markerUserIDMap.put(marker, i);
        }
    }

    public   void addPlaceMarkers(Location myLoc)
    {


        //ovde treba skloniti
        ArrayList<PlaceModel> places = FirebaseAccess.getInstance().getPlaces();
        markerPlaceIDMap = new HashMap<Integer, Marker>((int) ((double) places.size() * 1.2));
        for (int i = 0; i < places.size(); i++) {
            PlaceModel place = places.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitute();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location));
            markerOptions.title(String.valueOf(i)+"place");
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(places.get(i).getPlaceName()+" "+places.get(i).getPlaceLocation().toString());
            Location temp = new Location(provider);
            temp.setLatitude(Double.valueOf(lat));
            temp.setLongitude(Double.valueOf(lon));
            if(Enabled)
            {

                if(myLoc!=null&&myLocation.distanceTo(temp)>RADIUS)
                   marker.setVisible(false);
                else marker.setVisible(true);
            }
            if(FiltersEnabled)
            {
                    for(int j=1;j<FiltersString.split(" ").length;j++) {
                        if (marker.getTag().toString().contains(FiltersString.split(" ")[j])) {
                            marker.setVisible(false);
                        }
                        else  marker.setVisible(true);

                }
            }
            markerPlaceIDMap.put(i,marker);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider callingf
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
         //   return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        if(MainActivity.temporaryUser==-1)
        {

            for(int i=0;i<FirebaseAccess.getInstance().getUsers().size();i++)
            {
                if(FirebaseAccess.getInstance().getUsers().get(i).getEmail().compareTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())==0)
                {
                    MainActivity.temporaryUser=i;

                }
            }

        }

        lat = location.getLatitude();
        lng = location.getLongitude();
        myLocation=location;
        myLocation.setLatitude(lat);
        myLocation.setLongitude(lng);

        if(Enabled)
        {
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lng))
                    .radius(RADIUS)
                    .strokeColor(Color.RED));

        }

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
        Marker marker = mMap.addMarker(markerOptions);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
//
     //       Toast.makeText(GoogleMapActivity.this,marker.getPosition().latitude+" "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();

        if(MainActivity.temporaryUser!=-1) {
            User u = FirebaseAccess.getInstance().getUser(MainActivity.temporaryUser);
            u.setLatitude(String.valueOf(lat));
            u.setLongitude(String.valueOf(lng));
            FirebaseAccess.getInstance().updateUser(MainActivity.temporaryUser, u.getFirst_name(), u.getLast_name(), u.getEmail(), u.getPassword(), u.getGender(), u.getUsername(), u.getLatitude(), u.getLongitude());

            addUsersMarkers();
            addPlaceMarkers(myLocation);
            getImagesAsyn gia = new getImagesAsyn();
            gia.execute(getApplicationContext());
        }
    }//
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // lm.requestLocationUpdates(provider, 400, 0, this);

    }

    @SuppressLint("MissingPermision")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
                return;
            }

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_rank) {
            Intent mainIntent = new Intent(GoogleMapActivity.this,RankActivity.class);
            startActivity(mainIntent);
            finish();
        } else if (id == R.id.nav_radius) {
            Intent mainIntent = new Intent(GoogleMapActivity.this,FiltersActivity.class);
            startActivity(mainIntent);
            finish();

        } else if (id == R.id.nav_profile) {
            Intent mainIntent = new Intent(GoogleMapActivity.this,ProfileActivity.class);
            mainIntent.putExtra("position",MainActivity.temporaryUser);
            startActivity(mainIntent);
            finish();

        } else if (id == R.id.nav_settings) {

            Intent mainIntent = new Intent(GoogleMapActivity.this,UpcomingEventsActivity.class);
            startActivity(mainIntent);
            finish();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signOut();
            Intent mainIntent = new Intent(GoogleMapActivity.this, SigninActivity.class);
            startActivity(mainIntent);
            finish();
        }
        else if (id == R.id.nav_notifications) {
            if(!service_started)
            {
                service_started=true;
                startService(new Intent(GoogleMapActivity.this, BackgroundService.class));

            }else
            {
                service_started=false;
                stopService(new Intent(GoogleMapActivity.this, BackgroundService.class));

            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
