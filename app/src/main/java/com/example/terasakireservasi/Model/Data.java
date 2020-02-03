package com.example.terasakireservasi.Model;

import java.util.ArrayList;

public class Data {

    String id;
    String dateMade;
    String total;
    String status;
    String paymentStatus;

    public Data(){


    }

    public Data(String id, String dateMade, String total, String status, String paymentStatus){
        this.id = id;
        this.dateMade = dateMade;
        this.total = total;
        this.status = status;
        this.paymentStatus=paymentStatus;

    }
    public String getId() {
        return id;
    }

    public String getDateMade() {
        return dateMade;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentStatus(){return paymentStatus;}



}
