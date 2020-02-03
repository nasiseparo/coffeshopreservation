package com.example.terasakireservasi.Model;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

public class ModelChart implements Parcelable {


     String id;
    String total;
    String status;
    String datemade;



//    private ArrayList<ModelProduct> cartProducts = new ArrayList<>();
//    private String id;
//    private String idfood;
//    private String idBasket;
//    private String namamenu;
//    private String qty;
//    private String total;
//    private String datemade;
//    private String status;
//    private String image;

//    public ModelProduct getProduct(int position){
//        return this.cartProducts.get(position);
//    }

//
//    public void setProduct(ModelProduct modelProduct){
//        this.cartProducts.add(modelProduct);
//    }
//
//    public int getCartSize(){
//        return this.cartProducts.size();
//    }
//
//    public boolean checkProduct(ModelProduct modelProduct){
//        return this.cartProducts.contains(modelProduct);
//    }

    public ModelChart(){



    }



    public ModelChart(JSONObject object) {
        try{
            String id = object.getString("id");
            String total = object.getString("total");
            String datemade = object.getString("date_made");
            String status = object.getString("status");

           this.id = id;
           this.total = total;
           this.status = status;
           this.datemade = datemade;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ModelChart(Parcel in){

        id = in.readString();
        total = in.readString();
        status = in.readString();


    }

    public static  final Parcelable.Creator<ModelChart> CREATOR = new Parcelable.Creator<ModelChart>() {
        @Override
        public ModelChart createFromParcel(Parcel in) {
            return new ModelChart(in);
        }

        @Override
        public ModelChart[] newArray(int size) {
            return new ModelChart[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatemade() {
        return datemade;
    }

    public void setDatemade(String datemade) {
        this.datemade = datemade;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(total);
        dest.writeString(datemade);
        dest.writeString(status);

    }
}
