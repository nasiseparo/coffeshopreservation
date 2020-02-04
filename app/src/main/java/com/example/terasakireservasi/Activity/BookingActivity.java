package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.Model.Table;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.example.terasakireservasi.Tools.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    String posisi="0";
    String IP;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    private static String TAG_SUKSES ="sukses";
    private static String TAG_RECORD ="record";
    int sukses;
    int jd=0;
    String dataTable;
    SharedPrefManager sharedPrefManager;
    Button chair1, chair2, chair3, chair4, chair5, chair6, chair7, chair8, chair9, chair10, chair11, chair12, chair13, chair14, chair15;
    String idUser, nama, username, password, email, noTlp, alamat;
    int[]arNo;
    JSONArray myJSON = null;
    private StoreDatabase dbHelper;
    public static final String TIME_DIALOG_ID = "timePicker";
    public static final String DATE_DIALOG_ID = "datePicker";
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;
    static CardView btnDate, btnTime;
    static TextView tvSetdate, tvSettime;
    public static HashMap<String, String> listTable = new HashMap<>();
    public static ArrayList<Integer> id = new ArrayList<>();
    public static ArrayList<String> seatno = new ArrayList<>();
    public static ArrayList<String> status = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> price = new ArrayList<>();
    TextView tvCountTable;
    CardView btnHapus, btnCekout;
    //from database
    public static ArrayList<String> db_seatno = new ArrayList<>();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        IP = jsonParser.getIP();
        setContentView(R.layout.activity_booking);
        db_seatno.clear();
        id.clear();
        seatno.clear();
        status.clear();
        time.clear();
        price.clear();
        sharedPrefManager = new SharedPrefManager(BookingActivity.this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(BookingActivity.this);
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
        tvCountTable = findViewById(R.id.tv_counttable);

        dbHelper = new StoreDatabase(BookingActivity.this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setButton();
        btnDate = findViewById(R.id.btn_setdate);
        btnTime = findViewById(R.id.btn_settime);
        btnHapus = findViewById(R.id.hapustable);
        btnCekout = findViewById(R.id.btn_cekout);
        tvSetdate = findViewById(R.id.tv_setdate);
        tvSettime = findViewById(R.id.tv_settime);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new BookingActivity.DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new BookingActivity.TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            }
        });
        Log.d("data has : ",listTable.toString());

        ArrayList<ArrayList<Object>> listBookTable = dbHelper.getAllBookTable();
        Log.d("List Book Tabel", listBookTable.toString());
//        int res = dbHelper.getTotalBookTable();
//
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
//
//
        for (int i=0;i<listBookTable.size();i++){
            HashMap<String, String > list = new HashMap<>();
            ArrayList<Object> row = listBookTable.get(i);
            id.add(Integer.parseInt(row.get(0).toString()));
            seatno.add(row.get(1).toString());
            time.add(row.get(2).toString());
            time.add(row.get(3).toString());
            price.add(row.get(4).toString());
            list.put("table",seatno.get(i));
            hashMaps.add(list);

        }


        if (listBookTable.size()<=0){
            new SeatLoad().execute();
        }else{
            new SeatLoad().execute();
            for (int data = 0; data<seatno.size();data++)
            {
                setChair(Integer.parseInt(seatno.get(data)));
            }
        }
        int hitung = 0;
        for (int p=0;p<price.size();p++){
            hitung += Integer.parseInt(price.get(p));
        }

        Log.d("hitung : ",String.valueOf(hitung));
        Log.d("Hashmap Book Table : ",hashMaps.toString());
        dataTable = hashMaps.toString();

        int data = listBookTable.size();
        tvCountTable.setText("Jumlah Table Dipilih : "+String.valueOf(data));
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.clear();
                status.clear();
                time.clear();
                price.clear();
                seatno.clear();
                dbHelper.deleteAllTable();
                Toast.makeText(BookingActivity.this,"Hapus Berhasil",Toast.LENGTH_SHORT).show();
                Intent i = getIntent();
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(i);
            }
        });

        btnCekout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSetdate.getText().toString().equals("Pilih Tanggal")){
                    Toast.makeText(BookingActivity.this,"Silahkan Pilih Tanggal...",Toast.LENGTH_SHORT).show();

                }else if (tvSettime.getText().toString().equals("Pilih Jam")){
                    Toast.makeText(BookingActivity.this,"Silahkan Pilih Jam...",Toast.LENGTH_SHORT).show();

                }else{
                    new save().execute();
                    dbHelper.deleteAllTable();
                    Toast.makeText(BookingActivity.this,"Check Out Berhasil",Toast.LENGTH_SHORT).show();
                    Intent i = getIntent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(i);
                }
            }
        });

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            tvSetdate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }



    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        dialog.show();

    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // get selected time
            mHour = hourOfDay;
            mMinute = minute;

            // show selected time to time button
            tvSettime.setText(new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)).append(":")
                    .append("00"));
        }
    }


    private static String pad(int c) {
        if (c >= 10){
            return String.valueOf(c);
        }else{
            return "0" + String.valueOf(c);
        }
    }



    public void setButton(){

        chair1 = (Button)findViewById(R.id.chair1);
        chair1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("1");
            }
        });
        chair2 = (Button)findViewById(R.id.chair2);
        chair2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("2");
            }
        });
        chair3 = (Button)findViewById(R.id.chair3);
        chair3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("3");
            }
        });
        chair4 = (Button)findViewById(R.id.chair4);
        chair4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("4");
            }
        });
        chair5 = (Button)findViewById(R.id.chair5);
        chair5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("5");
            }
        });
        chair6 = (Button)findViewById(R.id.chair6);
        chair6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("6");
            }
        });
        chair7 = (Button)findViewById(R.id.chair7);
        chair7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("7");
            }
        });
        chair8 = (Button)findViewById(R.id.chair8);
        chair8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("8");
            }
        });
        chair9 = (Button)findViewById(R.id.chair9);
        chair9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("9");
            }
        });
        chair10 = (Button)findViewById(R.id.chair10);
        chair10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("10");
            }
        });
        chair11 = (Button)findViewById(R.id.chair11);
        chair11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("11");
            }
        });
        chair12 = (Button)findViewById(R.id.chair12);
        chair12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("12");
            }
        });
        chair13 = (Button)findViewById(R.id.chair13);
        chair13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("13");
            }
        });
        chair14 = (Button)findViewById(R.id.chair14);
        chair14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("14");
            }
        });
        chair15 = (Button)findViewById(R.id.chair15);
        chair15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order("15");
            }
        });
    }

    class SeatLoad extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingActivity.this);
            pDialog.setMessage("Load data. Silahkan Tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.httpRequest(IP+"table-data.php", "GET", params);
            Log.d("Seat : : ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_RECORD);

                    for (int i = 0; i < myJSON.length(); i++) {
                        JSONObject c = myJSON.getJSONObject(i);
                        String noTable = c.getString("no_table");
                        db_seatno.add(noTable);

                    }

                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Log.d("SEAT ", db_seatno.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   for (int i = 0; i<db_seatno.size();i++){
                      setChair(Integer.parseInt(db_seatno.get(i)));
                   }

                }
            });


