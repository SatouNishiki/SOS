package prgc.snct.sos.Activities;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 航輔 on 2015/10/03.
 */
public class GetSOS implements LocationListener, GpsStatus.Listener{
    private static final String url = "jdbc:mysql://160.16.91.195:3306/sos_db";
    private static final String user = "snctprocon2015";
    private static final String pass = "kadai";
    double lat, lng, Lat1r, Lng1r, Latr, Lngr, Latrad, Lngrad;
    public int scount = 0;
    int ret=0;
    private static final double a = 6378137.0;
    private static final double mf = 298.2572221;
    private static final double f = 0.003352811;
    private static final double ee = 0.006694380;
    private static final double PI = 3.1415926536;
    static boolean get = false;
    private LocationManager locationManager;
    public GetSOS(Context con) {

        locationManager = (LocationManager)con.getSystemService(Context.LOCATION_SERVICE);


        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(crit, true);

        Location loc = locationManager.getLastKnownLocation(provider);


        lat = loc.getLatitude();
        lng = loc.getLongitude();
        while(lat==0.0)
        {

        }
        get = true;
        Latrad = (lat/180)*PI;
        Lat1r = ((PI/648000)*a*(1-ee)/(Math.pow((1-ee*(Math.pow((Math.sin(Latrad)), 2))), 1.5))) * 3.6;
        Lng1r = ((PI/648000)*a*(Math.cos(Latrad))/(Math.sqrt(1 - ee * (Math.pow(Math.sin(Latrad), 2))))) * 3.6;
        Latr = 2/Lat1r;
        Lngr = 2/Lng1r;
    }
    public int geter(final double lat, final double lng, final double Latr, final double Lngr){

scount=0;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Class.forName("com.mysql.jdbc.Driver"); // JDBCドライバをロード

                    Connection con = (Connection) DriverManager.getConnection(url, user, pass); // サーバに接続
                    Statement st = (Statement) con.createStatement();

                    String SQL = "SELECT * from sos_stat";
                    ResultSet rs = st.executeQuery(SQL);

                    while (rs.next()) {
                        double dlat = rs.getDouble("lat");
                        double dlng = rs.getDouble("lng");
                        if (((dlat >= lat - Latr) && (dlat <= lat + Latr)) && ((dlng >= lng - Lngr) && (dlng <= lng + Lngr))) {
                            scount++;
                        }
                    }

                    rs.close();
                    st.close();
                    con.close();
                    if(scount > 0){
                        ret=scount;
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        }).start();
while(scount==0)
{
    ret=scount;
}


        return ret;
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


    @Override
    public void onGpsStatusChanged(int event) {
    }
}
