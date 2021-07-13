package com.example.musicapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerPlaylistAdapter extends FragmentPagerAdapter {
    public final ArrayList<Fragment> fragmentLists = new ArrayList<>();
    public ViewPagerPlaylistAdapter(FragmentManager fm){
        super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    public void addFragment(Fragment fm){
        fragmentLists.add(fm);
    }
}
