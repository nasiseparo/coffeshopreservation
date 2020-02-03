package com.example.terasakireservasi.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.Model.Cart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.ViewHolder> {

    ArrayList<Cart> cartList;
    Context context;

    public AdapterCartItem(ArrayList<Cart> cartList){
        this.cartList = cartList;
    }

    public AdapterCartItem(Context context){
        this.context = context;
    }

    public void setCartList(ArrayList<Cart> cartList){
        this.cartList = cartList;
    }

    private ArrayList<Cart> getCartList(){
        return cartList;
    }



    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView idItemCart, namaPesanan, jumlahPesanan, hargaPesanan;
        public ViewHolder(View view){
            super(view);
            idItemCart = view.findViewById(R.id.id_item_cart);
            namaPesanan = (TextView) view.findViewById(R.id.nama_pesanan);
            jumlahPesanan = view.findViewById(R.id.jumlah_pesanan);
            hargaPesanan = view.findViewById(R.id.harga_pesanan);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.desain_keranjang, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.idItemCart.setText(getCartList().get(position).getId());
        holder.namaPesanan.setText("Nama Pesanan : "+getCartList().get(position).getNama());
        holder.hargaPesanan.setText("Harga Pesanan : "+getCartList().get(position).getHarga());
        holder.jumlahPesanan.setText("Jumlah Pesanan : "+getCartList().get(position).getQty());
       // Picasso.get().load(Config.PATH_URL_IMG_CAT +product.getImageItem()).into(holder.imgThumb);

    }

    @Override
    public int getItemCount() {
        return getCartList().size();
    }



}