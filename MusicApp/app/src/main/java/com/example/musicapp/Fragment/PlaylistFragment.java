package com.example.musicapp.Fragment;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.PlaylistAdapter;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.blurry.Blurry;

public class PlaylistFragment extends Fragment implements FirebaseReference {
    View view;
    PlaylistAdapter playlistAdapter;
    RecyclerView playlistView;
    ImageView playlistBanner, playlistThumbNail;
    TextView playlistTitle, playlistTotalSongs;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songcaterogy, container, false);
        playlistView = (RecyclerView) view.findViewById(R.id.playlist);
        playlistTitle = (TextView) view.findViewById(R.id.appbarSubTitle);
        playlistBanner = (ImageView) view.findViewById(R.id.appbarImage);
        playlistThumbNail = (ImageView) view.findViewById(R.id.appbarSubImage);
        playlistTotalSongs = (TextView) view.findViewById(R.id.appbarSubDesc);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getData();
        return view;
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
        playlistAdapter = new PlaylistAdapter(songs, PlaylistFragment.this.getContext(), PlaylistFragment.this);
        playlistView.setLayoutManager(new LinearLayoutManager(PlaylistFragment.this.getContext()));
        playlistView.setAdapter(playlistAdapter);
    }

    private void getSongsData(Playlist playlist) {
        if (playlist.getSongs() != null && playlist.getSongs().size() > 0) {
            List<Song> songs = new ArrayList<>();
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
                    if (dataSnapshot.hasChildren()) {
                        songIds = (ArrayList<Long>) dataSnapshot.getValue();
                    }

                    if (dataSnapshot.getKey().equals("playlistName")) playlistName = (String)dataSnapshot.getValue();
                    if (dataSnapshot.getKey().equals("playlistUrl")) playlistUrl = (String)dataSnapshot.getValue();
                    if (dataSnapshot.getKey().equals("id")) id = Long.toString((Long)dataSnapshot.getValue());
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

    public void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//
//            if (listItem != null) {
//                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
//                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//                totalHeight += listItem.getMeasuredHeight();
//
//            }
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
    }
}
