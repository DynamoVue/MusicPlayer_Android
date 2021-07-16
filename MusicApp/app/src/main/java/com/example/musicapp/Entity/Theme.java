package com.example.musicapp.Entity;

import java.io.Serializable;

public class Theme implements Serializable {
    private String idTheme;
    private String nameTheme;
    private String imageTheme;

    public Theme() {

    }
    public Theme(String idTheme, String imageTheme, String nameTheme) {
        this.idTheme = idTheme;
        this.nameTheme = nameTheme;
        this.imageTheme = imageTheme;
    }

    public String getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(String idTheme) {
        this.idTheme = idTheme;
    }

    public String getNameTheme() {
        return nameTheme;
    }

    public void setNameTheme(String nameTheme) {
        this.nameTheme = nameTheme;
    }

    public String getImageTheme() {
        return imageTheme;
    }

    public void setImageTheme(String imageTheme) {
        this.imageTheme = imageTheme;
    }
}
