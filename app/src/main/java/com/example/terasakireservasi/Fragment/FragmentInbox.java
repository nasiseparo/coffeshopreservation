package com.example.terasakireservasi.Fragment;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.terasakireservasi.R;


public class FragmentInbox extends Fragment {


    public FragmentInbox() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_inbox, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarInbox);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.textToolbarInbox);
        textView.setText("Inbox");
        return view;
    }

}
