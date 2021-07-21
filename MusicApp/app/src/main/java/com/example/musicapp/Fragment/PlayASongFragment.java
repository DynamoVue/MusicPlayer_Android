package com.example.musicapp.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

public class PlayASongFragment extends Fragment {

    ImageView imgView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play_a_song, container, false);
        imgView = view.findViewById(R.id.imageViewCircle);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String url = bundle.getString("url");
            Picasso.get().load(url).fit().centerCrop().into(imgView);
        }
        //This part will make the disk shape poster rotate
//        objectAnimator = ObjectAnimator.ofFloat(imgView, "rotation", 0f, 360f);
//        objectAnimator.setDuration(10000);
//        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator.setInterpolator(new LinearInterpolator());
        return view;
    }

    public void setCircleImageView(String url) {

    }

    public static PlayASongFragment newInstance(String text, String url){
        PlayASongFragment f = new PlayASongFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("url", url);
        f.setArguments(b);
        return f;
    }
}