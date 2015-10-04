package prgc.snct.sos.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import prgc.snct.sos.R;

// ______________________________________________________________________________
// サービス本体
public class TryService extends Service {

    private final static String TAG = "TryService#";
    int oldscount=0;
    int flag=0;
    Context con =this;
    int scount;
    // Toastを何回表示されたか数えるためのカウント
    private int mCount = 0;

    // Toastを表示させるために使うハンドラ
    private Handler mHandler = new Handler();

    // スレッドを停止するために必要
    private boolean mThreadActive = true;

    // スレッド処理
    protected Runnable mTask = new Runnable() {

        @Override
        public void run() {

            // アクティブな間だけ処理をする
            while (mThreadActive) {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }

                // ハンドラーをはさまないとToastでエラーでる
                // UIスレッド内で処理をしないといけないらしい
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(mThreadActive==true) {
                            mCount++;

                            GetSOS d=new GetSOS();

                                scount=d.geter(con);


                            if(scount>oldscount) {
                                showNotification(TryService.this);
                            }
                            flag=1;

                            //if(oldscount<scount)
                                oldscount=scount;


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
     * テキストを表示する
     * @param text 表示したいテキスト
     */
    private void showText(Context ctx, final String text) {
        Toast.makeText(this, TAG + text, Toast.LENGTH_SHORT).show();
    }

    // ______________________________________________________________________________
    /**
     * テキストを表示する
     * @param text テキスト
     */
    private void showText(final String text) {
        showText(this, text);
    }


    // ______________________________________________________________________________
    @Override   // onBind:サービスがバインドされたときに呼び出される
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

        // 通知バーを表示する
        //showNotification(this);

        // 戻り値でサービスが強制終了されたときの挙動が変わる
        // START_NOT_STICKY,START_REDELIVER_INTENT,START_STICKY_COMPATIBILITY
        return START_STICKY;
    }

    // ______________________________________________________________________________
    @Override   // onCreate:サービスが作成されたときに呼びされる(最初に1回だけ)
    public void onCreate() {
        this.showText("Service has been begun.");
        super.onCreate();
    }


    // ______________________________________________________________________________
    @Override   // onDestroy:
    public void onDestroy() {
        this.showText("Service has been ended.");

        // スレッド停止
        this.mThread.interrupt();
        this.mThreadActive = false;

        this.stopNotification(this);
        super.onDestroy();
    }

    // ______________________________________________________________________________
    // 通知バーを消す
    public static void stopNotification(final Context ctx) {
        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(R.layout.activity_service);
    }

    // ______________________________________________________________________________
    // 通知バーを出す
    private void showNotification(final Context ctx) {

        NotificationManager mgr = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx, ActivityService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 通知バーの内容を決める
        Notification n = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("There is a rescue request person..")
                .setWhen(System.currentTimeMillis())    // 時間
                .setContentTitle("SOS")
                .setContentText("When a tap is done, the location of the linchpin savior is indicated.")
                .setContentIntent(contentIntent)// インテント
                .build();
        n.defaults |= Notification.DEFAULT_ALL;
        n.flags = Notification.FLAG_NO_CLEAR;

        mgr.notify(R.layout.activity_service, n);

    }

    //This bar, please choose "service end" behind the tap.
}
