package com.example.terasakireservasi.Model;

import android.graphics.drawable.Drawable;
import android.view.Display;

public class ModelMenu {


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    String namamenu;
    String harga;
    String qty;
    String desc;
    Drawable image;

    public ModelMenu(){
        //construkor
    }


    public ModelMenu(String namamenu, String harga, String qty, String desc, Drawable image){
        super();
        this.namamenu = namamenu;
        this.harga = harga;
        this.qty = qty;
        this.desc = desc;
        this.image = image;
    }

    public String getNamamenu() {
        return namamenu;
    }

    public void setNamamenu(String namamenu) {
        this.namamenu = namamenu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }




}