//            runOnUiThread(new Runnable() {
//                public void run() {
//
//                    for(int i=0;i<jd;i++){
//                        Log.v("Baca","#"+arNo[i]);
//                        setChair(arNo[i]);
//                    }
//                }
//            });
        }
    }


    @SuppressLint("NewApi")
    public void setChair(int number){
        if (number==1){
            Context context;
            chair1.setBackgroundResource(R.drawable.chairgreen);
            chair1.setEnabled(false);
        }else if(number==2){
            chair2.setBackgroundResource(R.drawable.chairgreen);
            chair2.setEnabled(false);
        }else if(number==3){
            chair3.setBackgroundResource(R.drawable.chairgreen);
            chair3.setEnabled(false);
        }else if(number==4){
            chair4.setBackgroundResource(R.drawable.chairgreen);
            chair4.setEnabled(false);
        }else if (number==5){
            chair5.setBackgroundResource(R.drawable.chairgreen);
            chair5.setEnabled(false);
        }else if (number==6){
            chair6.setBackgroundResource(R.drawable.chairgreen);
            chair6.setEnabled(false);
        }else if (number==7){
            chair7.setBackgroundResource(R.drawable.chairgreen);
            chair7.setEnabled(false);
        }else if (number==8){
            chair8.setBackgroundResource(R.drawable.chairgreen);
            chair8.setEnabled(false);
        }else if (number==9){
            chair9.setBackgroundResource(R.drawable.chairgreen);
            chair9.setEnabled(false);
        }else if (number==10){
            chair10.setBackgroundResource(R.drawable.chairgreen);
            chair10.setEnabled(false);
        }else if (number==11){
            chair11.setBackgroundResource(R.drawable.chairgreen);
            chair11.setEnabled(false);
        }else if (number==12){
            chair12.setBackgroundResource(R.drawable.chairgreen);
            chair12.setEnabled(false);
        }else if (number==13){
            chair13.setBackgroundResource(R.drawable.chairgreen);
            chair13.setEnabled(false);
        }else if (number==14){
            chair14.setBackgroundResource(R.drawable.chairgreen);
            chair14.setEnabled(false);
        }else if (number==15){
            chair15.setBackgroundResource(R.drawable.chairgreen);
            chair15.setEnabled(false);
        }
    }


    public void order(final String pos){
        posisi=pos;
        final AlertDialog.Builder ad=new AlertDialog.Builder(BookingActivity.this);
        ad.setTitle("Konfirmasi");
        ad.setMessage("Apakah benar ingin Booking ditabel "+pos+" ?");

        ad.setPositiveButton("YA",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (btnDate.getText().toString().equals("Set Date")){
//                    Toast.makeText(BookingActivity.this,"You Must Set Date First",Toast.LENGTH_SHORT).show();
//                }else if (btnTime.getText().toString().equals("Set Time")){
//                    Toast.makeText(BookingActivity.this,"You Must Set Time First",Toast.LENGTH_SHORT).show();
//
//                }else{
                    //new save().execute();
                    String jam = "empty";
                    String tanggal = "empty";
                    dbHelper.addBookTable(pos,tanggal, jam,200000);
                    Log.d("table list : ",listTable.toString());
                    Intent i = getIntent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             //   }

            }});

        ad.setNegativeButton("TIDAK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {

            }});

        ad.show();
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent i = new Intent(BookingActivity.this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    class save extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingActivity.this);
            pDialog.setMessage("Menyimpan data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }
        @SuppressLint("WrongThread")
        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList  <NameValuePair>();
            params.add(new BasicNameValuePair("id_user", idUser));
            params.add(new BasicNameValuePair("no_table", dataTable));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("phone", noTlp));
            params.add(new BasicNameValuePair("date", tvSetdate.getText().toString()));
            params.add(new BasicNameValuePair("time", tvSettime.getText().toString()));

            String url=IP+"add-reservation.php";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
