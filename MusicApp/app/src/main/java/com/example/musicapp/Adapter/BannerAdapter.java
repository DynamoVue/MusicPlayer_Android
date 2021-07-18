package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Activity.PlaylistActivity;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    Context context;
    List<Song> songs;

    public BannerAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //set view for only one advertisement
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.animation_banner, null);
        ImageView imgBackground = view.findViewById(R.id.imageView_temp);
        ImageView imgSongBanner = view.findViewById(R.id.img_view_banner);
        TextView txtTitleSongBanner = view.findViewById(R.id.txt_title_banner_song);
        TextView txtContent = view.findViewById(R.id.txt_content);
        Picasso.get().load(songs.get(position).getImageURL()).fit().centerCrop().into(imgBackground);
        Picasso.get().load(songs.get(position).getImageURL()).fit().centerCrop().into(imgSongBanner);
        txtTitleSongBanner.setText(songs.get(position).getSongName());
        txtContent.setText(songs.get(position).getSingers());

        //intent to play
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("banner", songs.get(position));
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
