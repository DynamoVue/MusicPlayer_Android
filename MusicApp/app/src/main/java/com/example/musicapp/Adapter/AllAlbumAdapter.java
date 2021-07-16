package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Entity.Album;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllAlbumAdapter extends RecyclerView.Adapter<AllAlbumAdapter.ViewHolder>  {
    Context context;
    ArrayList<Album> albums;

    public AllAlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllAlbumAdapter.ViewHolder holder, int position) {
        Album album = albums.get(position);
        Picasso.get().load(album.getAlbumPicture()).into(holder.imageViewAllAlbum);
        holder.textViewAllAlbum.setText(album.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAllAlbum;
        TextView textViewAllAlbum;
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewAllAlbum = itemView.findViewById(R.id.imageViewAllAlbum);
            textViewAllAlbum = itemView.findViewById(R.id.textViewAllAlbum);
        }
    }

}
