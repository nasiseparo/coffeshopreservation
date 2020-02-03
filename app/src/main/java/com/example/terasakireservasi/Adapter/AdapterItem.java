package com.example.terasakireservasi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.terasakireservasi.Activity.DetailRiwayatItem;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.Item;
import com.example.terasakireservasi.R;

import java.util.ArrayList;

// adapter class for custom category list
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {


    ArrayList<Item> listData;
    Context context;

    private ArrayList<Item> getListData(){
        return listData;
    }

    public void setListData(ArrayList<Item> listData) {
        this.listData = listData;
    }
    public AdapterItem(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_item, parent, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

          viewHolder.idItem.setText(getListData().get(i).getItemId());
          viewHolder.namaItem.setText("Nama item : "+getListData().get(i).getItemName());
          viewHolder.jumlahItem.setText("Jumlah item : "+getListData().get(i).getItemQty());

    }

    @Override
    public int getItemCount() {
        return getListData().size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idItem, namaItem, jumlahItem;


        ViewHolder(View itemView) {
            super(itemView);

            idItem = itemView.findViewById(R.id.id_item);
            namaItem = itemView.findViewById(R.id.nama_item);
            jumlahItem = itemView.findViewById(R.id.jumlah_item);


        }
    }



}