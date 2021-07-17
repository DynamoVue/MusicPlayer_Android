package com.example.musicapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.Adapter.AlbumAdapter;
import com.example.musicapp.Entity.Album;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumHotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumHotFragment extends Fragment implements FirebaseReference {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    private RecyclerView recyclerViewAlbum;
    private TextView tvMoreAlbum;
    AlbumAdapter albumAdapter;

    public AlbumHotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumHot.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumHotFragment newInstance(String param1, String param2) {
        AlbumHotFragment fragment = new AlbumHotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_album_hot, container, false);
        recyclerViewAlbum = view.findViewById(R.id.rvAlbum);
        tvMoreAlbum = view.findViewById(R.id.tvMore);
        getData();
        return view;

    }

    private void getData() {

        DATABASE_REFERENCE_ALBUM.addValueEventListener(new ValueEventListener() {
            List<Album> albums = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Album album = ds.getValue(Album.class);
                    albums.add(album);
                }
                albumAdapter = new AlbumAdapter(getActivity(), (ArrayList<Album>) albums);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                recyclerViewAlbum.setLayoutManager(linearLayoutManager);
                recyclerViewAlbum.setAdapter(albumAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}