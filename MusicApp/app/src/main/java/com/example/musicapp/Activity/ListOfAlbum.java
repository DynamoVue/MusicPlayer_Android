package com.example.musicapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.musicapp.Adapter.AllAlbumAdapter;
import com.example.musicapp.Adapter.CategoryByThemeAdapter;
import com.example.musicapp.Entity.Album;
import com.example.musicapp.Entity.Categories;
import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfAlbum extends AppCompatActivity implements FirebaseReference{
    RecyclerView recyclerViewAlbum;
    Toolbar toolbarAlbum;
    AllAlbumAdapter allAlbumAdapter;
    ArrayList<Album> albums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);
        init();
        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init(){
        recyclerViewAlbum = findViewById(R.id.recyclerViewAlbum);
        toolbarAlbum = findViewById(R.id.toolbarAlbum);
//        setSupportActionBar(toolbarAlbum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất cả Album");
        toolbarAlbum.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData(){
        DATABASE_REFERENCE_ALBUM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                albums = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Album album = dataSnapshot.getValue(Album.class);
                    albums.add(album);
                }
                allAlbumAdapter = new AllAlbumAdapter(ListOfAlbum.this, albums);
                recyclerViewAlbum.setLayoutManager(new GridLayoutManager(ListOfAlbum.this, 2));
                recyclerViewAlbum.setAdapter(allAlbumAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });

    }
}