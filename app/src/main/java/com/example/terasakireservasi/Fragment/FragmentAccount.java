package com.example.terasakireservasi.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SharedMemory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.R;
import com.example.terasakireservasi.Tools.SharedPrefManager;


public class FragmentAccount extends Fragment {

    SharedPrefManager sharedPrefManager;
    String ip;
    JSONParser jsonParser = new JSONParser();
    String idUser, username, password, email, noTlp, alamat, nama;

    TextView tvNamaUser, tvEmailUser, tvNotlp, tvAlamat;


    public FragmentAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_account, container, false);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(getActivity());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean RegisteredAdmin = sharedPref.getBoolean("admin", false);
        Boolean RegisteredUser = sharedPref.getBoolean("user", false);
        if (!RegisteredAdmin && !RegisteredUser){
            getActivity().finish();
        }else {
            idUser = sharedPref.getString("id_user", "");
            nama = sharedPref.getString("nama", "");
            username = sharedPref.getString("username", "");
            password = sharedPref.getString("password", "");
            email = sharedPref.getString("email", "");
            noTlp = sharedPref.getString("no_tlp", "");
            alamat = sharedPref.getString("alamat", "");

        }

        tvNamaUser = (TextView)view.findViewById(R.id.tv_name);
        tvEmailUser = (TextView)view.findViewById(R.id.account_email);
        tvNotlp = (TextView)view.findViewById(R.id.account_notlp);
        tvAlamat = (TextView)view.findViewById(R.id.account_alamat);
        tvNamaUser.setText(nama);
        tvEmailUser.setText(email);
        tvNotlp.setText(noTlp);
        tvAlamat.setText(alamat);



        return view;
    }

}
