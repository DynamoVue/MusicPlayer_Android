package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity {
    private ArrayList<Song> songs = new ArrayList<>();
    private ViewPagerPlaylistAdapter adapterMusic;
    private androidx.appcompat.widget.Toolbar playMToolBar;
    private TextView txtSongTime, txtTotalSongTime;
    private SeekBar skSongPlayThrough;
    private ImageButton imgPlay, imgNext, imgPrev, imgRandom, imgRepeat;
    private ViewPager2 viewPagerPlayM;
    private MediaPlayer mediaPlayer;
    private int position = 0;
    private boolean isRepeated = false, isRandom = false, next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getDataFromIntent();
        init();
        eventClick();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayMp3().execute(songs.get(0).getMp3URL());
        hdlr.postDelayed(UpdateSongTime, 50);
        imgPlay.setImageResource(R.drawable.iconpause);
    }
    private void eventClick() {
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    new PlayMp3().execute(songs.get(0).getMp3URL());
//                    hdlr.postDelayed(UpdateSongTime, 50);
                    imgPlay.setImageResource(R.drawable.iconpause);
                } else  if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.iconplay);
                } else {
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });
        //Event for button repeat
        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeated == false) {
                    if (isRandom == true) {
                        isRandom = false;
                        imgRandom.setImageResource(R.drawable.iconsuffle);
                    }
                    imgRepeat.setImageResource(R.drawable.iconsyned);
                    isRepeated = true;
                } else {
                    imgRepeat.setImageResource(R.drawable.iconrepeat);
                    isRepeated = false;
                }
            }
        });
        //Event for button random
        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRandom == false) {
                    if (isRepeated == true) {
                        isRepeated = false;
                        imgRepeat.setImageResource(R.drawable.iconrepeat);
                    }
                    imgRandom.setImageResource(R.drawable.iconshuffled);
                    isRandom = true;
                } else {
                    imgRandom.setImageResource(R.drawable.iconsuffle);
                    isRandom = false;
                }
            }
        });
        //Event for sk bar (play through bar)
        skSongPlayThrough.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    skSongPlayThrough.setProgress(progress);
                }
            }
        });

        //Event for next
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songs.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < songs.size()) {
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position++;
                        if (isRepeated) {
                            if (position == 0) {
                                position = songs.size();
                            }
                            position -= 1;
                        }
                        if (isRandom) {
                            Random random = new Random();
                            int index = random.nextInt(songs.size());
                            if (index == position) {
                                index++;
                            }
                            position = index;
                        }
                        if (position > songs.size() - 1) {
                            position = 0;
                        }
                        playDaSong(position,false);
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
                }, 3000);
            }
        });
        //Event for prev
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songs.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < songs.size()) {
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position--;
                        if (position < 0) {
                            position = songs.size() - 1;
                        }
                        if (isRepeated) {
                            position += 1;
                        }
                        if (isRandom) {
                            Random random = new Random();
                            int index = random.nextInt(songs.size());
                            if (index == position) {
                                index++;
                            }
                            position = index;
                        }
                        if (position > songs.size() - 1) {
                            position = 0;
                        }
                        playDaSong(position,false);
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
                }, 3000);
            }
        });
        playMToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                songs.clear();
            }
        });
        playMToolBar.setTitleTextColor(Color.WHITE);
//        viewPagerPlayM.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int positionPage) {
//                super.onPageSelected(position);
//                position = positionPage;
//                playDaSong(positionPage, true);
//            }
//        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        songs.clear();
        if (intent != null) {
            intent.getExtras();
            if (intent.hasExtra("song")) {
                Song song = (Song) intent.getExtras().getSerializable("song");
                songs.add(song);
            }
            if (intent.hasExtra("album")) {
                ArrayList<Song> album = (ArrayList<Song>) intent.getExtras().getSerializable("album");
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
        adapterMusic = new ViewPagerPlaylistAdapter(this,songs);
        viewPagerPlayM.setAdapter(adapterMusic);
        viewPagerPlayM.setUserInputEnabled(false);
        //This two belows is weir, consider vids 54,55

        skSongPlayThrough.setProgressBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

    }
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mediaPlayer.getCurrentPosition();
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            txtSongTime.setText(sdf.format(sTime));
            skSongPlayThrough.setProgress(sTime);
            hdlr.postDelayed(this, 50);
        }
    };
    class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        protected void onPostExecute(String song) {
            super.onPostExecute(song);
                try {
                    mediaPlayer.setDataSource(song);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                eTime = mediaPlayer.getDuration();
                sTime = mediaPlayer.getCurrentPosition();
                txtTotalSongTime.setText(sdf.format(eTime));
                if(oTime == 0){
                    skSongPlayThrough.setMax(eTime);
                    oTime =1;
                }
                txtSongTime.setText(sdf.format(sTime));
                skSongPlayThrough.setProgress(sTime);
        }
    }

    public void playDaSong(int position, boolean onChangePage) {
        if (songs.size() > 0) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Song daSong = songs.get(position);
//            playASong.setCircleImageView(daSong.getImageURL());
            getSupportActionBar().setTitle(daSong.getSongName());
            new PlayMp3().execute(daSong.getMp3URL());
            imgPlay.setImageResource(R.drawable.iconpause);
            if(!onChangePage){
                viewPagerPlayM.setCurrentItem(position);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}