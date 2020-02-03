package com.example.terasakireservasi.Fragment.InOrderFragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
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
import com.example.terasakireservasi.Activity.BookingActivity;
import com.example.terasakireservasi.Adapter.AdapterOrder;
import com.example.terasakireservasi.Adapter.AdapterRiwayatBooking;
import com.example.terasakireservasi.Fragment.FragmentHome;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.Model.Data;
import com.example.terasakireservasi.Model.Riwayat;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.Tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentRiwayatBooking extends Fragment {

    AdapterRiwayatBooking adapterRiwayatBooking;

    String ip;
    JSONParser jsonParser = new JSONParser();
    JSONArray myJSON;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    SwipeRefreshLayout swipeRefreshLayout = null;
    int IOConnect = 0;
    TextView txtAlert;
    ProgressBar prgLoading;
    RecyclerView listBooking;
    String id;
    SharedPrefManager sharedPrefManager;
    protected  ArrayList<Riwayat> riwayatList;
    public FragmentRiwayatBooking() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_riwayat_booking, container, false);
        ip = jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(getActivity());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            getActivity().finish();
        }else {
            id = sharedPref.getString("id_user", "");


        }
        prgLoading = (ProgressBar) view.findViewById(R.id.prgLoading);
        txtAlert = (TextView)view.findViewById(R.id.txtAlert);
        adapterRiwayatBooking = new AdapterRiwayatBooking(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        listBooking = (RecyclerView) view.findViewById(R.id.listRiwayatBooking);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        riwayatList.clear();
                       LoadData();
                        adapterRiwayatBooking.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });
        LoadData();
        riwayatList = new ArrayList<>();
        adapterRiwayatBooking = new AdapterRiwayatBooking(getActivity());
        adapterRiwayatBooking.setListData(riwayatList);
        listBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        listBooking.setItemAnimator(new DefaultItemAnimator());
        listBooking.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        listBooking.setAdapter(adapterRiwayatBooking);

        return view;
    }


    void LoadData(){
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, ip+"get-booking.php?id_user="+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Riwayat> riwayats = new ArrayList<>();
                    try {
                        final JSONObject object = new JSONObject(response);
                       int sukses = object.getInt(TAG_SUKSES);
                        if (sukses==1){
                            JSONArray jsonArray = object.getJSONArray("record");
                            Log.d("json array : ",jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String reserevid = obj.getString("reserve_id");
                                String iduser = obj.getString("id_user");
                                String noofguest = obj.getString("no_of_guest");
                                String date = obj.getString("date_res");
                                String time =obj.getString("time");
                                Riwayat riwayat = new Riwayat(reserevid, iduser, noofguest,
                                        date, time);
                                riwayats.add(riwayat);
                            }

                        }

                        riwayatList.clear();
                        riwayatList.addAll(riwayats);
                        adapterRiwayatBooking.setListData(riwayatList);
                        adapterRiwayatBooking.notifyDataSetChanged();
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

//    class load extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        protected String doInBackground(String... args) {
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("id_user", id));
//            JSONObject json = jsonParser.httpRequest(ip+"get-booking.php", "GET",params);
//            Log.d("show: ", json.toString());
//            final ArrayList<Riwayat> riwayats = new ArrayList<>();
//            try {
//                int sukses = json.getInt(TAG_SUKSES);
//                if (sukses == 1) {
//                    myJSON = json.getJSONArray(TAG_record);
//                    for (int i = 0; i < myJSON.length(); i++) {
//                        JSONObject object = myJSON.getJSONObject(i);
//                        String reserevid = object.getString("reserve_id");
//                        String iduser = object.getString("id_user");
//                        String noofguest = object.getString("no_of_guest");
//                        String date = object.getString("date_res");
//                        String time =object.getString("time");
//                        Riwayat riwayat = new Riwayat(reserevid, iduser, noofguest,
//                                date, time);
//                        riwayats.add(riwayat);
//                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            riwayats.clear();
//                            riwayatList.addAll(riwayats);
//                            adapterRiwayatBooking.setListData(riwayatList);
//                            adapterRiwayatBooking.notifyDataSetChanged();
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    });
//
//                }
//
//            }
//            catch (JSONException e) {e.printStackTrace();}
//            return null;
//        }
//        protected void onPostExecute(String file_url) {
//
//
//        }
//
//    }

}
