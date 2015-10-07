package prgc.snct.sos.Activities;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;

import prgc.snct.sos.R;

public class ActivityService extends Activity {

    private static final String TAG = "ActivityService";
    // ______________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        TryService.stopNotification(ActivityService.this);
        // �T�[�r�X���J�n����{�^��
        Button btn3 = (Button)this.findViewById(R.id.button3);
        btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityService.this, prgc.snct.sos.Activities.TryService.class);

                startService(intent);
            }
        });

        // �T�[�r�X���~����{�^��
        Button btn4 = (Button)this.findViewById(R.id.button4);
        btn4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityService.this, prgc.snct.sos.Activities.TryService.class);
                stopService(intent);
            }
        });

    }

    // ______________________________________________________________________________
    /**
     * �T�[�r�X�����s����
     * @param className �T�[�r�X�̃N���X��
     * @return true: ���s���ł�
     */
    private boolean isServiceRunnig(String className) {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);

        Log.i(TAG, "Search Start: " + className);
        for (RunningServiceInfo curr : listServiceInfo) {
            Log.i(TAG , "Check: " + curr.service.getClassName());
            if (curr.service.getClassName().equals(className)) {
                Log.i(TAG , ">>>>>>FOUND!");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.activity, menu);
        return true;
    }

}
