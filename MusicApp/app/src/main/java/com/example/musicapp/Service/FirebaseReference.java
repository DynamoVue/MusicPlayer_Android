package com.example.musicapp.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface FirebaseReference {
    DatabaseReference DATABASE_REFERENCE_MUSIC = FirebaseDatabase.getInstance().getReference("music");
    DatabaseReference DATABASE_REFERENCE_PLAYLIST = FirebaseDatabase.getInstance().getReference("playlist");
<<<<<<<<< Temporary merge branch 1
    DatabaseReference DATABASE_REFERENCE_USERS = FirebaseDatabase.getInstance().getReference("users");
=========
    DatabaseReference DATABASE_REFERENCE_ALBUM = FirebaseDatabase.getInstance().getReference("albums");
>>>>>>>>> Temporary merge branch 2
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // [START download_create_reference]
    // Create a storage reference from our app
    StorageReference STORAGE_REFERENCE = storage.getReference();
}
