package com.example.terasakireservasi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.Tools.SharedPrefManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";
    JSONParser jsonParser = new JSONParser();
    String ip;
    TextView linkSignup;
    Button btnLogin;

    int sukses;
    EditText edtUsernmae, edtPassword;
    String strGetUsername, strGetPassword;
    SharedPrefManager sharedPrefManager;
    String dbIdUser, dbUsername, dbPassword, dbNama, dbEmail, dbNotlp, dbAlamat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ip=jsonParser.getIP();
        sharedPrefManager = new SharedPrefManager(this);
//        if (sharedPrefManager.getSPSudahLoginUser()){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class)
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//            finish();
//        }
        edtUsernmae = (EditText)findViewById(R.id.input_username);
        edtPassword = (EditText)findViewById(R.id.input_password);
        linkSignup = (TextView)findViewById(R.id.link_signup);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromEditext();
                if (strGetUsername.length()<1||strGetPassword.length()<1){
                    emptytext();
                }else{
                    new proses().execute();
                }


            }
        });

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(i);
            }
        });
    }

    void getDataFromEditext(){
        strGetUsername = edtUsernmae.getText().toString();
        strGetPassword = edtPassword.getText().toString();
    }

    class proses extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... params) {
            try {

                List<NameValuePair> myparams = new ArrayList<NameValuePair>();
                myparams.add(new BasicNameValuePair("username", strGetUsername));
                myparams.add(new BasicNameValuePair("password", strGetPassword));
                String url=ip+"login-api.php";
                Log.v("detail",url);
                JSONObject json = jsonParser.httpRequest(url, "GET", myparams);
                //Log.d("detail", json.toString());
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    JSONArray myObj = json.getJSONArray(TAG_record); // JSON Array
                    final JSONObject myJSON = myObj.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(LoginActivity.this,"Login Berhasil",Toast.LENGTH_SHORT ).show();
                                dbIdUser = myJSON.getString("id_user");
                                dbUsername = myJSON.getString("username");
                                dbPassword = myJSON.getString("password");
                                dbNama = myJSON.getString("nama");
                                dbEmail = myJSON.getString("email");
                                dbNotlp = myJSON.getString("no_tlp");
                                dbAlamat = myJSON.getString("alamat");
                            }
                            catch (JSONException e) {e.printStackTrace();}
                        }});
                }else if (sukses==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gagal();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gagal();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @SuppressLint("NewApi")
        protected void onPostExecute(String file_url) {
           // Log.v("SUKSES",dbIdUser);
            if(sukses==1){
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                sharedPrefManager.saveSPBooleanUser(SharedPrefManager.SUDAH_LOGIN_USER, true);
                editor.putBoolean("user", true);
                editor.putString("id_user", dbIdUser);
                editor.putString("username", dbUsername);
                editor.putString("password", dbPassword);
                editor.putString("nama", dbNama);
                editor.putString("email", dbEmail);
                editor.putString("no_tlp", dbNotlp);
                editor.putString("alamat", dbAlamat);
                editor.apply();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }

        }
    }


    public void gagal(){
        new AlertDialog.Builder(this)
                .setTitle("Gagal Login")
                .setMessage("Username atau Password Tidak Cocok")
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }})
                .show();
    }


    public void emptytext(){
        new AlertDialog.Builder(this)
                .setTitle("Maaf...")
                .setMessage("Username atau Password Tidak Boleh Kosong!!!")
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }})
                .show();
    }


}
