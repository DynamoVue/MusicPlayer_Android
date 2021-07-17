package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotSongAdapter extends RecyclerView.Adapter<HotSongAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;

    public HotSongAdapter(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_hot_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotSongAdapter.ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.tvHotSong.setText(song.getSongName());
        holder.tvHotSongSingerName.setText(song.getSinger());
        Picasso.get().load(song.getImageURL()).into(holder.imageViewHotSong);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHotSong, tvHotSongSingerName;
        ImageView imageViewHotSong, imageViewLike;
        public ViewHolder(View itemView) {
            super(itemView);
            tvHotSong =itemView.findViewById(R.id.tvHotSong);
            tvHotSongSingerName = itemView.findViewById(R.id.tvHotSongSingerName);
            imageViewHotSong= itemView.findViewById(R.id.imageViewHotSong);
            imageViewLike= itemView.findViewById(R.id.imageViewLike);
        }
    }
}
