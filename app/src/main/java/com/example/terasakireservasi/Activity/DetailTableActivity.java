package com.example.terasakireservasi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.terasakireservasi.Fragment.FragmentOrder;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.MainActivity;
import com.example.terasakireservasi.R;

public class DetailTableActivity extends AppCompatActivity {

    String ip;
    JSONParser jsonParser = new JSONParser();
    String getIdBooking, getTableNumber, getDate;
    TextView tvIdbooking, tvTableNumber, tvDate;
    ImageView btnBack;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_table);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        ip=jsonParser.getIP();

        tvIdbooking = findViewById(R.id.tv_detailidbooking);
        tvTableNumber = findViewById(R.id.tv_notable);
        tvDate = findViewById(R.id.tv_detaildate);
        btnBack = findViewById(R.id.btn_back);

        Intent i = getIntent();
        getIdBooking = i.getStringExtra("idbooking");
        getTableNumber = i.getStringExtra("tablenumber");
        getDate = i.getStringExtra("date");


        tvIdbooking.setText("ID Booking : "+getIdBooking);
        tvTableNumber.setText("Table Number : "+getTableNumber);
        tvDate.setText("Date : "+getDate);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DetailTableActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
