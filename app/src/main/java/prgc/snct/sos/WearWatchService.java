package prgc.snct.sos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearWatchService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("mobile", "receiveMessage");
        Log.v("mobile", messageEvent.getPath());
        showToast(messageEvent.getPath());
    }

    private void showToast(String message) {
        Log.v("mobile", "showToast()");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
