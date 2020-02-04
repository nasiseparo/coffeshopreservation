package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terasakireservasi.Adapter.AdapterBasket;
import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.example.terasakireservasi.Tools.SharedPrefManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailMenuActivity extends AppCompatActivity {

    String ip;
    JSONParser jsonParser = new JSONParser();
    String passId, passNamaMenu, passHarga, passDesc, passGambar, passCategory;
    ProgressDialog pDialog;

    ImageView imgPreview;
    TextView txtText, txtSubText;
    ProgressBar prgLoading;
    TextView txtAlert;
    WebView txtDescription;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fbAdd;
    SharedPrefManager sharedPrefManager;
    String idUser, nama, username, password, email, noTlp, alamat;
    int sukses, hitung;
    Button btnPlusQty, btnMinQty, btnAddToCart;
    TextView tvQty;
    String jml;
    int tmpQty=0;
    private static String TAG_SUKSES ="sukses";
    String data;
    private StoreDatabase dbHelper;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        ip = jsonParser.getIP();
        passingData();
        final Intent[] i = {getIntent()};
        data = i[0].getStringExtra("cat");
        sharedPrefManager = new SharedPrefManager(DetailMenuActivity.this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DetailMenuActivity.this);
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
            nama = sharedPref.getString("nama", "");
            username = sharedPref.getString("username", "");
            password = sharedPref.getString("password", "");
            email = sharedPref.getString("email", "");
            noTlp = sharedPref.getString("no_tlp", "");
            alamat = sharedPref.getString("alamat", "");

        }

        dbHelper = new StoreDatabase(this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(passNamaMenu);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_content);
       // fbAdd = (FloatingActionButton)findViewById(R.id.btnAdd);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        btnMinQty = (Button)findViewById(R.id.btn_qty_min);
        btnPlusQty = (Button)findViewById(R.id.btn_qty_plus);
        btnAddToCart = (Button)findViewById(R.id.tambah_keranjang);
        tvQty = (TextView)findViewById(R.id.tv_qty);
//        fbAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                inputDialog();
//            }
//        });
        loadItem();
        ButtonQTy();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jml = tvQty.getText().toString();
                int jumlahQty = Integer.parseInt(jml);
                int hargaItem = Integer.parseInt(passHarga);
                hitung = hargaItem*jumlahQty;
                if (jumlahQty<1){
                    Toast.makeText(DetailMenuActivity.this, "Anda Belum Memilih Item", Toast.LENGTH_SHORT).show();
                }else{
                    String qty = String.valueOf(jumlahQty);
                    dbHelper.addToCart(passNamaMenu,hargaItem,qty,passGambar,passId);
                    //new addData().execute();

//                    String qty = String.valueOf(tmpQty);
//                    ModelProduct modelProduct = new ModelProduct(passId,passNamaMenu,jml);
//                    modelChart.setProduct(modelProduct);
//                    adapterBasket.notifyDataSetChanged();
//
//                    try {
//                        System.out.println("===========");
//                        for (int i=0;i<modelChart.getCartSize();i++){
//                           //    System.out.println(modelChart.getProduct(i));
//                            System.out.println(modelChart.getProduct(i));
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }



                   // hashMap.put(Integer.parseInt(passId),passNamaMenu);
                    Intent i = new Intent(DetailMenuActivity.this, ListMenuActivity.class);
                    i.putExtra("category_name", data);
                    startActivity(i);
                }

            }
        });



    }

    private void ButtonQTy(){

        btnMinQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (tmpQty==0){
                    btnMinQty.setEnabled(false);
                }else{
                    btnMinQty.setEnabled(true);
                    tmpQty--;
                }
                tvQty.setText(Integer.toString(tmpQty));
            }
        });

        btnPlusQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                btnMinQty.setEnabled(true);
                tmpQty++;
                tvQty.setText(Integer.toString(tmpQty));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void loadItem(){

        txtText.setText(passNamaMenu);
        txtSubText.setText("Harga : "+passHarga);
        Picasso.get().load(Config.PATH_URL_IMG_CAT+ passGambar).into(imgPreview);

    }



    class addData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailMenuActivity.this);
            pDialog.setMessage("Menyimpan data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", idUser));
            params.add(new BasicNameValuePair("id_food", passId));
            params.add(new BasicNameValuePair("nama", nama));
            params.add(new BasicNameValuePair("nohp", noTlp));
            params.add(new BasicNameValuePair("alamat", alamat));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("nama_pesanan", passNamaMenu));
            params.add(new BasicNameValuePair("total", String.valueOf(hitung)));
            params.add(new BasicNameValuePair("qty", Integer.toString(tmpQty)));
            params.add(new BasicNameValuePair("img", passGambar));

            String url=ip+"add-menu.php";
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
//            sukses();


        }
    }

//    public void inputDialog(){
//
//        final AlertDialog.Builder alert = new AlertDialog.Builder(DetailMenuActivity.this);
//        alert.setTitle("Order");
//        alert.setMessage("Jumlah Pesanan : ");
//        alert.setCancelable(false);
//        final EditText edtQuantity = new EditText(DetailMenuActivity.this);
//        alert.setView(edtQuantity);
//        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                jml = edtQuantity.getText().toString();
//                int jumlahQty = Integer.parseInt(jml);
//                int hargaItem = Integer.parseInt(passHarga);
//                hitung = hargaItem*jumlahQty;
//                if (jml.length()<1){
//                    Toast.makeText(getApplicationContext(), "Jumlah Tidak Boleh Kosong!!!",Toast.LENGTH_LONG).show();
//
//                }else{
//                    new addData().execute();
//                    Toast.makeText(getApplicationContext(), "Total Belanja Anda : "+hitung,Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
//
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.cancel();
//            }
//        });
//
//        alert.show();
//    }

    public void passingData(){

        Intent iGet = getIntent();
        passId = iGet.getStringExtra("id");
        passNamaMenu = iGet.getStringExtra("nama_menu");
        passHarga = iGet.getStringExtra("harga");
        passDesc = iGet.getStringExtra("desc");
        passGambar =  iGet.getStringExtra("img");
        passCategory = iGet.getStringExtra("cat");

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(DetailMenuActivity.this, ListMenuActivity.class);
            i.putExtra("category_name", passCategory);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
