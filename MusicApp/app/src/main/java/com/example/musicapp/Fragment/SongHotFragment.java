package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.HotSongAdapter;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongHotFragment extends Fragment implements FirebaseReference {

    View view;
    RecyclerView rvHotSong;
    HotSongAdapter hotSongAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_hot, container, false);
        rvHotSong=view.findViewById(R.id.rvHotSong);
        getData();
        return view;
    }

    private void getData() {
        DATABASE_REFERENCE_MUSIC.addValueEventListener(new ValueEventListener() {
            List<Song> songs = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Song song = ds.getValue(Song.class);
                    songs.add(song);
                    if(songs.size()>=5){
                        break;
                    }
                }
                Collections.sort(songs, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        if(o1.getNumberOfLikes()>o2.getNumberOfLikes()){
                            return 1;
                        }else if (o1.getNumberOfLikes()<o2.getNumberOfLikes()){
                            return -1;
                        }else return 0;

                    }
                });
                hotSongAdapter = new HotSongAdapter(getActivity(), (ArrayList<Song>) songs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rvHotSong.setLayoutManager(linearLayoutManager);
                rvHotSong.setAdapter(hotSongAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
