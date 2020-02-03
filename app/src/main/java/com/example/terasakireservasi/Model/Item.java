package com.example.terasakireservasi.Model;

public class Item {

    private String itemId;
    private String itemName;
    private String itemQty;

    public Item(String itemId, String itemName, String itemQty){
        this.itemId=itemId;
        this.itemName=itemName;
        this.itemQty=itemQty;
    }

    public void setId(String itemId){
        this.itemId=itemId;
    }

    public void setItemName(String itemName){
        this.itemName=itemName;
    }

    public void setItemQty(String itemQty){
        this.itemQty=itemQty;
    }

    public String getItemId(){
        return this.itemId;
    }

    public String getItemName(){
        return this.itemName;
    }

    public String getItemQty(){
        return this.itemQty;
    }



}
