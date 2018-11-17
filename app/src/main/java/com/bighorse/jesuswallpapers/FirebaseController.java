package com.bighorse.jesuswallpapers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("images");

    private static FirebaseController mInstance;
    private FirebaseListener mListener;

    private FirebaseController(){}

    public static FirebaseController getInstance(){
        if(mInstance == null){
            mInstance = new FirebaseController();
        }
        return mInstance;
    }

    public interface FirebaseListener{
        void onChildAdded(ImageModel image);
    }

    public void setListener(FirebaseListener listener){
        mListener = listener;
    }

    public void attachDatabaseListener(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ImageModel image = dataSnapshot.getValue(ImageModel.class);
                if(image != null){
                    image.setName(dataSnapshot.getKey());
                    if(mListener != null) {
                        mListener.onChildAdded(image);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
