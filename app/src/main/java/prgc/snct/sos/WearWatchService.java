package prgc.snct.sos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import prgc.snct.sos.Activities.TransceiverMain;

public class WearWatchService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("mobile", "receiveMessage");
        Log.v("mobile", messageEvent.getPath());
     //   showToast(messageEvent.getPath());

        Intent intent = new Intent(this.getApplicationContext(), TransceiverMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AutoClick", true);
        startActivity(intent);
    }

    private void showToast(String message) {
        Log.v("mobile", "showToast()");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
