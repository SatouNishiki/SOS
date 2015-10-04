package prgc.snct.sos.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prgc.snct.sos.R;

public class ListMain extends ActionBarActivity implements View.OnClickListener {

    ArrayList<String> names;
    ArrayList<String> id;
    int leng;
    Map<String, String> conMap;
    List<Map<String, String>> contactlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));

        names = new ArrayList<String>();
        names.add("NULL");
        id = new ArrayList<String>();
        id.add("NULL");

        //loadList();

        leng = names.size();

        Button button = (Button)findViewById(R.id.button7);
        ListView listView = (ListView) findViewById(R.id.listView);
        ListView listTitle = (ListView) findViewById(R.id.listView0);

        button.setOnClickListener(this);

        contactlist = new ArrayList<Map<String, String>>();
        List<Map<String, String>> titlecontactlist = new ArrayList<Map<String, String>>();

        setMap();

        Map<String, String> titleConMap = new HashMap<String, String>();
        titleConMap.put("Name", "Name");
        titleConMap.put("ID", "ID");
        titlecontactlist.add(titleConMap);

        SimpleAdapter adapter = new SimpleAdapter(this, contactlist, android.R.layout.simple_list_item_2, new String[] { "Name", "ID" }, new int[] { android.R.id.text1, android.R.id.text2 });
        SimpleAdapter titleAdapter = new SimpleAdapter(this, titlecontactlist, android.R.layout.simple_list_item_2, new String[] { "Name", "ID" }, new int[] { android.R.id.text1, android.R.id.text2 });
        listView.setAdapter(adapter);
        listTitle.setAdapter(titleAdapter);

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
                    EditText Id = (EditText)layout.findViewById(R.id.customDlg_id);
                    EditText name = (EditText)layout.findViewById(R.id.customDlg_name);
                    String strId   = Id.getText().toString();
                    String strName = name.getText().toString();
                    names.add(strName);
                    id.add(strId);
                    deleteNull();
                    //saveList();
                    leng++;
                    setMap();
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

    public void saveList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        // objectをjson文字列へ変換
        String jsonNamesString = gson.toJson(names);
        String jsonIdString = gson.toJson(id);
        // 変換後の文字列をputStringで保存
        prefs.edit().putString("ARRAY_NAMES",jsonNamesString).apply();
        prefs.edit().putString("ARRAY_ID",jsonIdString).apply();
    }

    public void loadList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        // 保存されているjson文字列を取得
        String savedNamesString = prefs.getString("ARRAY_NAMES", "");
        String savedIdString = prefs.getString("ARRAY_ID", "");
        // json文字列をArrayListクラスのインスタンスに変換
        names = gson.fromJson(savedNamesString, ArrayList.class);
        id = gson.fromJson(savedIdString, ArrayList.class);
    }

    public void setMap(){
        contactlist.clear();
        if("NULL".equals(names.get(0))) {
            conMap = new HashMap<String, String>();
            conMap.put("Name", "リストに登録されている人がいません");
            conMap.put("ID", "");
            contactlist.add(conMap);
            leng = 0;
        }else{
            for (int i = 0; i < leng; i++) {
                conMap = new HashMap<String, String>();
                conMap.put("Name", names.get(i));
                conMap.put("ID", id.get(i));
                contactlist.add(conMap);
            }
        }
    }

    public void deleteNull(){
        if("NULL".equals(names.get(0))){
            names.remove(0);
            id.remove(0);
        }
    }
}