package com.example.terasakireservasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.terasakireservasi.Koneksi.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    String ip;

    EditText userName, passWord, nama, email, phone, address;
    String strUsername, strPassword, strNama, strEmail, strPhone, strAddress;
    AppCompatButton btnSignup;
    private static final String TAG_SUKSES = "sukses";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ip=jsonParser.getIP();

        userName = (EditText)findViewById(R.id.username);
        passWord = (EditText)findViewById(R.id.password);
        nama = (EditText)findViewById(R.id.nama);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        address = (EditText)findViewById(R.id.address);
        btnSignup = (AppCompatButton)findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromEditText();
                if (strUsername.length()<1||strPassword.length()<1||
                        strNama.length()<1||strEmail.length()<1||
                        strPhone.length()<1 ||strAddress.length()<1){
                    //do something
                }else{
                    new Register().execute();
                }
            }
        });

    }

    void getDataFromEditText(){
        strUsername  = userName.getText().toString().trim();
        strPassword = passWord.getText().toString().trim();
        strNama = nama.getText().toString().trim();
        strEmail = email.getText().toString().trim();
        strPhone = phone.getText().toString().trim();
        strAddress = address.getText().toString().trim();
    }

    class Register extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", strUsername));
            params.add(new BasicNameValuePair("password", strPassword));
            params.add(new BasicNameValuePair("nama", strNama));
            params.add(new BasicNameValuePair("email", strEmail ));
            params.add(new BasicNameValuePair("no_tlp", strPhone ));
            params.add(new BasicNameValuePair("alamat", strAddress ));
            String url=ip+"register-api.php";
            Log.v("add",url);
            JSONObject json = jsonParser.httpRequest(url,"POST", params);
            Log.d("add", json.toString());

            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    Intent i = getIntent();
                    setResult(100, i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                        }
                    });
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();
                        }
                    });
                    // gagal update data
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }
        protected void onPostExecute(String file_url) {


        }
    }
}
