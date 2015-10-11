package prgc.snct.sos.Activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import prgc.snct.sos.R;

public class MapActivity2 extends FragmentActivity implements LocationListener, GpsStatus.Listener, View.OnClickListener {

    GoogleMap gMap;
    private static final int MENU_A = 0;
    private static final int MENU_B = 1;
    private static final int MENU_c = 2;

    public static String posinfo = "";
    public static String info_A = "";
    public static String info_B = "";
    ArrayList<LatLng> markerPoints;

    public static MarkerOptions options;

    public ProgressDialog progressDialog;

    public String travelMode = "driving";//default

    private LocationManager locationManager;

    LatLng curr = new LatLng(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.addGpsStatusListener(this);

        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            locationManager.requestLocationUpdates(provider, 3000, 10, this);
        }

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        //プログレス
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("検索中です......");
        progressDialog.hide();


        //初期化
        markerPoints = new ArrayList<LatLng>();


        SupportMapFragment mapfragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);


        gMap = mapfragment.getMap();



        if(gMap!=null){

            gMap.setMyLocationEnabled(true);

            //クリックリスナー
            gMap.setOnMapClickListener(new OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {

                    //３度目クリックでスタート地点を再設定
                    if(markerPoints.size()>1){
                        markerPoints.clear();
                        gMap.clear();
                    }


                    markerPoints.add(point);


                    options = new MarkerOptions();
                    options.position(point);


                    if(markerPoints.size()==1){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    //    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                        options.title("A");


                    }else if(markerPoints.size()==2){
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                      //  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                        options.title("B");


                    }


                    gMap.addMarker(options);


                    gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            // TODO Auto-generated method stub


                            String title = marker.getTitle();
                            if (title.equals("A")){
                                marker.setSnippet(info_A);

                            }else if (title.equals("B")){
                                marker.setSnippet(info_B);
                            }


                            return false;
                        }
                    });



                    if(markerPoints.size() >= 2){
                      //ルート検索
                        routeSearch();
                    }
                }
            });
        }
    }


    private void routeSearch(){
        progressDialog.show();

        LatLng origin = markerPoints.get(0);
        LatLng dest = markerPoints.get(1);


        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();


        downloadTask.execute(url);

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){


        String str_origin = "origin="+origin.latitude+","+origin.longitude;


        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        String sensor = "sensor=false";

        //パラメータ
        String parameters = str_origin+"&"+str_dest+"&"+sensor + "&language=ja" + "&mode=" + travelMode;

        //JSON指定
        String output = "json";


        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.connect();


            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("MapActivity2", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));

        Log.v("MapActivity2", "onLocationChanged");

        Intent intent = getIntent();
        if(intent != null){

            Log.v("MapActivity2", "intent != null");

            markerPoints.clear();
            gMap.clear();

            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng sosLatLng = new LatLng(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0));

            Log.v("MapActivity2", "Lat = " + new Double(sosLatLng.latitude).toString());
            Log.v("MapActivity2", "Lng = " + new Double(sosLatLng.longitude).toString());

            if (markerPoints.size()>1){
                markerPoints.clear();
                gMap.clear();
            }

            markerPoints.add(here);


            options = new MarkerOptions();
            options.position(here);


            if(markerPoints.size()==1){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                //    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                options.title("A");


            }else if(markerPoints.size()==2){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                options.title("B");


            }


            gMap.addMarker(options);


            gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // TODO Auto-generated method stub


                    String title = marker.getTitle();
                    if (title.equals("A")) {
                        marker.setSnippet(info_A);

                    }else if (title.equals("B")){
                        marker.setSnippet(info_B);
                    }


                    return false;
                }
            });

            if (markerPoints.size()>1){
                markerPoints.clear();
                gMap.clear();
            }

            markerPoints.add(sosLatLng);


            options = new MarkerOptions();
            options.position(sosLatLng);


            if(markerPoints.size()==1){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                //    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                options.title("A");


            }else if(markerPoints.size()==2){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                options.title("B");


            }


            gMap.addMarker(options);


            gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // TODO Auto-generated method stub


                    String title = marker.getTitle();
                    if (title.equals("A")) {
                        marker.setSnippet(info_A);

                    }else if (title.equals("B")){
                        marker.setSnippet(info_B);
                    }


                    return false;
                }
            });


            if(markerPoints.size() >= 2){
                //ルート検索
                routeSearch();
            }

            locationManager.removeUpdates(this);
            locationManager.removeGpsStatusListener(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.v("Status", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("Status", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("Status", "TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    public void onClick(View v) {
        if(v.getId() == R.id.button) {

            double a=curr.latitude;
            double b=curr.longitude;
            Uri uri = Uri.parse("https://www.google.co.jp/maps/search/%E7%97%85%E9%99%A2/@"+a+","+b+"z/data=!3m1!4b1");
            Intent i = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(i);

        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String>{
        //非同期で取得

        @Override
        protected String doInBackground(String... url) {


            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }


        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);
        }
    }

    /*parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{


        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                parseJsonpOfDirectionAPI parser = new parseJsonpOfDirectionAPI();


                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        //ルート検索で得た座標を使って経路表示
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            if(result.size() != 0){

                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();


                    List<HashMap<String, String>> path = result.get(i);


                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    //ポリライン
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(0x550000ff);

                }

                //描画
                gMap.addPolyline(lineOptions);
            }else{
                gMap.clear();
                Toast.makeText(MapActivity2.this, "ルート情報を取得できませんでした", Toast.LENGTH_LONG).show();
            }
            progressDialog.hide();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, MENU_A,   0, "Info");
        menu.add(0, MENU_B,   0, "Legal Notices");
        menu.add(0, MENU_c,   0, "Mode");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() )
        {
            case MENU_A:
                //show_mapInfo();
                return true;

            case MENU_B:
                //Legal Notices(免責事項)

                String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
                AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MapActivity2.this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();

                return true;

            case MENU_c:
                //show_settings();
                return true;

        }
        return false;
    }

    //リ･ルート検索
    private void re_routeSearch(){
        progressDialog.show();

        LatLng origin = markerPoints.get(0);
        LatLng dest = markerPoints.get(1);

        //
        gMap.clear();

        //マーカー
        //A
        options = new MarkerOptions();
        options.position(origin);
       // options.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options.title("A");
        options.draggable(true);
        gMap.addMarker(options);
        //B
        options = new MarkerOptions();
        options.position(dest);
      //  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.red));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        options.title("B");
        options.draggable(true);
        gMap.addMarker(options);


        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();


        downloadTask.execute(url);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        locationManager.removeGpsStatusListener(this);
    }

}
