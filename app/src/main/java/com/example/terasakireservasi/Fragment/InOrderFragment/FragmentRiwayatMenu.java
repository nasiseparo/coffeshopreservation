package com.example.terasakireservasi.Fragment.InOrderFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.terasakireservasi.Activity.ListMenuActivity;
import com.example.terasakireservasi.Activity.UploadActivity;
import com.example.terasakireservasi.Adapter.AdapterBasket;
import com.example.terasakireservasi.Adapter.AdapterOrder;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.ModelChart;
import com.example.terasakireservasi.Model.ModelProduct;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.Tools.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentRiwayatMenu extends Fragment {


    public FragmentRiwayatMenu() {
        // Required empty public constructor
    }
    private static final String TAG_SUKSES = "sukses";
    private int sukses;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    String ip;
    private JSONParser jsonParser = new JSONParser();
    private ArrayList<Data> listData;
    private AdapterOrder adapterOrder;
    SharedPrefManager sharedPrefManager;
    int IOConnect = 0;
    private String idUser;
    Button btnConfirm;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_menu, container, false);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(getActivity());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            getActivity().finish();
        }else {
            idUser = sharedPref.getString("id_user", "");


        }

        recyclerView = view.findViewById(R.id.riwayatpesanan);
        swipeRefreshLayout = view.findViewById(R.id.refreshRiwayatMenu);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        LoadData();
                        adapterOrder.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });

        try {
            LoadData();
            listData = new ArrayList<>();
            adapterOrder = new AdapterOrder(getActivity());
            adapterOrder.setListData(listData);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapterOrder);
        }catch (Exception e){
            e.printStackTrace();
        }





        return view;
    }



    void LoadData(){
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, ip+"riwayat-belanja.php?id_user="+idUser, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Data> list = new ArrayList<>();

                    try {
                        final JSONObject object = new JSONObject(response);
                        sukses = object.getInt(TAG_SUKSES);
                        if (sukses==1){
                            JSONArray jsonArray = object.getJSONArray("record");
                            Log.d("json array : ",jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String id = obj.getString("id");
                                String status = obj.getString("status");
                                String total = obj.getString("total");
                                String date = obj.getString("date_made");
                                String paymentStatus = obj.getString("payment_status");
                                Data data = new Data(id, date, total,status, paymentStatus);
                                Log.d("RIWAYAT MENU: ",obj.toString());

                                list.add(data);
                            }

                        }else if (sukses==0){

                        }

                        listData.clear();
                        listData.addAll(list);
                        adapterOrder.setListData(listData);
                        adapterOrder.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);


        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
