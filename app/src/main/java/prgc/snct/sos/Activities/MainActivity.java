package prgc.snct.sos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import prgc.snct.sos.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton ibutton = (ImageButton)findViewById(R.id.imageButton2);
        ImageButton ibutton2 = (ImageButton)findViewById(R.id.imageButton3);
        ImageButton ibutton3 = (ImageButton)findViewById(R.id.imageButton4);

        Button button4 = (Button)findViewById(R.id.button4);

        ibutton.setOnClickListener(this);
        ibutton2.setOnClickListener(this);
        ibutton3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    public void onClick(View v) {
        if(v.getId() == R.id.imageButton2){

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            //intent.setClassName("sos.Activities", "sos.Activities.MapsActivity");
            startActivity(intent);

        }else if(v.getId() == R.id.imageButton3){

            Intent intent = new Intent(MainActivity.this,BluetoothMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.BluetoothMain");
            startActivity(intent);

        }else if(v.getId() == R.id.imageButton4) {

            Intent intent = new Intent(MainActivity.this,DatabaseMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }else if(v.getId() == R.id.button4) {

            Intent intent = new Intent(MainActivity.this,TransceiverMain.class);
            startActivity(intent);

        }
    }
}
