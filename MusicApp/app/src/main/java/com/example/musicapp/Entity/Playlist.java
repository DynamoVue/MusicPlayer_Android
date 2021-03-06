package com.example.musicapp.Entity;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {
    private String id;
    private String playlistName;
    private String playlistUrl;
    private List<String> songs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistUrl() {
        return playlistUrl;
    }

    public void setPlaylistUrl(String playlistUrl) {
        this.playlistUrl = playlistUrl;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public Playlist() {

    }

    public Playlist(String id, String playlistName, String playlistUrl, List<String> songs) {
        this.id = id;
        this.playlistName = playlistName;
        this.playlistUrl = playlistUrl;
        this.songs = songs;
    }
}
