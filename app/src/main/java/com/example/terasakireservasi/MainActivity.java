package com.example.terasakireservasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.terasakireservasi.Activity.BookingActivity;
import com.example.terasakireservasi.Activity.ListMenuActivity;
import com.example.terasakireservasi.Fragment.FragmentAccount;
import com.example.terasakireservasi.Fragment.FragmentHome;
import com.example.terasakireservasi.Fragment.FragmentInbox;
import com.example.terasakireservasi.Fragment.FragmentOrder;
import com.example.terasakireservasi.SQLite.StoreDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new FragmentHome();
    final Fragment fragment2 = new FragmentOrder();
    final Fragment fragment3 = new FragmentInbox();
    final Fragment fragment4 = new FragmentAccount();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    public static final String TAG_1 = "1";
    public static final String TAG_2 = "2";
    public static final String TAG_3 = "3";
    public static final String TAG_4 = "4";


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.id_home_item:
                            fm.beginTransaction().hide(active).show(fragment1).addToBackStack(TAG_1).commit();
                            active = fragment1;
                            return true;
                        case R.id.id_order_item:
                            fm.beginTransaction().hide(active).show(fragment2).addToBackStack(TAG_2).commit();
                            active = fragment2;
                            return true;
                        case R.id.id_inbox_item:
                            fm.beginTransaction().hide(active).show(fragment3).addToBackStack(TAG_3).commit();
                            active = fragment3;
                            return true;
                        case R.id.id_account_item:
                            fm.beginTransaction().hide(active).show(fragment4).addToBackStack(TAG_4).commit();
                            active = fragment4;
                            return true;
                    }
                    if (active != null){
                          fm.beginTransaction().replace(R.id.main_container, active);
                          fm.beginTransaction().addToBackStack(null);
                          fm.beginTransaction().commit();
                    }
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


//        if (active != null){
//            fm.beginTransaction().replace(R.id.main_container, fragment1);
//            fm.beginTransaction().addToBackStack(null);
//            fm.beginTransaction().commit();
//        }
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).addToBackStack(TAG_4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).addToBackStack(TAG_3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).addToBackStack(TAG_2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").addToBackStack(TAG_1).commit();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("Message");
            ad.setMessage("Are you sure want to exit?");

            ad.setPositiveButton("YA",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }});

            ad.setNegativeButton("TIDAK",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {

                }});

            ad.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
