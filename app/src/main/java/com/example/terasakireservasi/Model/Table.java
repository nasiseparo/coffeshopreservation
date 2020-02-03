package com.example.terasakireservasi.Model;

public class Table {

    private String idTable;
    private String seatNo;
    private String status;


    public Table(String idTable, String seatNo, String status){

        this.idTable = idTable;
        this.seatNo = seatNo;
        this.status = status;

    }

    public String getIdTable() {
        return idTable;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getStatus() {
        return status;
    }



}
