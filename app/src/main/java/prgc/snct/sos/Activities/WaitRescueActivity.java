package prgc.snct.sos.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import prgc.snct.sos.R;

/**
 * Created by 航輔 on 2015/10/05.
 */
public class WaitRescueActivity extends ActionBarActivity implements View.OnClickListener {
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    MediaPlayer mp;
    Camera c = Camera.open();
    AudioManager am ;
    int ringVolume;
    boolean light=false,voice=false;
    TextView textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrescue);
        // ダイアログの設定
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("OK");          //タイトル
        alertDialog.setMessage("Send SOS");      //内容


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO 自動生成されたメソッド・スタブ
                Log.d("AlertDialog", "Positive which :" + which);


            }
        });

        // ダイアログの作成と表示
        alertDialog.create();
        alertDialog.show();

        mp = MediaPlayer.create(this, R.raw.alarm1);
        try{
            mp.prepare();
        }catch( Exception e ){ }

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ringVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        mp.setLooping(true);
        mp.setVolume(1.0f, 1.0f);

        b1=(Button)findViewById(R.id.vonLL);
        //b2=(Button)findViewById(R.id.voffLL);

        b1.setOnClickListener(this);
        //b2.setOnClickListener(this);

        b3=(Button)findViewById(R.id.lonLL);
        //b4=(Button)findViewById(R.id.loffLL);

        b3.setOnClickListener(this);
        //b4.setOnClickListener(this);


        b5=(Button)findViewById(R.id.Ambulance);


        b5.setOnClickListener(this);
        //b6=(Button)findViewById(R.id.comres);


        //b6.setOnClickListener(this);

         textView1 = (TextView)findViewById(R.id.vonLL);
        textView2 = (TextView)findViewById(R.id.lonLL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.vonLL:
                if(voice==false) {
                    voice=true;
                    textView1.setText("音を消す");
                    int ringMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


                    // 音量を設定する
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, ringMaxVolume, 0);
                    mp.start();
                }
                else
                {

                    voice=false;
                    textView1.setText("音を鳴らす");
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, ringVolume, 0);
                    mp.pause();
                    mp.seekTo(0);
                }
                break;
            /*case R.id.voffLL:
                am.setStreamVolume(AudioManager.STREAM_MUSIC, ringVolume, 0);
                mp.pause();
                mp.seekTo(0);
                break;
                */
            case R.id.lonLL:
                if(light==false) {
                    light=true;
                    textView2.setText("光を消す");
                    Camera.Parameters cp = c.getParameters();
//フラッシュモードを"常に点灯"に設定（Android OS Verに依存？）
                    cp.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//パラメータを設定
                    c.setParameters(cp);
//プレビューをしないと光らない
                    c.startPreview();
                }
                else
            {
                light=false;
                textView2.setText("光を点ける");
                Camera.Parameters cp2 = c.getParameters();
//フラッシュモードを"常に点灯"に設定（Android OS Verに依存？）
                cp2.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//パラメータを設定
                c.setParameters(cp2);
//プレビューをしないと光らない
                c.startPreview();
            }
                break;
            /*case R.id.loffLL:
                Camera.Parameters cp2 = c.getParameters();
//フラッシュモードを"常に点灯"に設定（Android OS Verに依存？）
                cp2.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//パラメータを設定
                c.setParameters(cp2);
//プレビューをしないと光らない
                c.startPreview();
                break;
                */
            case R.id.Ambulance:
                Uri uri = Uri.parse("tel:119");
                Intent i = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(i);
                break;
            //case R.id.comres:
                //break;

        }
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
    public void onStop(){
        super.onStop();
        //制御しているカメラデバイスのインスタンス
        c.release();
        mp.stop();
        mp.release();

        am.setStreamVolume(AudioManager.STREAM_MUSIC, ringVolume, 0);
    }
}