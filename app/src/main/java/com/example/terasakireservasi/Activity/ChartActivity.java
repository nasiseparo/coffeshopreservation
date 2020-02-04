package com.example.terasakireservasi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.terasakireservasi.Adapter.AdapterBasket;
import com.example.terasakireservasi.Adapter.AdapterCartItem;
import com.example.terasakireservasi.Adapter.AdapterOrder;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.Model.Cart;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.example.terasakireservasi.Tools.RecyclerItemTouchHelper;
import com.example.terasakireservasi.Tools.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{


    String ip;
    JSONParser jsonParser = new JSONParser();
    JSONArray myJSON;
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    private static final String TAG_total_belanja = "total_belanja";
    SwipeRefreshLayout swipeRefreshLayout = null;
    int IOConnect = 0;
    TextView txtAlert, totalBelanja;
    ProgressBar prgLoading;
    SharedPrefManager sharedPrefManager;
    String idUser, cUsername,cPassword,cName,cEmail,cTlp,cAlamat;
    ProgressDialog pDialog;
    private int sukses;
    AdapterBasket adapterBasket;
    SwipeMenuListView swipeMenuListView;
    RecyclerView recyclerView;
    long data;
    String strIdItem;
    ArrayList<Cart> listData;
    ImageView imageViewChoose;
    FloatingActionButton btncheckout, btnEmptyCart;
    private List<ModelProduct> productList = new ArrayList<>();
    int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap,decoded;
    Handler mHandler;
    StoreDatabase.DatabaseHelper mDbHelper;
    int bitmap_size = 60;
    SQLiteDatabase mDb;
    StoreDatabase dbHelper;
//    int toqty = 0;
//    int harga =0;
    int hitung=0;
    public static ArrayList<Integer> s_id = new ArrayList<>();
    public static ArrayList<String> s_name = new ArrayList<>();
    public static ArrayList<String> s_image = new ArrayList<>();
    public static ArrayList<String> s_status = new ArrayList<>();
    public static ArrayList<Integer> s_price = new ArrayList<>();
    public static ArrayList<String> s_qty = new ArrayList<>();
    String hasmap;
    ArrayList<HashMap<String, String>> userList;
    TextView total_harga;
    AdapterCartItem adapterCartItem;
    RecyclerView recyclerView1;
    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        ((AppCompatActivity)this).setSupportActionBar(toolbar);
        totalBelanja = (TextView)toolbar.findViewById(R.id.totalBelanja);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(ChartActivity.this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ChartActivity.this);
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
            cUsername = sharedPref.getString("username","");
            cPassword = sharedPref.getString("password","");
            cName = sharedPref.getString("nama","");
            cEmail = sharedPref.getString("email","");
            cTlp = sharedPref.getString("no_tlp","");
            cAlamat = sharedPref.getString("alamat","");
        }

        recyclerView1 = (RecyclerView) findViewById(R.id.listKeranjang);
        txtAlert = (TextView)findViewById(R.id.txtAlert);
        btncheckout = findViewById(R.id.id_chechout);
        btnEmptyCart = findViewById(R.id.id_emptyCart);
        total_harga=findViewById(R.id.total_harga);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        dbHelper = new StoreDatabase(this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userList = dbHelper.GetUsers();
        listData = dbHelper.getCart();
        ArrayList<ArrayList<Object>>  dataCart = dbHelper.getAllData();
        if (dataCart.size()==0){
            txtAlert.setVisibility(View.VISIBLE);
        }
        final ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        adapterCartItem.notifyDataSetChanged();
                        userList.clear();

                    }
                }, 3000);
            }
        });

        clearDataArray();

        ArrayList<Cart> cartList = dbHelper.getCart();
        for (int y=0; y<cartList.size();y++){
            Log.d("item",cartList.get(y).getHarga());
        }

        for (int i=0; i<dataCart.size();i++){
            HashMap<String, String> pi = new HashMap<>();
            ArrayList<Object> row = dataCart.get(i);
            s_id.add(Integer.parseInt(row.get(0).toString()));
            s_name.add(row.get(1).toString());
            s_image.add(row.get(2).toString());
            s_status.add(row.get(3).toString());
            s_price.add(Integer.parseInt(row.get(4).toString()));
            s_qty.add(row.get(5).toString());
            pi.put("",s_name.get(i)+"-"+s_qty.get(i));
            //pi.put("qty",s_qty.get(i));
            hashMaps.add(pi);


        }

        Log.d("hash map : ", hashMaps.toString());

        hasmap = hashMaps.toString();

