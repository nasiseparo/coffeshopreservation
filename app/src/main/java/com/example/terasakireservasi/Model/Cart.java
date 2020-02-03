package com.example.terasakireservasi.Model;

public class Cart {



    String nama;
    String id;
    String qty;
    String harga;

    public Cart(String nama, String id, String qty, String harga){
        this.nama = nama;
        this.id=id;
        this.qty=qty;
        this.harga=harga;
    }


    public String getNama() {
        return nama;
    }

    public String getId() {
        return id;
    }

    public String getQty() {
        return qty;
    }

    public String getHarga() {
        return harga;
    }


}
