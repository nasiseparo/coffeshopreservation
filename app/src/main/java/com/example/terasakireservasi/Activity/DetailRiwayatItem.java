package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.terasakireservasi.Adapter.AdapterItem;
import com.example.terasakireservasi.Adapter.AdapterOrder;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.Item;
import com.example.terasakireservasi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailRiwayatItem extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    String ip, getId, getTotal, getStatus, getTanggal, getPayment;
    CardView cardView, cardBtnKonfirmasi;
    TextView tvIdpesanan, tvHarga, tvTanggalpesanan, tvStatuspesanan, tvNamaitempesanan;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    private int sukses;
    private TextView tvKonfirmasipesanan;
    JSONArray myJSON;
    AdapterItem adapterItem;
    private ArrayList<Item> itemData;
    RecyclerView recyclerView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_item);
        ip=jsonParser.getIP();

        tvIdpesanan = findViewById(R.id.id_pesanan_detail);
        tvHarga = findViewById(R.id.id_harga);
        tvTanggalpesanan = findViewById(R.id.id_tanggal_pesanan);
        tvStatuspesanan = findViewById(R.id.id_status_pesanan);
        tvNamaitempesanan = findViewById(R.id.id_item_pesanan);
        recyclerView = findViewById(R.id.lv_detail_item);
        Intent i = getIntent();
        getId = i.getStringExtra("id");
        getStatus = i.getStringExtra("status");
        getTotal = i.getStringExtra("total");
        getTanggal = i.getStringExtra("tanggal");
        getPayment = i.getStringExtra("payment");

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        cardView = findViewById(R.id.btn_cardview_oke);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailRiwayatItem.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        cardBtnKonfirmasi = findViewById(R.id.btn_konfirmasi_pesanan);
        tvKonfirmasipesanan = findViewById(R.id.tv_tv_konfirmasi_pesanan);
        if (getPayment.equals("1")){
            tvKonfirmasipesanan.setText("Pesanan sudah dikonfirmasi");
            cardBtnKonfirmasi.setCardBackgroundColor(Color.GREEN);
            cardBtnKonfirmasi.setEnabled(false);
        }else{
            cardBtnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(DetailRiwayatItem.this, UploadActivity.class);
                    i.putExtra("id",getId);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);


                }
            });
        }

        tvIdpesanan.setText("ID Pesanan : "+"ORD_"+getId);
        tvHarga.setText("Harga : "+getTotal);
        tvTanggalpesanan.setText("Tanggal : "+getTanggal);
        tvStatuspesanan.setText("Status : "+getStatus);

        LoadData();
        itemData = new ArrayList<>();
        adapterItem = new AdapterItem(DetailRiwayatItem.this);
        adapterItem.setListData(itemData);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailRiwayatItem.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(DetailRiwayatItem.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterItem);
        adapterItem.notifyDataSetChanged();

    }


    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", getId));
            JSONObject json = jsonParser.httpRequest(ip+"get-order-item.php", "GET",params);
            Log.d("show: ", json.toString());

            try {
                    ArrayList<Item> items = new ArrayList<>();
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        final JSONObject object = myJSON.getJSONObject(i);
                        String idItem = object.getString("item_id");
                        String namaItem = object.getString("food");
                        String qtyItem = object.getString("qty");
                        Item item = new Item(idItem, namaItem, qtyItem);
                        items.add(item);

                }


                itemData.clear();
                items.addAll(items);
                adapterItem.setListData(itemData);
                adapterItem.notifyDataSetChanged();

            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {


        }

    }


    void LoadData(){
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, ip+"get-order-item.php?id="+getId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Item> items = new ArrayList<>();
                    try {
                        final JSONObject object = new JSONObject(response);
                        sukses = object.getInt(TAG_SUKSES);
                        if (sukses==1){
                            JSONArray jsonArray = object.getJSONArray("record");
                            Log.d("json array : ",jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String idItem = obj.getString("item_id");
                                String namaItem = obj.getString("food");
                                String qtyItem = obj.getString("qty");
                                Item item = new Item(idItem, namaItem, qtyItem);
                                items.add(item);
                            }

                        }else if (sukses==0){

                        }

                        itemData.clear();
                        itemData.addAll(items);
                        adapterItem.setListData(itemData);
                        adapterItem.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(DetailRiwayatItem.this);
            requestQueue.add(stringRequest);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
