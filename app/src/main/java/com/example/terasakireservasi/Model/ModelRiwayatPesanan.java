package com.example.terasakireservasi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class ModelRiwayatPesanan implements Parcelable {

    private String id;
    private String idfood;
    private String idBasket;
    private String namamenu;
    private String qty;
    private String total;
    private String datemade;
    private String status;
    private String image;

    public ModelRiwayatPesanan(JSONObject object){
        try {
            String idfood = object.getString("id_food");
            String idBasket = object.getString("id_basket");
            String namamenu = object.getString("nama_pesanan");
            String qty = object.getString("qty");
            String total = object.getString("total");
            String datemade = object.getString("date_made");
            String status = object.getString("status");
            String image = object.getString("image");

            //this.id = id;
            this.idfood = idfood;
            this.idBasket = idBasket;
            this.namamenu = namamenu;
            this.qty = qty;
            this.total = total;
            this.datemade = datemade;
            this.status = status;
            this.image = image;

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    protected ModelRiwayatPesanan(Parcel in) {
        id = in.readString();
        idfood = in.readString();
        idBasket = in.readString();
        namamenu = in.readString();
        qty = in.readString();
        total = in.readString();
        datemade = in.readString();
        status = in.readString();
        image = in.readString();
    }

    public static final Creator<ModelRiwayatPesanan> CREATOR = new Creator<ModelRiwayatPesanan>() {
        @Override
        public ModelRiwayatPesanan createFromParcel(Parcel in) {
            return new ModelRiwayatPesanan(in);
        }

        @Override
        public ModelRiwayatPesanan[] newArray(int size) {
            return new ModelRiwayatPesanan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idfood);
        dest.writeString(idBasket);
        dest.writeString(namamenu);
        dest.writeString(qty);
        dest.writeString(total);
        dest.writeString(datemade);
        dest.writeString(status);
        dest.writeString(image);
    }
}
