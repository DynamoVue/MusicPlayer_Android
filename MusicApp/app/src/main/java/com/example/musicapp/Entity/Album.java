package com.example.musicapp.Entity;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    private int albumID;
    private String albumName;
    private String albumPicture;
    private String singerName;

    public Album() {
    }

    public Album(int albumID, String albumName, String albumPicture, String singerName) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumPicture = albumPicture;
        this.singerName = singerName;
    }


    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumPicture() {
        return albumPicture;
    }

    public void setAlbumPicture(String albumPicture) {
        this.albumPicture = albumPicture;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumID=" + albumID +
                ", albumName='" + albumName + '\'' +
                ", albumPicture='" + albumPicture + '\'' +
                ", singerName='" + singerName + '\'' +
                '}';
    }
}
