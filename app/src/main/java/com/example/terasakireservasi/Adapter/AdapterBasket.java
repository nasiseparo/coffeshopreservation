package com.example.terasakireservasi.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// adapter class for custom category list
public class AdapterBasket extends RecyclerView.Adapter<AdapterBasket.ViewHolder> {

    private List<ModelProduct> productList;
    Context context;

    private List<ModelProduct> getListData(){
        return this.productList;
    }

    public AdapterBasket(List<ModelProduct> productList){
        this.productList = productList;
    }
    public void setListData(ArrayList<ModelProduct> listData) {
        this.productList = listData;
    }
    public AdapterBasket(Context context){
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_basket, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

//        viewHolder.idItem.setText(getListData().get(i).getId());
//        viewHolder.namaPesanan.setText(getListData().get(i).getNamamenu());
//        //viewHolder.tanggalPesanan.setText(getListData().get(i).;
//        //viewHolder.statusPesanan.setText(getListData().get(i).getStatus());
//        viewHolder.jmlPesanan.setText(getListData().get(i).getQty());
//       // viewHolder.hargaPesanan.setText(getListData().get(i).getTotal());
//        //Picasso.get().load(Config.PATH_URL_IMG_CAT+getListData().get(i).getImage()).into(viewHolder.imgThumb);

    }

    @Override
    public int getItemCount() {
        return getListData().size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaPesanan;
        TextView tanggalPesanan;
        TextView statusPesanan;
        TextView jmlPesanan;
        TextView hargaPesanan;
        TextView idItem;
        ImageView imgThumb;

       public RelativeLayout viewBackground, viewForeground;
        ViewHolder(View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            namaPesanan = (TextView) itemView.findViewById(R.id.nama_pesanan);
            tanggalPesanan = (TextView) itemView.findViewById(R.id.tgl_pesanan);
            statusPesanan = (TextView) itemView.findViewById(R.id.status);
            jmlPesanan = (TextView) itemView.findViewById(R.id.jml_pesanan);
            hargaPesanan = (TextView) itemView.findViewById(R.id.jml_harga);
            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
            idItem = (TextView) itemView.findViewById(R.id.id_item);

        }
    }



}