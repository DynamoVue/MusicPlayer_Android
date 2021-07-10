package com.example.musicapp.Adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.AuthenticationActivity;
import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Activity.PlaylistActivity;
import com.example.musicapp.AsyncTask.DownloadAsyncTask;
import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareStoryContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> implements FirebaseReference  {
    public List<Song> songs;
    public Context mContext;
    public Song songDisplayMore;
    public PlaylistActivity fragment;
    public String playlistId;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;

    public PlaylistAdapter(List<Song> songs, Context mContext, PlaylistActivity fragment, String playlistId) {
        this.songs = songs;
        this.mContext = mContext;
        this.playlistId = playlistId;
        this.fragment = fragment;
    }

//    private void init() {
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
//                user = FirebaseAuth.getInstance().getCurrentUser();
//            }
//        };
//
//        FirebaseAuth.getInstance().addAuthStateListener(authListener);
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder holder, int position) {
        holder.title.setText(songs.get(position).getSongName());
        holder.desc.setText(songs.get(position).getSingers());
        holder.index.setText(position + 1  + "");
        Picasso.get().load(songs.get(position).getImageURL()).fit().centerCrop().into(holder.imageThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(holder.itemView.getContext(), PlayMusicActivity.class);
                myIntent.putExtra("song", songs.get(position)); //Optional parameters
                holder.itemView.getContext().startActivity(myIntent);
            }
        });

        handleMoreClicked(holder.showMore, holder.itemView, songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, index;
        ImageView imageThumbnail;
        ImageView showMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.cardItemDesc);
            title = itemView.findViewById(R.id.cardItemTitle);
            index = itemView.findViewById(R.id.cardItemIndex);
            imageThumbnail = itemView.findViewById(R.id.cardItemImage);
            showMore = (ImageView) itemView.findViewById(R.id.cardItemMore);
            user = FirebaseAuth.getInstance().getCurrentUser();
        }
    }

    public void returnFromThread(Boolean result, ProgressDialog dialog) {
        Toast.makeText(fragment, "Download Successful!", Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + "." + fileExtension);
        final long downloadId = downloadManager.enqueue(request);

        ProgressDialog dialog = new ProgressDialog(fragment);
        dialog.setMessage("Your file is start downloading!");
        dialog.setTitle("Download " + songDisplayMore.getSongName());
        dialog.setProgressStyle(dialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();

        DownloadAsyncTask downloadTask = new DownloadAsyncTask(this, dialog, downloadManager);
        downloadTask.execute(Long.toString(downloadId));
    }

    private void handleMoreClicked(ImageView view, View itemView, Song currentSong) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDisplayMore = currentSong;

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(view.getContext())
                        .inflate(R.layout.layout_bottom_sheet, (LinearLayout)itemView.findViewById(R.id.bottomSheetContainer));

                ((TextView)bottomSheetView.findViewById(R.id.songTitle)).setText(songDisplayMore.getSongName());
                ((TextView)bottomSheetView.findViewById(R.id.songSingers)).setText(songDisplayMore.getSingers());
                ((ImageView)bottomSheetView.findViewById(R.id.songThumbnail)).setImageDrawable(((ImageView)itemView.findViewById(R.id.cardItemImage)).getDrawable());


                bottomSheetView.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user == null) {
                            Intent myIntent = new Intent(fragment, AuthenticationActivity.class);
                            fragment.startActivity(myIntent);
                            return;
                        }
                        StorageReference httpsReference = storage.getReferenceFromUrl(songDisplayMore.getMp3URL());

                        File localFile = null;
                        try {
                            localFile = File.createTempFile("asd",songDisplayMore.getSongName() + ".mp3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                downloadFile(fragment, songDisplayMore.getSongName() + "", "mp3", Environment.DIRECTORY_DOWNLOADS, songDisplayMore.getMp3URL());
                                bottomSheetDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Log.d("error", exception.getMessage());
                            }
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                                //displaying percentage in progress dialog
                            }
                        });

                    }
                });

                bottomSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user == null) {
                            Intent myIntent = new Intent(fragment, AuthenticationActivity.class);
                            fragment.startActivity(myIntent);
                            return;
                        }
                        bottomSheetDialog.dismiss();
                        BottomSheetDialog nestedBottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                        View nestedBottomSheetView = LayoutInflater.from(view.getContext())
                                .inflate(R.layout.layout_bottom_sheet_sharing, (LinearLayout)itemView.findViewById(R.id.bottomSheetSharingContainer));

                        nestedBottomSheetDialog.setContentView(nestedBottomSheetView);
                        nestedBottomSheetDialog.show();

                        ((TextView)nestedBottomSheetView.findViewById(R.id.nestedSongTitle)).setText(songDisplayMore.getSongName());
                        ((TextView)nestedBottomSheetView.findViewById(R.id.nestedSongSingers)).setText(songDisplayMore.getSingers());
                        ((ImageView)nestedBottomSheetView.findViewById(R.id.nestedSongThumbnail)).setImageDrawable(((ImageView)itemView.findViewById(R.id.cardItemImage)).getDrawable());

                        nestedBottomSheetView.findViewById(R.id.facebookStory).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri imageUri = Uri.parse(songDisplayMore.getImageURL());
                                SharePhoto photo = new SharePhoto.Builder()
                                        .setImageUrl(imageUri)
                                        .build();

                                ShareStoryContent content = new ShareStoryContent.Builder()
                                        .setBackgroundAsset(photo)
                                        .build();
                                ShareDialog dialog = new ShareDialog(fragment);
                                if (dialog.canShow(ShareStoryContent.class)) {
                                    dialog.show(fragment, content);
                                    nestedBottomSheetDialog.dismiss();
                                }
                            }
                        });

                        nestedBottomSheetView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShareLinkContent content = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(songDisplayMore.getMp3URL()))
                                        .build();
                                ShareDialog dialog = new ShareDialog(fragment);
                                if (dialog.canShow(ShareLinkContent.class)) {
                                    dialog.show(fragment, content);
                                    nestedBottomSheetDialog.dismiss();
                                }
                            }
                        });

                        nestedBottomSheetView.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(view.getContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied to clipboard", songDisplayMore.getMp3URL());
                                clipboard.setPrimaryClip(clip);
                                nestedBottomSheetDialog.dismiss();
                            }
                        });
                    }
                });

                bottomSheetView.findViewById(R.id.addFavor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            Intent myIntent = new Intent(fragment, AuthenticationActivity.class);
                            fragment.startActivity(myIntent);
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
                                    if (favorSongs.get(i).getId().equals(songDisplayMore.getId())) {
                                        alreadyContained = true;
                                        break;
                                    }
                                }

                                if (!alreadyContained) {
                                    Map<String, List<Song>> users = new HashMap<>();
                                    favorSongs.add(songDisplayMore);
                                    users.put("favoriteSongs", favorSongs);

                                    playlistRef.setValue(users);
                                }

                                Toast.makeText(v.getContext(), "Add " + songDisplayMore.getSongName() + " Successful!", Toast.LENGTH_LONG).show();
                                bottomSheetDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }


}
