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
import java.util.Random;

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
    int position = 0;
    boolean isRepeated = false, isRandom = false, next = false;

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
        //Event for button repeat
        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRepeated == false){
                    if(isRandom == true){
                        isRandom = false;
                        imgRandom.setImageResource(R.drawable.iconsuffle);
                    }
                    imgRepeat.setImageResource(R.drawable.iconsyned);
                    isRepeated = true;
                }else{
                    imgRepeat.setImageResource(R.drawable.iconrepeat);
                    isRepeated = false;
                }
            }
        });
        //Event for button random
        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRandom == false){
                    if(isRepeated == true){
                        isRepeated = false;
                        imgRepeat.setImageResource(R.drawable.iconrepeat);
                    }
                    imgRandom.setImageResource(R.drawable.iconshuffled);
                    isRandom = true;
                }else{
                    imgRandom.setImageResource(R.drawable.iconsuffle);
                    isRandom = false;
                }
            }
        });
        //Event for sk bar (play through bar)
        skSongPlayThrough.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        //Event for next
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songs.size() > 0){
                    if(mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if(position < songs.size()){
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position ++;
                        if(isRepeated){
                            if(position == 0){
                                position = songs.size();
                            }
                            position -= 1;
                        }
                        if(isRandom){
                            Random random = new Random();
                            int index = random.nextInt(songs.size());
                            if(index == position){
                                index ++;
                            }
                            position = index;
                        }
                        if(position > songs.size() - 1){
                            position = 0;
                        }
                        playDaSong(position);
                    }
                }
                imgNext.setClickable(false);
                imgPrev.setClickable(false);
                Handler temp = new Handler();
                temp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPrev.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 5000);
            }
        });
        //Event for prev
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songs.size() > 0){
                    if(mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if(position < songs.size()){
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position --;
                        if(position < 0){
                            position = songs.size() - 1;
                        }
                        if(isRepeated){
                            position += 1;
                        }
                        if(isRandom){
                            Random random = new Random();
                            int index = random.nextInt(songs.size());
                            if(index == position){
                                index ++;
                            }
                            position = index;
                        }
                        playDaSong(position);
                    }
                }
                imgNext.setClickable(false);
                imgPrev.setClickable(false);
                Handler temp = new Handler();
                temp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPrev.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 5000);
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
                mediaPlayer.stop();
                songs.clear();
            }
        });
        playMToolBar.setTitleTextColor(Color.WHITE);

        adapterMusic = new ViewPagerPlaylistAdapter(getSupportFragmentManager());
        adapterMusic.addFragment(playAlbum);
        adapterMusic.addFragment(playASong);
        viewPagerPlayM.setAdapter(adapterMusic);

        //Play immediately after clicking
        playASong = (PlayASongFragment) adapterMusic.getItem(1);
        playDaSong(0);
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

    public void playDaSong(int position){
        if(songs.size() > 0){
            Song daSong = songs.get(position);
            playASong.setCircleImageView(daSong.getImageURL());
            getSupportActionBar().setTitle(daSong.getSongName());
            new PlayMp3().execute(daSong.getMp3URL());
            imgPlay.setImageResource(R.drawable.iconpause);
        }
    }
}