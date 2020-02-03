package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.R;

public class DetailTableActivity extends AppCompatActivity {

    String ip;
    JSONParser jsonParser = new JSONParser();
    String getIdBooking, getTableNumber, getDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_table);
        ip=jsonParser.getIP();
        Intent i = getIntent();
        getIdBooking = i.getStringExtra("idbooking");
        getTableNumber = i.getStringExtra("tablenumber");
        getDate = i.getStringExtra("date");

    }
}
