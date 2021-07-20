package com.example.musicapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.musicapp.Adapter.SongFilterAdapter;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class FavorSongsActivity extends AppCompatActivity implements FirebaseReference {
    private ImageView btnBack;
    private EditText searchView;
    private SongFilterAdapter adapter;
    private RecyclerView filteredSongs;
    CharSequence search="";

    private List<Song> favSongs = new ArrayList<>();
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;

    private void initFirebaseListener() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    getData();
                }

                if (user == null) {

                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authListener != null) {
            FirebaseAuth.getInstance().addAuthStateListener(authListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authListener);
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_songs);

        searchView = (EditText) findViewById(R.id.search_bar);
        filteredSongs = (RecyclerView) findViewById(R.id.favSongs);

        btnBack = (ImageView) findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initFirebaseListener();
    }

    private void getData() {
        DatabaseReference playlistRef = DATABASE_REFERENCE_USERS.child(user.getUid());

        playlistRef.child("favoriteSongs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Song> songs = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = (Song) dataSnapshot.getValue(Song.class);
                    songs.add(song);
                }

                favSongs = songs;
                renderSongsInRecyclerView(songs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void renderSongsInRecyclerView(List<Song> songs) {
        adapter = new SongFilterAdapter(songs, this, FavorSongsActivity.this);
        filteredSongs.setLayoutManager(new LinearLayoutManager(this));
        filteredSongs.setAdapter(adapter);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                search = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}