package prgc.snct.sos.Activities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import prgc.snct.sos.R;
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

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import android.app.ProgressDialog;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity implements LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public double xpojia=0.0;//���p�n�_�p�ܓx
    public double xpojib=0.0;//���p�n�_�p�o�x

    public static String posinfo = "";
    public static String info_A = "";
    public static String info_B = "";
    LatLng old=new LatLng(0,0);//�^�b�v�ʒu�L���p
    ArrayList<LatLng> markerPoints;

    public static MarkerOptions options;

    public ProgressDialog progressDialog;

    public String travelMode = "driving";//default
    LatLng curr = new LatLng(0,0);//�~�����s�҂̈ʒu���
    //LatLng bhelp = new LatLng(bhelpx,bhelpy);//�v�~���҂̈ʒu���

    int startup=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.hide();


        //������
        markerPoints = new ArrayList<LatLng>();
        if(mMap!=null){

            mMap.setMyLocationEnabled(true);

            //�N���b�N���X�i�[
            mMap.setOnMapClickListener(new OnMapClickListener() {
                @Override
                //��ʃN���b�N�Ń��[�g����(���̓^�b�v�����ʒu�j
                public void onMapClick(LatLng point) {
                    //�O��ڂ̃^�b�v�Ń��Z�b�g
                    if(markerPoints.size()>1){
                        markerPoints.clear();
                        mMap.clear();
                    }
                    //���ڂ̃^�b�v���ɋ����[�g�폜
                    if(markerPoints.size()==1)
                    {
                        mMap.clear();
                        options = new MarkerOptions();
                        options.position(old);
                        options.title("A");
                         mMap.addMarker(options);
                    }

                    markerPoints.add(point);


                    options = new MarkerOptions();
                    options.position(point);//�^�b�v�����ʒu�Ƀs���𗧂Ă�
/*����^�b�v�ŗv�~���ҁA���ڂŒʍs�s�ʒu�ݒ�̏ꍇ
if(markerPoints.size()==0)
options.position(bhelp);
else
options.position(point);
*/


                    if(markerPoints.size()==1){
                        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        options.title("A");

                    }
                    else if(markerPoints.size()==2) {
                        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        options.title("B");
                    }

                    mMap.addMarker(options);
                    old=point;

                    mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            // TODO Auto-generated method stub


                            String title = marker.getTitle();
                            if (title.equals("A")){
                                marker.setSnippet(info_A);

                            }
                            else if (title.equals("B")){
                                marker.setSnippet(info_B);
                            }


                            return false;
                        }
                    });



                    if(markerPoints.size() >= 1){
                        //���[�g����
                        routeSearch();
                    }
                }
            });
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
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
    public void onLocationChanged(Location location) {
        curr = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(curr).title("Marker"));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));
        //����ǂݍ��ݎ��̂ݕ\���ʒu���ړ�
        if(startup==0)
        {
            startup=1;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));
        }

    }
    private void routeSearch(){
        progressDialog.show();

        LatLng origin = curr;
        LatLng dest = markerPoints.get(0);//�v�~���҂̈ʒu���ǂݍ���
        LatLng xp=new LatLng(0,0);
        //���ڂ̃^�b�v���ɒʍs�s�n�_���l��
        if(markerPoints.size()==2) {
            xp=markerPoints.get(1);
            //point�����炷���p�����߂�֐�
            getstopper(origin, dest,xp);
        }
        //�ʂ�|�C���g
        LatLng xpoints=new LatLng(xpojia,xpojib);
        String url = getDirectionsUrl(origin, dest,xpoints);

        DownloadTask downloadTask = new DownloadTask();


        downloadTask.execute(url);

    }
    //point�����炷���p�����߂�
    private void getstopper(LatLng origin,LatLng dest,LatLng xp)
    {
        if(origin.latitude-dest.latitude>0)//n->s
        {
            if(origin.longitude-dest.longitude>0)//e->w
            {
                //origin��dest�����񂾐����k,������xp
                if((xp.latitude-dest.latitude+origin.longitude-xp.longitude)>(origin.latitude-dest.latitude+origin.longitude-dest.longitude)/2.0)
                {
                    //xpojia=dest.latitude;
                    xpojia=xp.latitude-0.001;
                    //xpojib=origin.longitude;
                    xpojib=xp.longitude+0.001;
                }
                //origin��dest�����񂾐�����,������xp
                else
                {
                    //xpojix=origin.latitude;
                    xpojia=xp.latitude+0.001;
                    //xpojiy=dest.longitude;
                    xpojib=xp.longitude-0.001;
                }
            }
            else//w->e
            {
                //origin��dest�����񂾐����k,������xp
                if((xp.latitude-dest.latitude-origin.longitude+xp.longitude)>(origin.latitude-dest.latitude-origin.longitude+dest.longitude)/2.0)
                {
                    //xpojix=dest.latitude;
                    xpojia=xp.latitude-0.001;
                    //xpojiy=origin.longitude;
                    xpojib=xp.longitude-0.001;
                }
                //origin��dest�����񂾐�����,������xp
                else
                {
                    //xpojix=origin.latitude;
                    xpojia=xp.latitude+0.001;
                    //xpojiy=dest.longitude;
                    xpojib=xp.longitude+0.001;
                }
            }
        }
        else//s->n
        {
            if(origin.longitude-dest.longitude>0)//e->w
            {
                //origin��dest�����񂾐����k,������xp
                if((xp.latitude-origin.latitude-dest.longitude+xp.longitude)>(-origin.latitude+dest.latitude+origin.longitude-dest.longitude)/2.0)
                {
                    //xpojix=origin.latitude;
                    xpojia=xp.latitude-0.001;
                    //xpojiy=dest.longitude;
                    xpojib=xp.longitude-0.001;
                }
                //origin��dest�����񂾐�����,������xp
                else
                {
                    //xpojix=dest.latitude;
                    xpojia=xp.latitude+0.001;
                    //xpojiy=origin.longitude;
                    xpojib=xp.longitude+0.001;
                }
            }
            else//w->e
            {
                //origin��dest�����񂾐����k,������xp
                if((xp.latitude-origin.latitude+dest.longitude-xp.longitude)>(-origin.latitude+dest.latitude-origin.longitude+dest.longitude)/2.0)
                {
                    //xpojix=origin.latitude;
                    xpojia=xp.latitude-0.001;
                    //xpojiy=dest.longitude;
                    xpojib=xp.longitude+0.001;
                }
                //origin��dest�����񂾐�����,������xp
                else
                {
                    //xpojix=dest.latitude;
                    xpojia=xp.latitude+0.001;
                    //xpojiy=origin.longitude;
                    xpojib=xp.longitude-0.001;
                }
            }
        }
    }
    private String getDirectionsUrl(LatLng origin,LatLng dest,LatLng xpoints){


        String str_origin = "origin="+origin.latitude+","+origin.longitude;


        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String wayp="waypoints="+xpoints.latitude+","+xpoints.longitude;
        String sensor = "sensor=false";
        String parameters="";
        if(markerPoints.size()<2)
        {
            parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=ja" + "&mode=" + travelMode;
        }
        else {
            //�p�����[�^
            parameters = str_origin + "&" + str_dest + "&" + wayp + "&" + sensor + "&language=ja" + "&mode=" + travelMode;
        }
        //JSON�w��
        String output = "json";


        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
//https://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA
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
            Log.d("Exception while downloading url",e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class DownloadTask extends AsyncTask<String, Void, String>{
        //�񓯊��Ŏ擾

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

        //���[�g�����œ������W���g���Čo�H�\��
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

                    //�|�����C��
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(0x550000ff);

                }

                //�`��
                mMap.addPolyline(lineOptions);
            }else{
                mMap.clear();
                Toast.makeText(MapsActivity.this, "Can't acquire route", Toast.LENGTH_LONG).show();
            }
            progressDialog.hide();

        }
    }








    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
