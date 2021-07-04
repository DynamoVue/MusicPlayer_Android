package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
    public PlaylistAdapter(Context context, int resource, List<Playlist> objects) {
        super(context, resource, objects);
    }

    class ViewHolder {
        TextView txtNamePlaylist;
        ImageView imageBackground;
        ImageView imagePlaylist;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.animation_playlist, null);
            viewHolder = new ViewHolder();
            viewHolder.txtNamePlaylist = convertView.findViewById(R.id.txt_view_name_playlist);
            viewHolder.imagePlaylist = convertView.findViewById(R.id.img_view_playlist);
            viewHolder.imageBackground = convertView.findViewById(R.id.image_background_playlist_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Playlist playlist = getItem(position);
        Picasso.get().load(playlist.getImageURLPlaylist()).fit().centerCrop().into(viewHolder.imageBackground);
        Picasso.get().load(playlist.getImageURLPlaylist()).fit().centerCrop().into(viewHolder.imagePlaylist);
        viewHolder.txtNamePlaylist.setText(playlist.getNamePlaylist());
        return convertView;
    }
}
