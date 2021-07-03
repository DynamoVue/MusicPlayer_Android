package com.example.musicapp.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface FirebaseReference {
    DatabaseReference DATABASE_REFERENCE_MUSIC = FirebaseDatabase.getInstance().getReference("music");
    DatabaseReference DATABASE_REFERENCE_PLAYLIST = FirebaseDatabase.getInstance().getReference("playlist");
}
