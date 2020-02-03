package com.example.terasakireservasi.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.terasakireservasi.Activity.ChartActivity;
import com.example.terasakireservasi.Activity.ListMenuActivity;
import com.example.terasakireservasi.Adapter.AdapterCategoryList;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentHome extends Fragment {

    String ip;
    JSONParser jsonParser = new JSONParser();
    JSONArray myJSON;
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    GridView listCategory;
    ProgressBar prgLoading;
    TextView txtAlert;
    ImageView btnChart;

    AdapterCategoryList adapterCategoryList;

    public static ArrayList<Long> Id_Category = new ArrayList<Long>();
    public static ArrayList<String> Nama_Category = new ArrayList<String>();
    public static ArrayList<String> Img_Category = new ArrayList<String>();
    SwipeRefreshLayout swipeRefreshLayout = null;
    int IOConnect = 0;



    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ip=jsonParser.getIP();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarHome);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.textToolbar);
        textView.setText("Home");
        prgLoading = (ProgressBar) view.findViewById(R.id.prgLoading);
        listCategory = (GridView)view.findViewById(R.id.listCategory);
        txtAlert = (TextView)view.findViewById(R.id.txtAlert);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        adapterCategoryList = new AdapterCategoryList(getActivity());
        clearAdpter();
        new load().execute();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        listCategory.invalidateViews();
                        clearAdpter();
                        new load().execute();

                    }
                }, 3000);
            }
        });

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"You Selected"+Nama_Category.get(i),Toast.LENGTH_SHORT).show();
                Intent xi = new Intent(getActivity(), ListMenuActivity.class);
                xi.putExtra("category_id", Id_Category.get(i));
                xi.putExtra("category_name", Nama_Category.get(i));
                xi.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                xi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                xi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(xi);
            }
        });

        listCategory.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listCategory != null && listCategory.getChildCount() > 0) {
                    boolean firstItemVisible = listCategory.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listCategory.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        btnChart = (ImageView)view.findViewById(R.id.idChart);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ChartActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        return view;
    }

    void clearAdpter(){
        Id_Category.clear();
        Nama_Category.clear();
        Img_Category.clear();
    }



    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.httpRequest(ip+"get-all-category.php", "GET",params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject object = myJSON.getJSONObject(i);

                        Id_Category.add(Long.parseLong(object.getString("id_category")));
                        Nama_Category.add(object.getString("category_name"));
                        Img_Category.add(object.getString("img_category"));
                        Log.d("Category name", Nama_Category.get(i));


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
            if((Id_Category.size() > 0) && (IOConnect == 0)){
                listCategory.setVisibility(View.VISIBLE);
                listCategory.setAdapter(adapterCategoryList);
            }else{
                txtAlert.setVisibility(View.VISIBLE);
            }


        }

    }

    @Override
    public void onDestroy() {
        listCategory.setAdapter(null);
        super.onDestroy();
    }
}
