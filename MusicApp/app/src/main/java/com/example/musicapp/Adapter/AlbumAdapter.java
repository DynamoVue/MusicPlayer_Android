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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> albums;

    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.tvSingerName.setText(album.getSingerName());
        holder.tvAlbumName.setText(album.getAlbumName());
        Picasso.get().load(album.getAlbumPicture()).into(holder.imageViewAlbum);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewAlbum;
        TextView tvAlbumName;
        TextView tvSingerName;
        public  ViewHolder(View itemView){
            super((itemView));
            imageViewAlbum = itemView.findViewById(R.id.imageViewAlbum);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvSingerName = itemView.findViewById(R.id.tvSingerName);

        }
    }
}
