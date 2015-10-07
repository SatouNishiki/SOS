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


        Button button5 = (Button)findViewById(R.id.button5);
        //Button button6 = (Button)findViewById(R.id.button6);　chart(Mounting ly.)
        //showDialog(1); name(Mounting ly.)

        ibutton.setOnClickListener(this);
        ibutton2.setOnClickListener(this);
        ibutton3.setOnClickListener(this);

        button4.setOnClickListener(this);

        button5.setOnClickListener(this);
        //button6.setOnClickListener(this);

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

                //���C�A�E�g�̌Ăяo��
                LayoutInflater factory = LayoutInflater.from(this);
                final View inputView = factory.inflate(R.layout.input_dialog, null);

                //�_�C�A���O�̍쐬(AlertDialog.Builder)
                return new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("name")
                    .setView(inputView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                    /* int whichButton �ɂ��A�����ꂽ�{�^���𔻒� */
                    /* ��t���� �F���͂��ꂽ�e�L�X�g�̏����Ȃ� */
                        }
                    })

                        .create();
        }

        return null;
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageButton2){

       //     Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            Intent intent = new Intent(MainActivity.this, MapActivity2.class);
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
        else if(v.getId() == R.id.button5) {

            Intent intent = new Intent(MainActivity.this,ActivityService.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }
        else if(v.getId() == R.id.button6) {

            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
            //intent.setClassName("prgc.snct.sos.Activities", "prgc.snct.sos.Activities.DatabaseMain");
            startActivity(intent);

        }
    }
}
