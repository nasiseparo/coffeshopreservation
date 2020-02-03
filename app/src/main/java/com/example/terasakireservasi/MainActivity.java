package com.example.terasakireservasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

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

    StoreDatabase dbHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.id_home_item:
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;
                        case R.id.id_order_item:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;
                        case R.id.id_inbox_item:
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;
                        case R.id.id_account_item:
                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;
                    }
                    return false;
                }
            };

    String rsCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    /*    Intent i = getIntent();
        rsCode = i.getStringExtra("key");
        if (rsCode.equals("2")) {
            fm.beginTransaction().replace(R.id.main_container, fragment2).
                    addToBackStack(null).commit();

        }else{

        }
*/
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

//        if (active != null){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.main_container, fragment1);
//            ft.addToBackStack(null);
//            ft.commit();
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==2){

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
