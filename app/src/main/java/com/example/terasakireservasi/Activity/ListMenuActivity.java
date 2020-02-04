package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.terasakireservasi.Adapter.AdapterListMenu;
import com.example.terasakireservasi.Fragment.FragmentHome;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.example.terasakireservasi.Tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListMenuActivity extends AppCompatActivity {

    String ip;
    JSONParser jsonParser = new JSONParser();
    int IOConnect = 0;
    ProgressBar prgLoading;
    ListView listMenu;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    AdapterListMenu adapterListMenu;
    final Fragment fragment1 = new FragmentHome();
    public static ArrayList<Long> ID_MENU = new ArrayList<>();
    public static ArrayList<String> NAMA_MENU = new ArrayList<>();
    public static ArrayList<String> HARGA = new ArrayList<>();
    public static ArrayList<String> DESKRIPSI = new ArrayList<>();
    public static ArrayList<String> IMAGE = new ArrayList<>();

    public static String TAG_SUKSES = "sukses";
    public static  String TAG_record = "record";
    JSONArray myJSON;
    SharedPrefManager sharedPrefManager;
    String data;
    String urlSearch;
    String urlNoSearch, idUser;
    String passingNamaCategory;
    TextView tvOrder;
    StoreDatabase dbHelper;
    ChartActivity chartActivity = new ChartActivity();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        ip = jsonParser.getIP();
        Intent iGet = getIntent();
        //Category_ID = iGet.getLongExtra("category_id",0);
        passingNamaCategory = iGet.getStringExtra("category_name");
        //MenuAPI += Category_ID;
        sharedPrefManager = new SharedPrefManager(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            finish();
        }else {
            idUser = sharedPref.getString("id_user", "");

        }

        dbHelper = new StoreDatabase(this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listMenu = (ListView) findViewById(R.id.listMenu);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        tvOrder = (TextView)findViewById(R.id.tv_order);
        adapterListMenu = new AdapterListMenu(ListMenuActivity.this);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent n = new Intent(ListMenuActivity.this, DetailMenuActivity.class);
                n.putExtra("id", ID_MENU.get(i).toString());
                n.putExtra("nama_menu", NAMA_MENU.get(i));
                n.putExtra("harga", HARGA.get(i));
                n.putExtra("desc", DESKRIPSI.get(i));
                n.putExtra("img", IMAGE.get(i));
                n.putExtra("cat", passingNamaCategory);
                n.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(n);
            }
        });

        int order= dbHelper.getTotalItemsCount();
        String totalItem = String.valueOf(order);
        tvOrder.setText("Your Order : "+totalItem);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        listMenu.invalidateViews();
                        clearData();
                        new load().execute();
                    }
                }, 3000);
            }
        });
        clearData();
        new load().execute();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (data.length()<1){
                    clearData();
                    new load().execute();
                }else{
                    clearData();
                    new search().execute();
                }
            }
        });
        //LoadData();
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListMenuActivity.this,ChartActivity.class);
                startActivity(i);
            }
        });

    }

    void LoadData(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip+"get-basket.php?id_user="+idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject object = new JSONObject(response);
                    int  sukses = object.getInt(TAG_SUKSES);
                    if (sukses==1){
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                try {
                                    if (object.get("jumlah_item").equals("0")){
                                        tvOrder.setVisibility(View.GONE);
                                    }else{
                                        tvOrder.setVisibility(View.VISIBLE);
                                        tvOrder.setText("Total Item : "+object.getString("jumlah_item")+" | Harga : "+object.get("total_belanja"));
                                    }

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
                                        tvOrder.setVisibility(View.GONE);
                                    }



                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }



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


//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent i = new Intent(ListMenuActivity.this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            clearData();
//            startActivity(i);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void getData(){
        data = edtKeyword.getText().toString().trim();
    }

    void  clearData(){
        ID_MENU.clear();
        NAMA_MENU.clear();
        HARGA.clear();
        DESKRIPSI.clear();
        IMAGE.clear();
    }

    class search extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category_search", passingNamaCategory));
            params.add(new BasicNameValuePair("search", data));
            JSONObject json = jsonParser.httpRequest(ip+"get-all-menu.php", "POST",params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject object = myJSON.getJSONObject(i);
                        ID_MENU.add(Long.parseLong(object.getString("id")));
                        NAMA_MENU.add(object.getString("food_name"));
                        HARGA.add(object.getString("food_price"));
                        DESKRIPSI.add(object.getString("food_description"));
                        IMAGE.add(object.getString("img_category"));

                      // Log.d("MENU ID", ID_MENU.toString());


                    }
                } else {

                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {
            prgLoading.setVisibility(View.GONE);

            // if internet connection and data available show data on list
            // otherwise, show alert text
            if((ID_MENU.size() > 0) && (IOConnect == 0)){
                listMenu.setVisibility(View.VISIBLE);
                listMenu.setAdapter(adapterListMenu);
            }else{
                txtAlert.setVisibility(View.VISIBLE);
            }


        }

    }

    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", passingNamaCategory));
            JSONObject json = jsonParser.httpRequest(ip+"get-all-menu.php", "POST",params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject object = myJSON.getJSONObject(i);

                        ID_MENU.add(Long.parseLong(object.getString("id")));
                        NAMA_MENU.add(object.getString("food_name"));
                        HARGA.add(object.getString("food_price"));
                        DESKRIPSI.add(object.getString("food_description"));
                        IMAGE.add(object.getString("img_category"));
                        Log.d("ID MENU", ID_MENU.toString());
//                        Log.d("Category name", Nama_Category.get(i));


                    }
                } else {

                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(String file_url) {
            prgLoading.setVisibility(View.GONE);

            // if internet connection and data available show data on list
            // otherwise, show alert text
            if((ID_MENU.size() > 0) && (IOConnect == 0)){
                listMenu.setVisibility(View.VISIBLE);
                listMenu.setAdapter(adapterListMenu);
            }else{
                txtAlert.setVisibility(View.VISIBLE);
            }


        }

    }


}
