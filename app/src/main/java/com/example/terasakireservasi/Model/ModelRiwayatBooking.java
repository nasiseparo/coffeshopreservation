package com.example.terasakireservasi.Model;

import android.graphics.drawable.Drawable;

public class ModelRiwayatBooking {

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    private String namamenu;
    private String harga;
    private String qty;
    private String desc;
    private String kodebooking;
    private Drawable image;

    public ModelRiwayatBooking(){
        //construkor
    }


    public ModelRiwayatBooking(String namamenu, String harga, String qty, String desc, String kodebooking, Drawable image){
        super();
        this.namamenu = namamenu;
        this.harga = harga;
        this.qty = qty;
        this.desc = desc;
        this.kodebooking = kodebooking;
        this.image = image;
    }

    public String getKodebooking(){return kodebooking;}
    public void  setKodebooking(String kodebooking){this.kodebooking = kodebooking;}
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
