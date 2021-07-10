package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songs;

    public SearchSongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    //anh xa va gan lai layout cho moi dong item
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dong_search_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.artistName.setText(song.getSinger());
        Picasso.get().load(song.getImageURL()).into(holder.imageViewSongCover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    //dinh dang cho cac item trong recycler view
    //thiet ke va khai bao view thong qua viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName, artistName;
        ImageView imgViewLikes, imageViewSongCover;

        public ViewHolder(View itemView){
            super(itemView);
            songName = itemView.findViewById(R.id.tvSearchSongName);
            artistName = itemView.findViewById(R.id.tvSearchArtistName);
            imgViewLikes = itemView.findViewById(R.id.imageViewSearchLikes);
            imageViewSongCover = itemView.findViewById(R.id.imageViewSearchItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("song", songs.get(getPosition()));
                    context.startActivity(intent);
                }
            });
            //Luot thich
            imgViewLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgViewLikes.setImageResource(R.drawable.iconloved);
                    //This part is to update like
                }
            });
        }
    }
}
