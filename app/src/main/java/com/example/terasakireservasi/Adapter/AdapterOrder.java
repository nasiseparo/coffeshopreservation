package com.example.terasakireservasi.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.terasakireservasi.Activity.DetailRiwayatItem;
import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// adapter class for custom category list
public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {


    ArrayList<Data> listData;
    Context context;

    private ArrayList<Data> getListData(){
        return listData;
    }

    public void setListData(ArrayList<Data> listData) {
        this.listData = listData;
    }
    public AdapterOrder(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_riwayat, parent, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (getListData().get(i).getPaymentStatus().equals("1")){
            viewHolder.cardView.setCardBackgroundColor(Color.YELLOW);

            if (getListData().get(i).getStatus().equals("confirmed")){
                viewHolder.cardView.setCardBackgroundColor(Color.GREEN);
            }



        }else {
            viewHolder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
        }


        if (getListData().get(i).getPaymentStatus().equals("1")){
            viewHolder.statusPayment.setText("Status Pembayaran : Berhasil");
        }else{
            viewHolder.statusPayment.setText("Status Pembayaran : Menunggu");
        }

          viewHolder.idPesanan.setText("ID Pesanan : "+"ORD_"+getListData().get(i).getId());
          viewHolder.hargaPesanan.setText("Total Harga : "+getListData().get(i).getTotal());
          viewHolder.statusPesanan.setText("Status Pesanan : "+getListData().get(i).getStatus());

          viewHolder.tanggalPesanan.setText("Tanggal : "+getListData().get(i).getDateMade());


          viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
//                  if (getListData().get(i).getStatus().equals("confirmed")){
//                      Toast.makeText(v.getContext(),"Transaksi ini sudah dikonfirmasi",Toast.LENGTH_SHORT).show();
//                  }else{
                      Intent x = new Intent(v.getContext(), DetailRiwayatItem.class);
                      x.putExtra("id", getListData().get(i).getId());
                      x.putExtra("total",getListData().get(i).getTotal());
                      x.putExtra("status",getListData().get(i).getStatus());
                      x.putExtra("tanggal",getListData().get(i).getDateMade());
                      x.putExtra("payment",getListData().get(i).getPaymentStatus());
                      v.getContext().startActivity(x);
//                      Toast.makeText(v.getContext(), "You clicked " + pos, Toast.LENGTH_SHORT).show();
//                  }

              }
          });

    }

    @Override
    public int getItemCount() {
        return getListData().size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idPesanan, hargaPesanan, statusPesanan, tanggalPesanan, statusPayment;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            idPesanan = itemView.findViewById(R.id.id_pesanan);
            hargaPesanan = itemView.findViewById(R.id.harga_pesanan);
            statusPesanan = itemView.findViewById(R.id.status_pesanan);
            tanggalPesanan = itemView.findViewById(R.id.tanggal_pesanan);
            statusPayment = itemView.findViewById(R.id.status_pembayaran);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   int pos = getAdapterPosition();
//                   if (pos != RecyclerView.NO_POSITION){
//                       Data data = getListData().get(pos);
//                       Toast.makeText(v.getContext(), "You clicked " + data.getTotal(), Toast.LENGTH_SHORT).show();
//                   }
//                }
//            });

        }
    }



}