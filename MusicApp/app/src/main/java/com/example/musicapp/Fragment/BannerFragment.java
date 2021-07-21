package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Adapter.BannerAdapter;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class BannerFragment extends Fragment implements FirebaseReference {
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    Runnable runnable;
    Handler handler;
    int currentItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        init(view);

        DATABASE_REFERENCE_MUSIC.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                currentItem = 0;
                List<Song> songList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    if (song.getNumberOfLikes() > 4) {
                        songList.add(song);
                    }
                }
                bannerAdapter = new BannerAdapter(getActivity(), songList);
                viewPager.setAdapter(bannerAdapter);
                circleIndicator.setViewPager(viewPager);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem > viewPager.getAdapter().getCount()) {
                            currentItem = 0;
                        }
                        viewPager.setCurrentItem(currentItem, true);
                        handler.postDelayed(runnable, 4500);
                    }
                };
                handler.postDelayed(runnable, 4500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void init(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.indicatorFirst);
    }
}
