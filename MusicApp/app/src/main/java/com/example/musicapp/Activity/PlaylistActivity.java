package com.example.musicapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.PlaylistAdapter;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity implements FirebaseReference {
    PlaylistAdapter playlistAdapter;
    RecyclerView playlistView;
    ImageView playlistBanner, playlistThumbNail, backButton;
    TextView playlistTitle, playlistTotalSongs;
    Toolbar toolbar;
    Button btnShuffle;

    List<Song> songs;

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_songcaterogy);

        playlistView = (RecyclerView) findViewById(R.id.playlist);
        playlistTitle = (TextView) findViewById(R.id.appbarSubTitle);
        playlistBanner = (ImageView) findViewById(R.id.appbarImage);
        playlistThumbNail = (ImageView) findViewById(R.id.appbarSubImage);
        backButton = (ImageView) findViewById(R.id.backButton);
        playlistTotalSongs = (TextView) findViewById(R.id.appbarSubDesc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnShuffle = (Button) findViewById(R.id.btnShuffle);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PlaylistActivity.this, PlayMusicActivity.class);
                myIntent.putExtra("album", (Serializable)songs); //Optional parameters
                startActivity(myIntent);
            }
        });

        getData();
    }

    private void renderViewFromPlaylist(Playlist playlist) {
        playlistTitle.setText(playlist.getPlaylistName() + "");
        if (playlist.getSongs() != null) {
            playlistTotalSongs.setText(playlist.getSongs().size() + " songs");
        } else playlistTotalSongs.setText("0 songs");
        Picasso.get().load(playlist.getPlaylistUrl()).fit().centerCrop().into(playlistThumbNail);
        Picasso.get().load(playlist.getPlaylistUrl()).fit().centerCrop().into(playlistBanner);
    }

    private void renderSongsInRecyclerView(List<Song> songs) {
        playlistAdapter = new PlaylistAdapter(songs, this, this);
        playlistView.setLayoutManager(new LinearLayoutManager(this));
        playlistView.setAdapter(playlistAdapter);
    }

    private void getSongsData(Playlist playlist) {
        if (playlist.getSongs() != null && playlist.getSongs().size() > 0) {
            songs = new ArrayList<>();
            ArrayList<Long> songIds = (ArrayList<Long>)playlist.getSongs();

            DATABASE_REFERENCE_MUSIC.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Long id = (Long)dataSnapshot.child("id").getValue();
                        int index = songIds.indexOf(id);
                        if (index > -1) {
                            Song song = dataSnapshot.getValue(Song.class);
                            songs.add(song);
                        }
                    }

                    renderSongsInRecyclerView(songs);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
    }

    private void getData() {
        Query connectedPlaylist = DATABASE_REFERENCE_PLAYLIST.child("2");
//        Query playlistFilteredByIds = DATABASE_REFERENCE_MUSIC.orderByChild("id");
        connectedPlaylist.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Long> songIds;
            String playlistName, playlistUrl, id;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChildren() && dataSnapshot.getKey().equals("songs")) {
                        songIds = (ArrayList<Long>) dataSnapshot.getValue();
                    }

                    if (dataSnapshot.getKey().equals("playlistName")) playlistName = (String)dataSnapshot.getValue();
                    if (dataSnapshot.getKey().equals("playlistUrl")) playlistUrl = (String)dataSnapshot.getValue();
                    if (dataSnapshot.getKey().equals("id")) id = (String)dataSnapshot.getValue();
                }

                Playlist playlist = new Playlist(id, playlistName, playlistUrl, songIds);
                renderViewFromPlaylist(playlist);
                getSongsData(playlist);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
