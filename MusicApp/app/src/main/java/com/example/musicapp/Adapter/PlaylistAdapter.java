package com.example.musicapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    public List<Playlist> playlists;
    public Context mContext;


    public PlaylistAdapter(List<Playlist> playlists, Context mContext) {
        this.playlists = playlists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder holder, int position) {
        holder.title.setText(playlists.get(position).getId());
        holder.desc.setText(playlists.get(position).getPlaylistName());
        Picasso.get().load(playlists.get(position).getPlaylistUrl()).fit().centerCrop().into(holder.imageThumbnail);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView index;
        ImageView imageThumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.cardItemDesc);
            title = itemView.findViewById(R.id.cardItemTitle);
            index = itemView.findViewById(R.id.cardItemIndex);
            imageThumbnail = itemView.findViewById(R.id.cardItemImage);
        }
    }

}
