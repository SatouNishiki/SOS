package prgc.snct.sos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GridViewPager gvp = (GridViewPager) findViewById(R.id.gridPageView);
        gvp.setAdapter(new GridViewPagerAdapter(this.getFragmentManager()));
    }

    static class GridViewPagerAdapter extends FragmentGridPagerAdapter {

        public GridViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getFragment(int row, int col) {
            return MyFragment.newInstance();
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return 1;
        }
    }

    static class MyFragment extends CardFragment {
        private GoogleApiClient client;

        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.client = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.d("MyFragment", "onConnected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d("MyFragment", "onConnectionSuspended");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("MyFragment", "onConnectionFailed");
                        }
                    })
                    .addApi(Wearable.API)
                    .build();
            this.client.connect();

            Button button = new Button(this.getActivity());
            button.setText("OK");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MyFragment", "onClick");
                            final String message = "Hello world";
                            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                            for (Node node : nodes.getNodes()) {
                                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                        client,
                                        node.getId(),
                                        "/hello",
                                        message.getBytes())
                                        .await();
                                if (result.getStatus().isSuccess()) {
                                    Log.d("onClick", "isSuccess is true");
                                } else {
                                    Log.d("onClick", "isSuccess is false");
                                }
                            }
                        }
                    }).start();
                }
            });
            return button;
        }

        public static MyFragment newInstance() {
            return new MyFragment();
        }
    }
}
/*
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.Activity;


import com.google.android.gms.common.ConnectionResult;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        sendButton = (Button)findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(this);


    }



    private void sendToast(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("wear", "run()");
                GoogleApiClient client = new GoogleApiClient.Builder(context)
                        .addApi(Wearable.API)
                        .build();
                client.blockingConnect(100, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    client.blockingConnect(100, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodes.get(0).getId(), "hello", null);
                    Log.d("wear", nodes.get(0).getId());
                    client.disconnect();
                    Log.v("wear", "disconnect1");
                }
                client.disconnect();
                Log.v("wear", "disconnect2");
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        Log.v("wear", "OnClick");
        if((Button)view == sendButton) {
            Log.v("wear", "sendToast()");
            sendToast(this);
        }
    }*/

