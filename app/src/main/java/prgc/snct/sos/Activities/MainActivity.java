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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import prgc.snct.sos.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    boolean first;
    String Id, Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImageButton ibutton = (ImageButton)findViewById(R.id.imageButton2);
       // ImageButton ibutton2 = (ImageButton)findViewById(R.id.imageButton3);
        //ImageButton ibutton3 = (ImageButton)findViewById(R.id.imageButton4);

        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);

        //Button button6 = (Button)findViewById(R.id.button6);　chart(Mounting ly.)

        //Button button7 = (Button)findViewById(R.id.button7);

        //ibutton.setOnClickListener(this);
        //ibutton2.setOnClickListener(this);
        //ibutton3.setOnClickListener(this);

        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        //button6.setOnClickListener(this);
       // button7.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        /*if(v.getId() == R.id.imageButton2){

            //     Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            Intent intent = new Intent(MainActivity.this, MapActivity2.class);
            startActivity(intent);

        }else if(v.getId() == R.id.imageButton3){

            Intent intent = new Intent(MainActivity.this,BluetoothMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.BluetoothMain");
            startActivity(intent);

        }else if(v.getId() == R.id.imageButton4) {

            Intent intent = new Intent(MainActivity.this,DatabaseMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);}
*/
         if(v.getId() == R.id.button4) {

             new AlertDialog.Builder(MainActivity.this)
//アイコンを指定(省略可、省略の場合はコメントアウトしてください)
                     .setIcon(android.R.drawable.ic_dialog_alert)
//タイトルメッセージ
                     .setTitle(R.string.alert_dialog_two_buttons_title)
                     .setMessage(R.string.message)
//ポジティブボタン（今回はOKボタン）を押下した時のイベント処理
                     .setPositiveButton("OK",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int whichButton) {
                                     Intent intent = new Intent(MainActivity.this, TransceiverMain.class);
                                     startActivity(intent);
                                 }
                             })
//ネガティブボタン（今回はCancelボタン）を押下した時のイベント処理
                     .setNegativeButton("Cancel",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int whichButton) {

                                 }
                             })
                     .show();


        }
        else if(v.getId() == R.id.button5) {

            Intent intent = new Intent(MainActivity.this,ActivityService.class);
            startActivity(intent);

        }
        /*else if(v.getId() == R.id.button6) {

            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.button7) {

            Intent intent = new Intent(MainActivity.this,ListMain.class);
            startActivity(intent);

        }*/
    }
}