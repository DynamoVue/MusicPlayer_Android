package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.AuthenticationActivity;
import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songs;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;
    SearchSongAdapter searchSongAdapter;

    public SearchSongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    //anh xa va gan lai layout cho moi dong item
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dong_search_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.artistName.setText(song.getSinger());
        Picasso.get().load(song.getImageURL()).into(holder.imageViewSongCover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    //dinh dang cho cac item trong recycler view
    //thiet ke va khai bao view thong qua viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName, artistName;
        ImageView imgViewLikes, imageViewSongCover;

        public ViewHolder(View itemView){
            super(itemView);
            songName = itemView.findViewById(R.id.tvSearchSongName);
            artistName = itemView.findViewById(R.id.tvSearchArtistName);
            imgViewLikes = itemView.findViewById(R.id.imageViewSearchLikes);
            imageViewSongCover = itemView.findViewById(R.id.imageViewSearchItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("song", songs.get(getPosition()));
                    context.startActivity(intent);
                }
            });
            //Luot thich
            imgViewLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(), R.style.BottomSheetDialogTheme);
                    Song clickedSong = songs.get(getPosition());
                    imgViewLikes.setImageResource(R.drawable.iconloved);
                    //This part is to update like
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Intent myIntent = new Intent(context, AuthenticationActivity.class);
                        context.startActivity(myIntent);
                        return;
                    }

                    DatabaseReference playlistRef = DATABASE_REFERENCE_USERS.child(user.getUid());

                    playlistRef.child("favoriteSongs").addListenerForSingleValueEvent(new ValueEventListener() {
                        ArrayList<Song> favorSongs = new ArrayList<>();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                favorSongs.add((Song) dataSnapshot.getValue(Song.class));
                            }

                            boolean alreadyContained = false;

                            for (int i = 0; i < favorSongs.size(); i++) {
                                if (favorSongs.get(i).getId().equals(clickedSong.getId())) {
                                    alreadyContained = true;
                                    break;
                                }
                            }

                            if (!alreadyContained) {
                                Map<String, List<Song>> users = new HashMap<>();
                                favorSongs.add(clickedSong);
                                users.put("favoriteSongs", favorSongs);

                                playlistRef.setValue(users);
                            }

                            Toast.makeText(v.getContext(), "Add " + clickedSong.getSongName() + " Successful!", Toast.LENGTH_LONG).show();
                            bottomSheetDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    imgViewLikes.setEnabled(false);
                }
            });
        }
    }
}
