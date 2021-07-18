package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.SearchSongAdapter;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.musicapp.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    View view;
    Toolbar toolbarSearchSong;
    RecyclerView recyclerViewSearchSong;
    TextView txtNoData;
    SearchSongAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbarSearchSong = view.findViewById(R.id.toolBarSearchSong);
        recyclerViewSearchSong = view.findViewById(R.id.recyclerVSearchSong);
        txtNoData = view.findViewById(R.id.tvNoDataSearchSong);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarSearchSong);
        toolbarSearchSong.setTitle("");

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch); //click vao chau nao
        SearchView searchView = (SearchView) menuItem.getActionView();
        //Handle event
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSong(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchSong(String keyword){
        ArrayList<Song> songs = new ArrayList<>();
        FirebaseReference.DATABASE_REFERENCE_MUSIC.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = (String)dataSnapshot.child("songName").getValue();
                    if (name.contains(keyword)) {
                        Song song = dataSnapshot.getValue(Song.class);
                        songs.add(song);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        if(songs.size() > 0){
            adapter = new SearchSongAdapter(getActivity(), songs);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewSearchSong.setLayoutManager(linearLayoutManager);
            recyclerViewSearchSong.setAdapter(adapter);
            txtNoData.setVisibility(view.GONE);
        }else{
            recyclerViewSearchSong.setVisibility(View.GONE);
            txtNoData.setVisibility(view.VISIBLE);
        }
    }
}
