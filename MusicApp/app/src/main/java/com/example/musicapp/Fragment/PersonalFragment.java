package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Adapter.BannerAdapter;
import com.example.musicapp.Activity.AuthenticationActivity;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class PersonalFragment extends Fragment implements FirebaseReference {
    private View view;
    private FirebaseAuth.AuthStateListener authListener;
    private int MY_REQUEST_CODE = 2009;

    ViewPager viewPager;
    private Button btnLogin;
    private TextView userEmail;
    private ImageView userAvatar;

    FirebaseUser user;

    CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    Runnable runnable;
    Handler handler;
    int currentItem;

    private void init() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    userEmail.setText(user.getDisplayName());
                    Picasso.get().load(user.getPhotoUrl()).fit().centerCrop().into(userAvatar);
                    btnLogin.setText("Logout");
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
        btnLogin = (Button) view.findViewById(R.id.login);
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.indicatorFirst);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Log.d("sign out", "Singing out");
                    FirebaseAuth.getInstance().signOut();
                    return;
                }
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });

        DATABASE_REFERENCE_MUSIC.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                currentItem = 0;
                List<Song> songList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    songList.add(song);
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
                        if (currentItem >= viewPager.getAdapter().getCount()) {
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
