package prgc.snct.sos.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import prgc.snct.sos.R;

public class ListMain extends ActionBarActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));

        Button button = (Button)findViewById(R.id.button7);

        button.setOnClickListener(this);

        // リストビューに表示するためのデータを設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("listview item 1");
        adapter.add("listview item 2");
        adapter.add("listview item 3");

        // リストビューにデータを設定
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        ListView listview2 = (ListView)findViewById(R.id.listView2);
        listview2.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button7){
            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("リストに追加");
            builder.setView(layout);
            builder.setPositiveButton("OK", new OnClickListener () {
                public void onClick(DialogInterface dialog, int which) {
                    // OK ボタンクリック処理
                    // ID と NAME を取得
                    EditText id
                            = (EditText)layout.findViewById(R.id.customDlg_id);
                    EditText name
                            = (EditText)layout.findViewById(R.id.customDlg_name);
                    String strId   = id.getText().toString();
                    String strName = name.getText().toString();


                }
            });
            builder.setNegativeButton("Cancel", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // 表示
            builder.create().show();
        }
    }
}