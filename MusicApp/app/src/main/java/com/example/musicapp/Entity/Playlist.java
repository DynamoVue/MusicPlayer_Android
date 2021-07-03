package com.example.musicapp.Entity;

import java.io.Serializable;

public class Playlist implements Serializable {
    private String id;
    private String namePlaylist;
    private String imageURLPlaylist;

    public Playlist(String id, String namePlaylist, String imageURLPlaylist) {
        this.id = id;
        this.namePlaylist = namePlaylist;
        this.imageURLPlaylist = imageURLPlaylist;
    }

    public Playlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public String getImageURLPlaylist() {
        return imageURLPlaylist;
    }

    public void setImageURLPlaylist(String imageURLPlaylist) {
        this.imageURLPlaylist = imageURLPlaylist;
    }
}
