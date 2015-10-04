package prgc.snct.sos.Activities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import prgc.snct.sos.R;

public class TransceiverMain extends ActionBarActivity implements LocationListener, GpsStatus.Listener, View.OnClickListener {

    private static final double a = 6378137.0;
    private static final double mf = 298.2572221;
    private static final double f = 0.003352811;
    private static final double ee = 0.006694380;
    private static final double PI = 3.1415926536;

    static boolean get = false;

    private static final String url = "jdbc:mysql://160.16.91.195:3306/sos_db";
    private static final String user = "snctprocon2015";
    private static final String pass = "kadai";
    String result = "";
    String Id;
    double lat, lng, Lat1r, Lng1r, Latr, Lngr, Latrad, Lngrad;
    int scount = 0;
    private TextView textView1;

    private LocationManager locationManager;
    private Map<String, LinearLayout> layoutMap = new HashMap<String, LinearLayout>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transceiver_main);

        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        textView1 = (TextView)findViewById(R.id.textView);

        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationManager.addGpsStatusListener(this);

        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 3000, 10, this);
        }

        Intent intent = getIntent();

        if(intent != null){
             boolean autoClick = intent.getBooleanExtra("AutoClick", false);

            if(autoClick){
                button5.performClick();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        locationManager.removeGpsStatusListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transceiver_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        final Handler handler = new Handler();

        if(get == true) {
            if (v.getId() == R.id.button5) { //データを送信

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            String Message = "NULL";
                            String Status = "NULL";

                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            Id = wifiInfo.getMacAddress();

                            Date d = new Date();
                            String date = d.toString();

                            Class.forName("com.mysql.jdbc.Driver"); // JDBCドライバをロード

                            Connection con = (Connection) DriverManager.getConnection(url, user, pass); // サーバに接続
                            Statement st = (Statement) con.createStatement();

                            String SQL = "INSERT INTO `sos_stat` VALUES ('" + Id + "', '" + Message + "', '" + Status + "', " + lat + ", " + lng + ", cast(now() as datetime))";

                            st.executeUpdate(SQL);

                            st.close();
                            con.close();

                            //result = "lat = " + String.valueOf(lat) + ", lng = " + String.valueOf(lng);

                        } catch (Exception e) {
                            e.printStackTrace();
                            result = e.toString();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText("Send SOS.");
                            }
                        });
                    }
                }).start();

            }else if(v.getId() == R.id.button6){ //データを受信

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Class.forName("com.mysql.jdbc.Driver"); // JDBCドライバをロード

                            Connection con = (Connection) DriverManager.getConnection(url, user, pass); // サーバに接続
                            Statement st = (Statement) con.createStatement();

                            String SQL = "SELECT * from sos_stat";
                            ResultSet rs = st.executeQuery(SQL);

                            while(rs.next()){
                                double dlat = rs.getDouble("lat");
                                double dlng = rs.getDouble("lng");
                                if(((dlat >= lat - Latr) && (dlat <= lat + Latr)) && ((dlng >= lng - Lngr) && (dlng <= lng + Lngr))){
                                    scount++;
                                }
                            }

                            rs.close();
                            st.close();
                            con.close();

                            if(scount > 0){
                                result = "Find SOS = " + String.valueOf(scount);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            result = e.toString();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText(result);
                            }
                        });
                    }
                }).start();

            }
        }else{
            textView1.setText("Haven't got Location yet.");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        get = true;
        Latrad = (lat/180)*PI;
        Lat1r = ((PI/648000)*a*(1-ee)/(Math.pow((1-ee*(Math.pow((Math.sin(Latrad)), 2))), 1.5))) * 3.6;
        Lng1r = ((PI/648000)*a*(Math.cos(Latrad))/(Math.sqrt(1-ee*(Math.pow(Math.sin(Latrad), 2))))) * 3.6;
        Latr = 2/Lat1r;
        Lngr = 2/Lng1r;
        textView1.setText("Got Location.");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void setUpMap() {

        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        // Accuracy Settings(Low Level)
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // PowerRequirement Setting (Low Power)
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Location Provider Getting
        String provider = mLocationManager.getBestProvider(criteria, true);

        mLocationManager.requestLocationUpdates(provider, 0, 0, this);

    }

    @Override
    public void onGpsStatusChanged(int event) {
    }

}
