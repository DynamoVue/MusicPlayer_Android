package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.musicapp.Activity.AuthenticationActivity;
import com.example.musicapp.Activity.FavorSongsActivity;
import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Activity.PlaylistActivity;
import com.example.musicapp.Animation.ItemAnimation;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class PersonalFragment extends Fragment implements FirebaseReference {
    private View view;
    private FirebaseAuth.AuthStateListener authListener;
    private int MY_REQUEST_CODE = 2009;

    private Button btnLogin;
    private TextView userEmail, favMore;
    private ImageView userAvatar;
    private ImageView songAds;

    private List<Song> songs = new ArrayList<Song>();

    private ImageCarousel recentPlayedCarousel, favSongsCarousel;

    FirebaseUser user;

    Runnable runnable;
    Handler handler;
    int _currentItem = 0;

    private void init() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    userEmail.setText(user.getDisplayName());
                    Picasso.get().load(user.getPhotoUrl()).fit().centerCrop().into(userAvatar);
                    btnLogin.setText("Logout");
                    getFavoriteSongs();
                    getRecentPlayed();
                }

                if (user == null) {
                    userEmail.setText("Login");
                    btnLogin.setText("Login");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authListener != null) {
            FirebaseAuth.getInstance().addAuthStateListener(authListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);

        userAvatar = (ImageView) view.findViewById(R.id.userAvatar);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        favMore = (TextView) view.findViewById(R.id.favMore);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        songAds = (ImageView) view.findViewById(R.id.songAds);
        favSongsCarousel = view.findViewById(R.id.favSongsCarousel);
        recentPlayedCarousel = view.findViewById(R.id.carousel);

        ItemAnimation.animateLeftRight(favSongsCarousel, 0);
        ItemAnimation.animateFadeIn(recentPlayedCarousel, 0);

        favMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavorSongsActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Log.d("sign out", "Singing out");
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(PersonalFragment.this.getContext(), gso);
                    mGoogleSignInClient.signOut();
                    FirebaseAuth.getInstance().signOut();
                    return;
                }
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
        init();
        return view;
    }

    private void getFavoriteSongs() {
        DatabaseReference playlistRef = DATABASE_REFERENCE_USERS.child(user.getUid());

        playlistRef.child("favoriteSongs").limitToFirst(5).addListenerForSingleValueEvent(new ValueEventListener() {
            List<CarouselItem> list = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song temp = (Song) dataSnapshot.getValue(Song.class);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("caption", temp.getSongName() + " - " + temp.getSingers());
                    headers.put("songIndex", index + "");

                    songs.add(temp);
                    list.add(new CarouselItem(temp.getImageURL(), temp.getSongName() + " - " + temp.getSingers(), headers));
                }

                if (list.size() > 0) {
                    favSongsCarousel.registerLifecycle(getLifecycle());
                    favSongsCarousel.setData(list);
                }

                favSongsCarousel.setCarouselListener(new CarouselListener() {
                    @Override
                    public ViewBinding onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        return null;
                    }

                    @Override
                    public void onBindViewHolder(ViewBinding viewBinding, CarouselItem carouselItem, int i) {

                    }

                    @Override
                    public void onLongClick(int position, @NotNull CarouselItem dataObject) {
                        // ...
                    }

                    @Override
                    public void onClick(int position, @NotNull CarouselItem carouselItem) {
                        String songIndex = carouselItem.getHeaders().get("songIndex");
                        Intent myIntent = new Intent(PersonalFragment.this.getContext(), PlayMusicActivity.class);
                        myIntent.putExtra("song", songs.get(Integer.parseInt(songIndex)));
                        PersonalFragment.this.getContext().startActivity(myIntent);
                        return;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private View getRecentPlayed() {
        DATABASE_REFERENCE_PLAYLIST.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CarouselItem> list = new ArrayList<>();
                List<Playlist> playlists = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshotChildren : snapshot.getChildren()) {
                            String playlistName = (String)dataSnapshotChildren.child("playlistName").getValue();
                            String playlistUrl = (String)dataSnapshotChildren.child("playlistUrl").getValue();
                            String id = (String)dataSnapshotChildren.child("id").getValue();

                            Map<String, String> headers = new HashMap<>();
                            headers.put("caption", playlistName);
                            headers.put("id", id);

                            playlists.add(new Playlist(id, playlistName, playlistUrl, new ArrayList<>()));
                            list.add(new CarouselItem(playlistUrl, playlistName, headers));
                        }
                };

                if (list.size() > 0)  {
                    recentPlayedCarousel.registerLifecycle(getLifecycle());
                    recentPlayedCarousel.setData(list);
                }

                recentPlayedCarousel.setCarouselListener(new CarouselListener() {
                    @Override
                    public ViewBinding onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        return null;
                    }

                    @Override
                    public void onBindViewHolder(ViewBinding viewBinding, CarouselItem carouselItem, int i) {

                    }

                    @Override
                    public void onLongClick(int position, @NotNull CarouselItem dataObject) {
                        // ...
                    }

                    @Override
                    public void onClick(int position, @NotNull CarouselItem carouselItem) {
                        String playlistId = carouselItem.getHeaders().get("id");
                        Intent myIntent = new Intent(PersonalFragment.this.getContext(), PlaylistActivity.class);
                        myIntent.putExtra("playlistId", playlistId);
                        PersonalFragment.this.getContext().startActivity(myIntent);
                        return;
                    }
                });

                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (playlists.get(_currentItem).getPlaylistUrl() != "") {
                            Picasso.get().load(playlists.get(_currentItem).getPlaylistUrl()).fit().centerCrop().into(songAds);
                            return;
                        }

                        _currentItem++;

                        if (_currentItem >= playlists.size()) {
                            _currentItem = 0;
                        }
                        handler.postDelayed(runnable, 4500);
                    }
                };
                handler.postDelayed(runnable, 4500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        init();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 2009 ) {
            if (data != null) {
                Bundle mBundle = data.getExtras();
                user = mBundle.getParcelable("user");

                Toast.makeText(view.getContext(), "Login Successful!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
