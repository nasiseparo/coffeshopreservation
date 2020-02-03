package com.example.terasakireservasi.Model;

import android.graphics.drawable.Drawable;

public class ModelCategory {
    String title;
    Drawable img;


    public ModelCategory(){
        //for constructor
    }

    public ModelCategory(String title, Drawable img){
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
