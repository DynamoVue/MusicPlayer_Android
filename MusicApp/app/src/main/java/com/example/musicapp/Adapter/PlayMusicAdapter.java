package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;

import java.util.ArrayList;

public class PlayMusicAdapter extends RecyclerView.Adapter<PlayMusicAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;

    public PlayMusicAdapter(Context context, ArrayList<Song> songs){
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_play_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayMusicAdapter.ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.txtArtist.setText(song.getSinger());
        holder.txtIndex.setText(position + 1 + "");
        holder.txtSongName.setText(song.getSongName());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIndex, txtSongName, txtArtist;
        public ViewHolder (View itemView){
            super(itemView);
            txtArtist = itemView.findViewById(R.id.tvPlayMusicArtist);
            txtIndex = itemView.findViewById(R.id.tvPlayMusicIndex);
            txtSongName = itemView.findViewById(R.id.tvPlayMusicSongName);
        }
    }

}
