package com.example.musicapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Entity.Playlist;
import com.example.musicapp.Entity.Song;
import com.example.musicapp.Fragment.PlaylistFragment;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.facebook.FacebookSdk;
import com.facebook.share.internal.ShareStoryFeature;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareStoryContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> implements FirebaseReference  {
    public List<Song> songs;
    public Context mContext;
    public Song songDisplayMore;
    public androidx.fragment.app.Fragment fragment;

    public PlaylistAdapter(List<Song> songs, Context mContext, Fragment fragment) {
        this.songs = songs;
        this.mContext = mContext;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder holder, int position) {
        songDisplayMore = songs.get(position);
        holder.title.setText(songs.get(position).getSongName());
        holder.desc.setText(songs.get(position).getSingers());
        holder.index.setText(position + 1  + "");
        Picasso.get().load(songs.get(position).getImageURL()).fit().centerCrop().into(holder.imageThumbnail);
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

            handleMoreClicked(showMore, itemView);
        }
    }

    private void handleMoreClicked(ImageView view, View itemView) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(view.getContext())
                        .inflate(R.layout.layout_bottom_sheet, (LinearLayout)itemView.findViewById(R.id.bottomSheetContainer));

                ((TextView)bottomSheetView.findViewById(R.id.songTitle)).setText(songDisplayMore.getSongName());
                ((TextView)bottomSheetView.findViewById(R.id.songSingers)).setText(songDisplayMore.getSingers());
                ((ImageView)bottomSheetView.findViewById(R.id.songThumbnail)).setImageDrawable(((ImageView)itemView.findViewById(R.id.cardItemImage)).getDrawable());


                bottomSheetView.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference httpsReference = storage.getReferenceFromUrl(songDisplayMore.getMp3URL());
                        ProgressDialog dialog = new ProgressDialog(fragment.getContext());
                        dialog.setMessage("Your file is start downloading!");
                        dialog.setTitle("Download " + songDisplayMore.getSongName());
                        dialog.setProgressStyle(dialog.STYLE_SPINNER);
                        dialog.setCancelable(false);
                        dialog.show();
                        try {
                            File localFile = File.createTempFile("audioFile", "mp3");
                            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    Toast.makeText(view.getContext(), "Download Successful!", Toast.LENGTH_LONG).show();
                                    bottomSheetDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    //displaying percentage in progress dialog
                                    dialog.setMessage("Downloaed " + ((int) progress) + "%...");
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                bottomSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied to clipboard", songDisplayMore.getMp3URL());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(view.getContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                                nestedBottomSheetDialog.dismiss();
                            }
                        });
                    }
                });

                bottomSheetView.findViewById(R.id.addFavor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(), "Add Favor is Clicked", Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }


}
