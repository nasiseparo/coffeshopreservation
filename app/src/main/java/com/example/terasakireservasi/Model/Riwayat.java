package com.example.terasakireservasi.Model;

public class Riwayat {


    private String reserveId;
    private String idUser;
    private String noOfguest;
    private String dateRes;
    private String time;

    public Riwayat(String reserveId, String idUser, String noOfguest,
                   String dateRes, String time){
        this.reserveId=reserveId;
        this.idUser=idUser;
        this.noOfguest=noOfguest;
        this.dateRes=dateRes;
        this.time=time;

    }

    public String getReserveId() {
        return reserveId;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getNoOfguest() {
        return noOfguest;
    }



    public String getDateRes() {
        return dateRes;
    }

    public String getTime() {
        return time;
    }




}
