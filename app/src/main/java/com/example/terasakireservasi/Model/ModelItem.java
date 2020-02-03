package com.example.terasakireservasi.Model;

public class ModelItem {



    private String id;
    private String qty;
    private String nameItem;

   public ModelItem(String id, String qty, String nameItem){
        this.id = id;
        this.qty=qty;
        this.nameItem=nameItem;
    }

    public String getId() {
        return id;
    }

    public String getQty() {
        return qty;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }
}
