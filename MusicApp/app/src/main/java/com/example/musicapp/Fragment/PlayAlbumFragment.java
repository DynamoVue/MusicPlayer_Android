package com.example.musicapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Adapter.PlayMusicAdapter;
import com.example.musicapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayAlbumFragment extends Fragment {

    View view;
    RecyclerView recyclerViewPlayMusic;
    PlayMusicAdapter playMusicAdapter;

    public PlayAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayAlbumFragment newInstance() {
        PlayAlbumFragment fragment = new PlayAlbumFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play_album, container, false);
        recyclerViewPlayMusic = view.findViewById(R.id.recyclerVPlayAlbum);
        if(PlayMusicActivity.songs.size() > 0){
            playMusicAdapter = new PlayMusicAdapter(getActivity(), PlayMusicActivity.songs);
            recyclerViewPlayMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewPlayMusic.setAdapter(playMusicAdapter);
        }

        return view;
    }

    public static PlayAlbumFragment newInstance(String text) {
        PlayAlbumFragment albumFragment = new PlayAlbumFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        albumFragment.setArguments(b);
        return albumFragment;
    }
}