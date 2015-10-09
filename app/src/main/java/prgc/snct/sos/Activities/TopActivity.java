package prgc.snct.sos.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import prgc.snct.sos.R;

public class TopActivity extends ActionBarActivity implements View.OnClickListener{

    private GoogleApiClient client;
    boolean first;
    String Name, Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        ImageButton button = (ImageButton)findViewById(R.id.imageButton);

        button.setOnClickListener(this);

        this.client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        this.client.connect();

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Id = wifiInfo.getMacAddress();

        loadData();

        if(first) {
            initList();
            showDialog(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton) {

            Intent intent = new Intent(TopActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 1:

                // カスタムビューを設定
                LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.first_dialog, (ViewGroup)findViewById(R.id.layout_root));

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("初期設定");
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        // NAME を取得
                        EditText name = (EditText) layout.findViewById(R.id.first_dialog_name);
                        Name = name.getText().toString();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                    Id = wifiInfo.getMacAddress();

                                    Class.forName("com.mysql.jdbc.Driver"); // JDBCドライバをロード

                                    final String url = "jdbc:mysql://160.16.91.195:3306/sos_db";
                                    final String user = "snctprocon2015";
                                    final String pass = "kadai";

                                    Connection con = (Connection) DriverManager.getConnection(url, user, pass); // サーバに接続
                                    Statement st = (Statement) con.createStatement();

                                    String SQL = "INSERT INTO `sos_nlist` VALUES ('" + Id + "', '" + Name + "')";
                                    st.executeUpdate(SQL);

                                    st.close();
                                    con.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        first = false;
                        saveData();
                    }
                });

                // 表示
                builder.create().show();
        }

        return null;
    }

    public void initList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        //  空のリストを保存しておく
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        String jsonNamesString = gson.toJson(names);
        String jsonIdString = gson.toJson(ids);
        prefs.edit().putString("ARR_NAMES",jsonNamesString).apply();
        prefs.edit().putString("ARR_ID",jsonIdString).apply();
    }

    public void saveData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // putBooleanで保存
        prefs.edit().putBoolean("WhichFirst",first).apply();
    }

    public void loadData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // getBooleanで取得
        first = prefs.getBoolean("WhichFirs", true);
    }

}
