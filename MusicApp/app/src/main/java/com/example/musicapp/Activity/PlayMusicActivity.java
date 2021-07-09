package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.musicapp.Adapter.ViewPagerPlaylistAdapter;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.Fragment.PlayASongFragment;
import com.example.musicapp.Fragment.PlayAlbumFragment;
import com.example.musicapp.R;

import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {

    Toolbar playMToolBar;
    TextView txtSongTime, txtTotalSongTime;
    SeekBar skSongPlayThrough;
    ImageButton imgPlay, imgNext, imgPrev, imgRandom, imgRepeat;
    ViewPager viewPagerPlayM;
    public static ArrayList<Song> songs = new ArrayList<>();
    public static  ViewPagerPlaylistAdapter adapterMusic;
    PlayASongFragment playASong;
    PlayAlbumFragment playAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        songs.clear();
        if(intent != null){
            if(intent.hasExtra("song")){
                Song song = intent.getParcelableExtra("song");
                songs.add(song);
            }
            if(intent.hasExtra("album")){
                ArrayList<Song> album = intent.getParcelableArrayListExtra("album");
                songs.addAll(album);
            }
        }
    }

    public void init() {
        playMToolBar = findViewById(R.id.playMToolBar);
        txtSongTime = findViewById(R.id.songTime);
        txtTotalSongTime = findViewById(R.id.songTotalTime);
        skSongPlayThrough = findViewById(R.id.songPlayThrough);
        imgPlay = findViewById(R.id.imageBPlay);
        imgPrev = findViewById(R.id.imageBPrev);
        imgNext = findViewById(R.id.imageBNext);
        imgRandom = findViewById(R.id.imageBSuffle);
        imgRepeat = findViewById(R.id.imageBRepeat);
        viewPagerPlayM = findViewById(R.id.playMViewPager);
        setSupportActionBar(playMToolBar);
        getSupportActionBar(playMToolBar);
        playMToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playMToolBar.setTitleTextColor(Color.WHITE);

        adapterMusic = new ViewPagerPlaylistAdapter(getSupportFragmentManager());
        adapterMusic.addFragment(playAlbum);
        adapterMusic.addFragment(playASong);
        viewPagerPlayM.setAdapter(adapterMusic);
    }

    private void getSupportActionBar(Toolbar playMToolBar) {
    }

    private void setSupportActionBar(Toolbar playMToolBar) {
    }
}