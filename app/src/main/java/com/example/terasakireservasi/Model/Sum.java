package com.example.terasakireservasi.Model;

public class Sum {

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

    private String harga;
    private String qty;
    public Sum(String harga, String qty){
        this.harga = harga;
        this.qty = qty;
    }


}
