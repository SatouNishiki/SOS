package prgc.snct.sos.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import prgc.snct.sos.R;

//
//
public class TryService extends Service {

    private final static String TAG = "TryService#";
    int oldscount=0,oldlcount=0;
    int flag=0;
    Context con =this;
    int scount,lcount;
    double lat,lng,Latr,Lngr;

    private int mCount = 0;


    private Handler mHandler = new Handler();


    private boolean mThreadActive = true;

    private LatLng sosLocation;

    protected Runnable mTask = new Runnable() {

        @Override
        public void run() {
            final GetSOS d=new GetSOS(con);
            lat=d.lat;
            lng=d.lng;
            Latr=d.Latr;
            Lngr=d.Lngr;

            while (mThreadActive) {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }


                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(mThreadActive==true) {
                            //mCount++;
                            //showText("Service was bound.");


                                scount=d.geter(lat,lng,Latr,Lngr, con);
                                //lcount=d.lcount;

                            if(scount>oldscount&&flag==1) {
                                sosLocation = d.getSosLocation();
                                showNotification(TryService.this);

                            }
                           /* if(lcount>oldlcount&&flag==1)
                            {
                                showNotification2(TryService.this);
                            }
                            */
                            flag=1;


                                oldscount=scount;
                                //oldlcount=lcount;
                            showText("Service was bound."+scount);
                        }
                    }
                });
            }


            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    showText("Thread end");
                }
            });
        }
    };
    private Thread mThread;

    // ______________________________________________________________________________
    /**

     */
    private void showText(Context ctx, final String text) {
        Toast.makeText(this, TAG + text, Toast.LENGTH_SHORT).show();
    }

    // ______________________________________________________________________________
    /**

     */
    private void showText(final String text) {
        showText(this, text);
    }


    // ______________________________________________________________________________
    @Override
    public IBinder onBind(Intent intent) {
        this.showText("Service was bound.");
        return null;
    }

    // ______________________________________________________________________________
    @Override   // onStartCommand:
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        this.showText("onStartCommand");
        this.mThread = new Thread(null, mTask, "NortifyingService");
        this.mThread.start();


        //showNotification(this);


        return START_STICKY;
    }

    // ______________________________________________________________________________
    @Override
    public void onCreate() {
        this.showText("Service has been begun.");
        super.onCreate();
    }


    // ______________________________________________________________________________
    @Override   // onDestroy:
    public void onDestroy() {
        this.showText("Service has been ended.");


        this.mThread.interrupt();
        this.mThreadActive = false;

        this.stopNotification(this);
        super.onDestroy();
    }

    // ______________________________________________________________________________

    public static void stopNotification(final Context ctx) {
        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(R.layout.activity_service);
    }

    // ______________________________________________________________________________

    private void showNotification(final Context ctx) {

        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);

       // Intent intent = new Intent(ctx, ActivityService.class);
        Intent intent = new Intent(ctx, MapActivity2.class);
        intent.putExtra("isIntent", true);
        intent.putExtra("latitude", sosLocation.latitude);
        intent.putExtra("longitude", sosLocation.longitude);

        Log.v("TryService", new Double(sosLocation.latitude).toString());
        Log.v("TryService", new Double(sosLocation.longitude).toString());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Notification n = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("There is a rescue request person..")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("SOS")
                .setContentText("When a tap is done, the location of the linchpin savior is indicated.")
                .setContentIntent(contentIntent)
                .build();
        n.defaults |= Notification.DEFAULT_ALL;
        n.flags = Notification.FLAG_NO_CLEAR;

        mgr.notify(R.layout.activity_service, n);

    }
    private void showNotification2(final Context ctx) {

        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent intent = new Intent(ctx, ActivityService.class);
        Intent intent = new Intent(ctx, MainActivity.class);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Notification n = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("There is a rescue request for List person..")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("SOS")
                .setContentText("When a tap is done, the location of the linchpin savior is indicated.")
                .setContentIntent(contentIntent)
                .build();
        n.defaults |= Notification.DEFAULT_ALL;
        n.flags = Notification.FLAG_NO_CLEAR;

        mgr.notify(R.layout.activity_service, n);

    }

    //This bar, please choose "service end" behind the tap.
}
