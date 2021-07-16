package com.example.musicapp.Entity;

import com.example.musicapp.Fragment.CategoryThemeFragment;

public class Categories {
    private String idCategory;
    private String idTheme;
    private String nameCategory;
    private String imageCategory;

    public Categories(){

    }
    public Categories(String idCategory, String idTheme, String imageCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.idTheme = idTheme;
        this.nameCategory = nameCategory;
        this.imageCategory = imageCategory;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(String idTheme) {
        this.idTheme = idTheme;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }
}
