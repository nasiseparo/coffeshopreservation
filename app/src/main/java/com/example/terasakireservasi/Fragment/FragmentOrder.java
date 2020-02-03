package com.example.terasakireservasi.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.terasakireservasi.Activity.BookingActivity;
import com.example.terasakireservasi.Fragment.InOrderFragment.FragmentRiwayatBooking;
import com.example.terasakireservasi.Fragment.InOrderFragment.FragmentRiwayatMenu;
import com.example.terasakireservasi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrder extends Fragment {

    FloatingActionButton btnBooking;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;


    public FragmentOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarOrder);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.textToolbarOrder);
        textView.setText("Order");
        btnBooking = (FloatingActionButton)view.findViewById(R.id.booking);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BookingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        return view;
    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm){super(fm);}

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0: return new FragmentRiwayatMenu();
                case 1: return new FragmentRiwayatBooking();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return getResources().getString(R.string.information_tab1);
                case 1:
                    return getResources().getString(R.string.information_tab2);
            }
            return null;
        }
    }

}
