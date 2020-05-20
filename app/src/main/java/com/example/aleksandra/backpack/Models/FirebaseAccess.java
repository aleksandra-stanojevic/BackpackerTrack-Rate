package com.example.aleksandra.backpack.Models;

import android.app.Application;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.EventLog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseAccess {

    private ArrayList<User> Users;
    private ArrayList<PlaceModel> Places;
    private ArrayList<EventModel> Events;
    private ArrayList<CommentModel> Comments;
    private ArrayList<String> ImagesURL;

    private HashMap<String,Integer> userKeyIndexMapping;
    private HashMap<String, Integer> placeKeyIndexMapping;
    private HashMap<String, Integer> eventKeyIndexMapping;
    private HashMap<String, Integer> commentKeyIndexMapping;
    private HashMap<String, Integer> imageURLKeyIndexMapping;

    private DatabaseReference database;

    private static final String FIREBASE_USER_CHILD="user";
    private static final String FIREBASE_PLACE_CHILD="place";
    private static final String FIREBASE_EVENT_CHILD="event";
    private static final String FIREBASE_COMMMENT_CHILD="comment";
    private static final String FIREBASE_IMAGEURL_CHILD="imgurl";


    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseAccess()
    {
        Users=new ArrayList<User>();
        Places=new ArrayList<PlaceModel>();
        Events=new ArrayList<EventModel>();
        Comments=new ArrayList<CommentModel>();
        ImagesURL=new ArrayList<String>();
        placeKeyIndexMapping=new HashMap<String, Integer>();
        userKeyIndexMapping=new HashMap<String,Integer>();
        eventKeyIndexMapping=new HashMap<String,Integer>();
        commentKeyIndexMapping=new HashMap<String, Integer>();
        imageURLKeyIndexMapping=new HashMap<String, Integer>();

        database= FirebaseDatabase.getInstance().getReference();

        database.child(FIREBASE_USER_CHILD).addChildEventListener(userChildEventListener);
        database.child(FIREBASE_USER_CHILD).addListenerForSingleValueEvent(userParentEventListener);
        database.child(FIREBASE_PLACE_CHILD).addChildEventListener(placeChildEventListener);
        database.child(FIREBASE_PLACE_CHILD).addListenerForSingleValueEvent(placeParentEventListener);
        database.child(FIREBASE_EVENT_CHILD).addChildEventListener(eventChildEventListener);
        database.child(FIREBASE_EVENT_CHILD).addListenerForSingleValueEvent(eventParentEventListener);
        database.child(FIREBASE_COMMMENT_CHILD).addChildEventListener(commentChildEventListener);
        database.child(FIREBASE_COMMMENT_CHILD).addListenerForSingleValueEvent(commentParentEventListener);
        database.child(FIREBASE_IMAGEURL_CHILD).addChildEventListener(imageurlChildEventListener);
        database.child(FIREBASE_IMAGEURL_CHILD).addListenerForSingleValueEvent(imageurlParentEventListener);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    //samo jednom se izvrsava
    ValueEventListener userParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (userUpdateEventListener != null) {
                userUpdateEventListener.onUserListUpdate();
            }
            ArrayList<User> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                User note = noteDataSnapshot.getValue(User.class);
                note.UserKey = noteDataSnapshot.getKey();
                notes.add(note);
            }
            Users = notes;

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
        ValueEventListener imageurlParentEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (imageurlUpdateEventListener != null) {
                    imageurlUpdateEventListener.onImageListUpdate();
                }
                ArrayList<String> notes = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String note = noteDataSnapshot.getValue(String.class);
                    notes.add(note);
                }
                ImagesURL = notes;

            }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener commentParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(commentUpdateEventListener != null){
                commentUpdateEventListener.onCommentListUpdate();
            }
            ArrayList<CommentModel> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                CommentModel note = noteDataSnapshot.getValue(CommentModel.class);
                note.commentKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Comments=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener placeParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(placeUpdateEventListener != null){
                placeUpdateEventListener.onPlaceListUpdate();
            }
            ArrayList<PlaceModel> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                PlaceModel note = noteDataSnapshot.getValue(PlaceModel.class);
                note.placeKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Places=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener eventParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(eventUpdateEventListener != null){
                eventUpdateEventListener.onEventListUpdate();
            }
            ArrayList<EventModel> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                EventModel note = noteDataSnapshot.getValue(EventModel.class);
                note.eventKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Events=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //na svaku promenu child
    ChildEventListener userChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!userKeyIndexMapping.containsKey(ukey))
            {
                User user=dataSnapshot.getValue(User.class);
                user.UserKey=ukey;
                Users.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
                if(userUpdateEventListener != null) {
                    userUpdateEventListener.onUserListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            com.example.aleksandra.backpack.Models.User user=dataSnapshot.getValue(com.example.aleksandra.backpack.Models.User.class);
            user.UserKey=ukey;
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Users.set(index,user);
            }
            else
            {
                Users.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
            }
            if(userUpdateEventListener != null){
                userUpdateEventListener.onUserListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Users.remove(index);
                recreateUserKeyIndexMapping();
            }
            if(userUpdateEventListener != null){
                userUpdateEventListener.onUserListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


        ChildEventListener imageurlChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String ukey=dataSnapshot.getKey();
                if(!imageURLKeyIndexMapping.containsKey(ukey))
                {
                    String user=dataSnapshot.getValue(String.class);
                    ImagesURL.add(user);
                    imageURLKeyIndexMapping.put(ukey,ImagesURL.size()-1);
                    if(imageurlUpdateEventListener != null) {
                        imageurlUpdateEventListener.onImageListUpdate();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String ukey=dataSnapshot.getKey();
                String user=dataSnapshot.getValue(String.class);
                if(imageURLKeyIndexMapping.containsKey(ukey))
                {
                    int index=imageURLKeyIndexMapping.get(ukey);
                    ImagesURL.set(index,user);
                }
                else
                {
                    ImagesURL.add(user);
                    imageURLKeyIndexMapping.put(ukey,ImagesURL.size()-1);
                }
                if(imageurlUpdateEventListener != null){
                    imageurlUpdateEventListener.onImageListUpdate();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String ukey=dataSnapshot.getKey();
                if(imageURLKeyIndexMapping.containsKey(ukey))
                {
                    int index=imageURLKeyIndexMapping.get(ukey);
                    ImagesURL.remove(index);
                    recreateImageUrlKeyIndexMapping();
                }
                if(imageurlUpdateEventListener != null){
                    imageurlUpdateEventListener.onImageListUpdate();
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    //na svaku promenu child
    ChildEventListener commentChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(! commentKeyIndexMapping.containsKey(ukey))
            {
                CommentModel user=dataSnapshot.getValue(CommentModel.class);
                user.commentKey=ukey;
                Comments.add(user);
                commentKeyIndexMapping.put(ukey,Comments.size()-1);
                if(commentUpdateEventListener != null) {
                    commentUpdateEventListener.onCommentListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            CommentModel user=dataSnapshot.getValue(CommentModel.class);
            user.commentKey=ukey;
            if(commentKeyIndexMapping.containsKey(ukey))
            {
                int index=commentKeyIndexMapping.get(ukey);
                Comments.set(index,user);
            }
            else
            {
                Comments.add(user);
                commentKeyIndexMapping.put(ukey,Comments.size()-1);
            }
            if(commentUpdateEventListener != null){
                commentUpdateEventListener.onCommentListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(commentKeyIndexMapping.containsKey(ukey))
            {
                int index=commentKeyIndexMapping.get(ukey);
                Comments.remove(index);
                recreateUserKeyIndexMapping();
            }
            if(commentUpdateEventListener != null){
                commentUpdateEventListener.onCommentListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener placeChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!userKeyIndexMapping.containsKey(ukey))
            {
                PlaceModel user=dataSnapshot.getValue(PlaceModel.class);
                user.placeKey=ukey;
                Places.add(user);
                placeKeyIndexMapping.put(ukey,Places.size()-1);
                if(placeUpdateEventListener != null) {
                    placeUpdateEventListener.onPlaceListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            PlaceModel user=dataSnapshot.getValue(PlaceModel.class);
            user.placeKey=ukey;
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=placeKeyIndexMapping.get(ukey);
                Places.set(index,user);
            }
            else
            {
                Places.add(user);
                placeKeyIndexMapping.put(ukey,Places.size()-1);
            }
            if(placeUpdateEventListener != null) {
                placeUpdateEventListener.onPlaceListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Places.remove(index);
                recreatePlaceKeyIndexMapping();
            }
            if(placeUpdateEventListener != null) {
                placeUpdateEventListener.onPlaceListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener eventChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!eventKeyIndexMapping.containsKey(ukey))
            {
                EventModel user=dataSnapshot.getValue(EventModel.class);
                user.eventKey=ukey;
                Events.add(user);
                eventKeyIndexMapping.put(ukey,Events.size()-1);
                if(eventUpdateEventListener != null) {
                    eventUpdateEventListener.onEventListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            EventModel user=dataSnapshot.getValue(EventModel.class);
            user.eventKey=ukey;
            if(eventKeyIndexMapping.containsKey(ukey))
            {
                int index=eventKeyIndexMapping.get(ukey);
                Events.set(index,user);
            }
            else
            {
                Events.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
            }
            if(eventUpdateEventListener != null) {
                eventUpdateEventListener.onEventListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(eventKeyIndexMapping.containsKey(ukey))
            {
                int index=eventKeyIndexMapping.get(ukey);
                Events.remove(index);
                recreatePlaceKeyIndexMapping();
            }
            if(eventUpdateEventListener != null) {
                eventUpdateEventListener.onEventListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private static class  SingltonHolder
    {
        public  static  final FirebaseAccess instance=new FirebaseAccess();
    }
    public static FirebaseAccess getInstance()
    {
        return SingltonHolder.instance;
    }

    public ArrayList<User> getUsers()
    {
        return  Users;
    }
    public ArrayList<PlaceModel> getPlaces()
    {
        return  Places;
    }
    public ArrayList<EventModel> getEvents()
    {
        return  Events;
    }
    public ArrayList<CommentModel> getComments()
    {
        return  Comments;
    }
    public ArrayList<String> getImages()
        {
            return  ImagesURL;
        }


    public void addNewUser(User user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

            String key=database.push().getKey();
            Users.add(user);
            userKeyIndexMapping.put(key,Users.size()-1);
            database.child(FIREBASE_USER_CHILD).child(key).setValue(user);
            user.UserKey=key;
    }
    public void addNewComment(CommentModel user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

        String key=database.push().getKey();
        Comments.add(user);
        commentKeyIndexMapping.put(key,Comments.size()-1);
        database.child(FIREBASE_COMMMENT_CHILD).child(key).setValue(user);
        user.commentKey=key;
    }
        public void addNewImage(String user)
        {
            //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
            //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

            String key=database.push().getKey();
            ImagesURL.add(user);
            imageURLKeyIndexMapping.put(key,Comments.size()-1);
            database.child(FIREBASE_IMAGEURL_CHILD).child(key).setValue(user);
        }
    public  User getUser(int index)
    {
     return Users.get(index);
    }
    public  CommentModel commentModel(int index)
    {
        return Comments.get(index);
    }
    public  void deleteUser(int index)
    {
        database.child(FIREBASE_USER_CHILD).child(Users.get(index).UserKey).removeValue();
        Users.remove(index);
        recreateUserKeyIndexMapping();

    }
    public  void deleteComment(int index)

    {
        database.child(FIREBASE_COMMMENT_CHILD).child(Comments.get(index).commentKey).removeValue();
        Comments.remove(index);
        recreateCommentKeyIndexMapping();

    }

    public void addNewPlace(PlaceModel user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

        String key=database.push().getKey();
        Places.add(user);
        placeKeyIndexMapping.put(key,Places.size()-1);
        database.child(FIREBASE_PLACE_CHILD).child(key).setValue(user);
        user.placeKey=key;
    }
    public  PlaceModel getPlace(int index)
    {
        return Places.get(index);
    }
    public  void deletePlace(int index)
    {
        database.child(FIREBASE_PLACE_CHILD).child(Places.get(index).placeKey).removeValue();
        Places.remove(index);
        recreatePlaceKeyIndexMapping();

    }
    public void addNewEvent(EventModel user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

        String key=database.push().getKey();
        Events.add(user);
        eventKeyIndexMapping.put(key,Events.size()-1);
        database.child(FIREBASE_EVENT_CHILD).child(key).setValue(user);
        user.eventKey=key;
    }
    public  EventModel getEvent(int index)
    {
        return Events.get(index);
    }
    public  void deleteEvent(int index)
    {
        database.child(FIREBASE_EVENT_CHILD).child(Events.get(index).eventKey).removeValue();
        Events.remove(index);
        recreateEventKeyIndexMapping();

    }


   public Uri getImage(String name)
    {
        Uri downloadURI = storageReference.child(name).getDownloadUrl().getResult();
        return  downloadURI;
    }
    public  void updateUser(int index, String nme, String srnme, String e, String pass, String g,String u, String lat, String lon)
    {

        User user=Users.get(index);
        user.setFirst_name(nme);
        user.setLast_name(srnme);
        user.setEmail(e);
        user.setPassword(pass);
        user.setGender(g);
        user.setUsername(u);
        user.setLatitude(lat);
        user.setLongitude(lon);
        database.child(FIREBASE_USER_CHILD).child(user.UserKey).setValue(user);
    }
    public  void updateUser(int index, User u)
    {

        User user=Users.get(index);
        user=u;
        database.child(FIREBASE_USER_CHILD).child(user.UserKey).setValue(user);
    }
    public  void updatePlace(int index, String comment, String placeName, String placeLocation, String placeState, String placePhone,String placeImage,String timePassed, String lon, String lot, String u)
    {

        PlaceModel pm=Places.get(index);
        pm.comment = comment;
        pm.placeName = placeName;
        pm.placeLocation = placeLocation;
        pm.placeState = placeState;
        pm.placePhone = placePhone;
        pm.placeImage = placeImage;
        pm.timePassed = timePassed;
        pm.longitute=lot;
        pm.longitute=lon;
        pm.userId=u;
        database.child(FIREBASE_PLACE_CHILD).child(pm.placeKey).setValue(pm);
    }
    public  void updateEvents(int index,String when, String where, String personImage, String comment, String name, String state, String placeName, String placeLocation, String timePassed, String pname)
    {

      /*  EventModel pm=Events.get(index);
        pm.when = when;
        pm.where = where;
        pm.personImage = personImage;
        pm.comment = comment;
        pm.name = name;
        pm.state = state;
        pm.placeName = placeName;
        pm.placeLocation = placeLocation;
        pm.timePassed = timePassed;
        pm.userID=pname;
        database.child(FIREBASE_EVENT_CHILD).child(pm.eventKey).setValue(pm);*/
    }

    private  void recreateUserKeyIndexMapping() {
        userKeyIndexMapping.clear();
        for (int i = 0; i < Users.size(); i++)
            userKeyIndexMapping.put(Users.get(i).UserKey, i);
    }
    private  void recreatePlaceKeyIndexMapping() {
        placeKeyIndexMapping.clear();
        for(int i=0;i<Places.size();i++)
            userKeyIndexMapping.put(Places.get(i).placeKey,i);

    }
    private  void recreateCommentKeyIndexMapping() {
        commentKeyIndexMapping.clear();
        for(int i=0;i<Comments.size();i++)
            commentKeyIndexMapping.put(Comments.get(i).commentKey,i);

    }
    private  void recreateEventKeyIndexMapping() {
        eventKeyIndexMapping.clear();
        for(int i=0;i<Events.size();i++)
            eventKeyIndexMapping.put(Events.get(i).eventKey,i);

    }

    private  void recreateImageUrlKeyIndexMapping() {
        imageURLKeyIndexMapping.clear();

          //  imageURLKeyIndexMapping.put(Image.get(i).eventKey,i);

    }

    userListUpdateEventListener userUpdateEventListener;
    public void setUserEventListener(userListUpdateEventListener listener)
    {
        userUpdateEventListener = listener;
    }

    public interface userListUpdateEventListener {
        void onUserListUpdate();
    }

    placeListUpdateEventListener placeUpdateEventListener;
    public void setPlaceEventListener(placeListUpdateEventListener listener)
    {
        placeUpdateEventListener = listener;
    }

    public interface placeListUpdateEventListener {
        void onPlaceListUpdate();
    }

    eventListUpdateEventListener eventUpdateEventListener;
    public void setEventListener(eventListUpdateEventListener listener)
    {
        eventUpdateEventListener = listener;
    }

    public interface eventListUpdateEventListener {
        void onEventListUpdate();
    }

    commentListUpdateEventListener commentUpdateEventListener;
    public void setEventListener(commentListUpdateEventListener listener)
    {
        commentUpdateEventListener = listener;
    }

    public interface commentListUpdateEventListener {
        void onCommentListUpdate();
    }


    imageurlListUpdateEventListener imageurlUpdateEventListener;
    public void setEventListener(imageurlListUpdateEventListener listener)
    {
        imageurlUpdateEventListener = listener;
    }

    public interface imageurlListUpdateEventListener {
        void onImageListUpdate();
    }

}

