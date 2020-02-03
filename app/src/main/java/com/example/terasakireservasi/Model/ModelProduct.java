package com.example.terasakireservasi.Model;



public class ModelProduct {

    private String idItem;
    private String priceItem;
    private String nameItem;
    private String qtyItem;
    private String imageItem;

    public  ModelProduct(){ }

    public ModelProduct(String idItem, String priceItem, String nameItem, String qtyItem, String imageItem){
        this.idItem = idItem;
        this.priceItem = priceItem;
        this.nameItem = nameItem;
        this.qtyItem = qtyItem;
        this.imageItem = imageItem;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(String priceItem) {
        this.priceItem = priceItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public String getQtyItem() {
        return qtyItem;
    }

    public void setQtyItem(String qtyItem) {
        this.qtyItem = qtyItem;
    }

    public String getImageItem() {
        return imageItem;
    }

    public void setImageItem(String imageItem) {
        this.imageItem = imageItem;
    }





}