//        Log.d("Data id : ", s_id.toString());
//        Log.d("Data name : ", s_name.toString());
//        Log.d("Data image : ", s_image.toString());
//        Log.d("Data status : ", s_status.toString());
//        Log.d("Data price : ", s_price.toString());
//        Log.d("Data qty : ", s_qty.toString());

        Log.d("Data Car : ", String.valueOf(dataCart));
      int totalCart = dataCart.size();
      Log.d("Total Cart : ", String.valueOf(totalCart));
      int tmp;
      for (int x=0;x<totalCart;x++){
          int harga = s_price.get(x);
          int toqty = Integer.parseInt(s_qty.get(x));
          hitung += harga * toqty;
      }

       // Log.d("Total harga : ", String.valueOf(harga));
       // Log.d("Total qty : ", String.valueOf(toqty));
        Log.d("jumlah yang dibayar : ", String.valueOf(hitung));

        if (totalCart<=0||hitung<=0){
            total_harga.setVisibility(View.GONE);
        }else{
            total_harga.setText("Total Item : "+totalCart+" | Harga : "+toRupiah(String.valueOf(hitung)));
        }

        adapterCartItem = new AdapterCartItem(ChartActivity.this);
        adapterCartItem.setCartList(listData);
        recyclerView1.setLayoutManager(new LinearLayoutManager(ChartActivity.this));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.addItemDecoration(new DividerItemDecoration(ChartActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView1.setAdapter(adapterCartItem);
        adapterCartItem.notifyDataSetChanged();


            btnEmptyCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hashMaps.size()<=0){
                        Toast.makeText(ChartActivity.this,"Maaf Keranjang Anda Kosong...",Toast.LENGTH_SHORT).show();
                    }else{

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        dbHelper.deleteAllItems();
                                        hashMaps.clear();
                                        hasmap="";
                                        clearDataArray();
                                        Intent i = new Intent(ChartActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Toast.makeText(ChartActivity.this,"Item Berhasil Dihapus",Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
                        builder.setMessage("Yakin Ingin Hapus Semua Item Anda?").setPositiveButton("Ya", dialogClickListener)
                                .setNegativeButton("Tidak", dialogClickListener).show();

                    }

                }
            });


        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hashMaps.size()<=0){
                    Toast.makeText(ChartActivity.this,"Maaf Keranjang Anda Kosong...",Toast.LENGTH_SHORT).show();

                }else{

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    new CekOut().execute();
                                    dbHelper.deleteAllItems();
                                    hashMaps.clear();
                                    clearDataArray();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
                    builder.setMessage("Yakin Checkout Keranjang Anda?").setPositiveButton("Ya", dialogClickListener)
                            .setNegativeButton("Tidak", dialogClickListener).show();

                }


            }
        });
        totalBelanja.setText("Keranjang Belanja");

    }


    private String toRupiah(String nominal){
        String hasil = "";
        DecimalFormat toRupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatAngka = new DecimalFormatSymbols();
        formatAngka.setCurrencySymbol("Rp. ");
        formatAngka.setMonetaryDecimalSeparator(',');
        toRupiah.setDecimalFormatSymbols(formatAngka);
        hasil = toRupiah.format(Double.valueOf(nominal));
        return hasil;
    }



    void clearDataArray(){
        s_id.clear();
        s_qty.clear();
        s_price.clear();
        s_name.clear();
        s_status.clear();
        s_image.clear();
    }

    void LoadData(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip+"get-basket.php?id_user="+idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ModelChart> list = new ArrayList<>();
                try {
                    final JSONObject object = new JSONObject(response);
                    sukses = object.getInt(TAG_SUKSES);
                    if (sukses==1){
                        JSONArray jsonArray = object.getJSONArray("record");
                        Log.d("json array : ",jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            //ModelChart modelChart = new ModelChart(obj);



                          //  list.add(modelChart);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    totalBelanja.setText(object.getString("total_belanja"));

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                    }else if (sukses==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (object.getString("total_belanja").equals("0")){
                                        totalBelanja.setText("Keranjang Anda Kosong");
                                    }



                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

//                    listData.clear();
//                    listData.addAll(list);
//                    adapterBasket.setListData(listData);
//                    adapterBasket.notifyDataSetChanged();
//                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        try {

            TextView tvIdItem = ((TextView) viewHolder.itemView.findViewById(R.id.id_item));
            strIdItem = tvIdItem.getText().toString();
            Log.d("Data Item : ", strIdItem);
            listData.remove(position);
            adapterBasket.notifyDataSetChanged();
            new DeleteItem().execute();
            //LoadData();
            adapterBasket.notifyDataSetChanged();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

//    class load extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//        protected String doInBackground(String... args) {
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//            params.add(new BasicNameValuePair("id_user", idUser));
//            final JSONObject json = jsonParser.httpRequest(ip+"get-basket.php", "GET",params);
//            Log.d("show: ", json.toString());
//            try {
//                int sukses = json.getInt(TAG_SUKSES);
//                final String data = json.getString(TAG_total_belanja);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        totalBelanja.setText(data);
//                    }
//                });
//
//                if (sukses == 1) {
//                    myJSON = json.getJSONArray(TAG_record);
//                    for (int i = 0; i < myJSON.length(); i++) {
//                        JSONObject object = myJSON.getJSONObject(i);
//                        final ModelChart modelChart = new ModelChart(object);
//                        list.add(modelChart);
//
////                        basket_id.add(Long.parseLong(object.getString("id_basket")));
////                        nama_pesanan.add(object.getString("nama_pesanan"));
////                        qty.add(object.getString("qty"));
////                        harga.add(object.getString("total"));
////                        tanggal.add(object.getString("date_made"));
////                        status.add(object.getString("status"));
////                        gambar.add(object.getString("img_category"));
//
//
//
//
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            listData.clear();
//                            listData.addAll(list);
//                            adapterBasket.setListData(listData);
//                            adapterBasket.notifyDataSetChanged();
//                        }
//                    });
//
//                } else {
//
//                }
//
//
//            }
//            catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//        protected void onPostExecute(String file_url) {
//            prgLoading.setVisibility(View.GONE);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    listData.clear();
//                    listData.addAll(list);
//                    adapterBasket.setListData(listData);
//                    adapterBasket.notifyDataSetChanged();
//                }
//            });
//            // if internet connection and data available show data on list
//            // otherwise, show alert text
////            if((basket_id.size() > 0) && (IOConnect == 0)){
////                swipeMenuListView.setVisibility(View.VISIBLE);
////                swipeMenuListView.setAdapter(adapterBasket);
////            }else{
////                txtAlert.setVisibility(View.VISIBLE);
////            }
//
//
//        }
//
//    }

    class DeleteItem extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChartActivity.this);
            pDialog.setMessage("Delete Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_basket", strIdItem));
            params.add(new BasicNameValuePair("id_user", idUser));
            String url=ip+"delete-item.php";
            Log.v("add",url);
            JSONObject json = jsonParser.httpRequest(url,"POST", params);
            Log.d("add", json.toString());
            try {
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    Intent i = getIntent();
                    setResult(100, i);
                    //finish();
                } else {
                    // gagal update data
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

        }
    }

    class CekOut extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChartActivity.this);
            pDialog.setMessage("Check Out Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_user", idUser));
                params.add(new BasicNameValuePair("customer_name",cName));
                params.add(new BasicNameValuePair("contact_number", cTlp));
                params.add(new BasicNameValuePair("address", cAlamat));
                params.add(new BasicNameValuePair("email", cEmail));
                params.add(new BasicNameValuePair("total", String.valueOf(hitung)));
                params.add(new BasicNameValuePair("item", hasmap));
                String url=ip+"process.php";
                Log.v("add",url);
                JSONObject json = jsonParser.httpRequest(url,"POST", params);
                Log.d("add", json.toString());
                try {
                    sukses = json.getInt(TAG_SUKSES);
                    if (sukses == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(ChartActivity.this, "Checkout Berhasil",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ChartActivity.this, CheckOutAtivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        // gagal update data
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }





    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
