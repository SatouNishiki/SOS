package prgc.snct.sos.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import prgc.snct.sos.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);

        showDialog(1);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 1:

                //レイアウトの呼び出し
                LayoutInflater factory = LayoutInflater.from(this);
                final View inputView = factory.inflate(R.layout.input_dialog, null);

                //ダイアログの作成(AlertDialog.Builder)
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("name")
                    .setView(inputView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                    /* int whichButton により、押されたボタンを判定 */
                    /* 受付処理 ：入力されたテキストの処理など */
                        }
                    })

                        .create();
        }

        return null;
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button){

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            //intent.setClassName("sos.Activities", "sos.Activities.MapsActivity");
            startActivity(intent);

        }else if(v.getId() == R.id.button2){

            Intent intent = new Intent(MainActivity.this,BluetoothMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.BluetoothMain");
            startActivity(intent);

        }else if(v.getId() == R.id.button3) {

            Intent intent = new Intent(MainActivity.this,DatabaseMain.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }
        else if(v.getId() == R.id.button4) {

            Intent intent = new Intent(MainActivity.this,ActivityService.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }
        else if(v.getId() == R.id.button5) {

            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }
    }
}
