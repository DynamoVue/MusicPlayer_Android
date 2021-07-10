package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicapp.Adapter.ListPlaylistAdapter;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListPlaylistFragment extends Fragment implements FirebaseReference {
    private View view;
    ListPlaylistAdapter adapter;
    List<Playlist> playlists;
    ListView listView;
    TextView title_view_playlist;
    TextView txt_view_more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        listView = view.findViewById(R.id.list_view_playlist);
        title_view_playlist = view.findViewById(R.id.title_view_playlist);
        txt_view_more = view.findViewById(R.id.txt_view_more_playlist);
        getData();
        return view;
    }

    private void getData() {
        DATABASE_REFERENCE_PLAYLIST.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playlists = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
                    playlist.setId(dataSnapshot.getKey());
                    playlists.add(playlist);
                }
                adapter = new ListPlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1, playlists);
                listView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listView);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = new Intent(getActivity(), PlaylistFragment.class);
//                        intent.putExtra("item_playlist", playlists.get(position));
//                        startActivity(intent);
//                    }
//                });
            }

            //
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
