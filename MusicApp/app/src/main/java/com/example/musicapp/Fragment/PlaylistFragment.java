package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.PlaylistAdapter;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements FirebaseReference {
    View view;
    ListView listPlaylist;
    TextView txtTitle;
    TextView txtContinue;
    PlaylistAdapter playlistAdapter;
    Playlist playlist;
    RecyclerView playlistView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songcaterogy, container, false);
        playlistView = (RecyclerView) view.findViewById(R.id.playlist);
        getData();
        return view;
    }

    private void getData() {
        Query connectedPlaylist = DATABASE_REFERENCE_PLAYLIST.child("1");
        connectedPlaylist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.e("i love you", "onDataChange: "+dataSnapshot.toString());
//                    if (dataSnapshot.getKey()== uid)
//                    {
//                        User user= dataSnapshot.getValue(User.class);
//                        array[0] = user;
//                    }
//                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
//                    playlist.setId(dataSnapshot.getKey());
//                    playlists.add(playlist);
                }
//                playlistAdapter = new PlaylistAdapter(playlists, PlaylistFragment.this.getContext());
//                playlistView.setLayoutManager(new LinearLayoutManager(PlaylistFragment.this.getContext()));
//                playlistView.setAdapter(playlistAdapter);
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
