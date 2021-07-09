package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar playMToolBar;
    TextView txtSongTime, txtTotalSongTime;
    SeekBar skSongPlayThrough;
    ImageButton imgPlay, imgNext, imgPrev, imgRandom, imgRepeat;
    ViewPager viewPagerPlayM;
    public static ArrayList<Song> songs = new ArrayList<>();
    public static  ViewPagerPlaylistAdapter adapterMusic;
    PlayASongFragment playASong;
    PlayAlbumFragment playAlbum;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getDataFromIntent();
        init();
        eventClick();
    }

    private void eventClick() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapterMusic.getItem(1) != null){
                    if(songs.size() > 0){
                        playASong.setCircleImageView(songs.get(0).getImageURL());
                        handler.removeCallbacks(this);
                    }else{
                        handler.postDelayed(this, 500);
                        //500s lai kiem tra url va set hinh anh neu fail
                    }
                }
            }
        }, 500);
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });
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
        //This two belows is weir, consider vids 54,55
        setSupportActionBar(playMToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);;
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

        //Play immediately after clicking
        if(songs.size() > 0){
            playASong = (PlayASongFragment) adapterMusic.getItem(1);
            getSupportActionBar().setTitle(songs.get(0).getSongName());
            new PlayMp3().execute(songs.get(0).getMp3URL());
            imgPlay.setImageResource(R.drawable.iconpause);
        }

    }

    class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String song) {
            super.onPostExecute(song);
            try{
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //Dung va khoi tao lai tranh van de xay ra khi xu ly bat dong bo
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare(); //Must have this
            }catch (IOException e){
                e.printStackTrace();
            }

            mediaPlayer.start();
            TimeSong();
        }
    }

    private void TimeSong() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        txtTotalSongTime.setText(sdf.format(mediaPlayer.getDuration()));
        skSongPlayThrough.setMax(mediaPlayer.getDuration());
    }
}