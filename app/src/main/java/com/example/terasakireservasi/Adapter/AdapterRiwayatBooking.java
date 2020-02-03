package com.example.terasakireservasi.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.terasakireservasi.Model.Riwayat;
import com.example.terasakireservasi.R;

import java.util.ArrayList;

public class AdapterRiwayatBooking extends RecyclerView.Adapter<AdapterRiwayatBooking.ViewHolder>  {

    private ArrayList<Riwayat> list;
    private Context context;

    public ArrayList<Riwayat> getList(){
        return list;
    }

    public void setListData(ArrayList<Riwayat> listdata ){
        this.list = listdata;
    }

    public AdapterRiwayatBooking(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public AdapterRiwayatBooking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_table, parent, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final AdapterRiwayatBooking.ViewHolder viewHolder, final int i) {

        viewHolder.tv_idBooking.setText("ID Booking : "+getList().get(i).getReserveId());
        viewHolder.tv_tableNumber.setText("Table Number : "+getList().get(i).getNoOfguest());
        viewHolder.tv_dateBooked.setText("Date Booked : "+getList().get(i).getDateRes()+"-"+getList().get(i).getTime());


    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_idBooking ,tv_tableNumber ,tv_dateBooked;

        ViewHolder(View itemView) {
            super(itemView);
            tv_idBooking = itemView.findViewById(R.id.idbooking);
            tv_tableNumber = itemView.findViewById(R.id.table_number);
            tv_dateBooked = itemView.findViewById(R.id.tanggal_pesanan);

        }
    }



}