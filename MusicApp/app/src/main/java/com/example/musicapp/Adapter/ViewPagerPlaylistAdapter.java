package com.example.musicapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicapp.Entity.Song;
import com.example.musicapp.Fragment.PlayASongFragment;
import com.example.musicapp.Fragment.PlayAlbumFragment;

import java.util.ArrayList;

public class ViewPagerPlaylistAdapter extends FragmentStateAdapter {
    public static ArrayList<Song> songs = new ArrayList<>();

    private String url;

    public ViewPagerPlaylistAdapter(FragmentActivity fragmentActivity, ArrayList<Song> songs) {
        super(fragmentActivity);
        this.songs = songs;
    }


    @Override
    public Fragment createFragment(int position) {
        String url = songs.get(position).getImageURL();
        return PlayASongFragment.newInstance("", url);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
