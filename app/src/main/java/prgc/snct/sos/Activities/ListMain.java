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

public class ListMain extends Activity implements View.OnClickListener {

    SimpleAdapter adapter;
    List<Map<String, String>> contactlist;
    boolean empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
        empty = true;

        contactlist = new ArrayList<Map<String, String>>();

        loadList();

        Button addButton = (Button)findViewById(R.id.button7);
        Button clearButton = (Button)findViewById(R.id.button8);
        ListView listView = (ListView) findViewById(R.id.listView);
        ListView listTitle = (ListView) findViewById(R.id.listView0);

        addButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        List<Map<String, String>> titlecontactlist = new ArrayList<Map<String, String>>();

        setMap();

        Map<String, String> titleConMap = new HashMap<String, String>();
        titleConMap.put("Name", "Name");
        titleConMap.put("ID", "ID");
        titlecontactlist.add(titleConMap);

        adapter = new SimpleAdapter(this, contactlist, android.R.layout.simple_list_item_2, new String[] { "Name", "ID" }, new int[] { android.R.id.text1, android.R.id.text2 });
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
                    if(empty){
                        contactlist.clear();
                    }
                    Map<String, String> newLine = new HashMap<String, String>();
                    newLine.put("Name", strName);
                    newLine.put("ID", strId);
                    contactlist.add(newLine);
                    saveList();
                    setMap();
                }
            });
            builder.setNegativeButton("Cancel", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // 表示
            builder.create().show();

        }else if(v.getId() == R.id.button8){

            contactlist.clear();
            saveList();
            setMap();
            empty = true;

        }
    }

    public void saveList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        // objectをjson文字列へ変換
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < contactlist.size(); ++i)
        {
            names.add(contactlist.get(i).get("Name"));
            ids.add(contactlist.get(i).get("ID"));
        }
        String jsonNamesString = gson.toJson(names);
        String jsonIdString = gson.toJson(ids);
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
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        if((gson.fromJson(savedNamesString, ArrayList.class)).isEmpty() == false){
            names = gson.fromJson(savedNamesString, ArrayList.class);
            ids = gson.fromJson(savedIdString, ArrayList.class);
            empty = false;
        }
        contactlist.clear();
        for (int i = 0; i < names.size(); ++i)
        {
            Map<String, String> line = new HashMap<String, String>();
            line.put("Name", names.get(i));
            line.put("ID", ids.get(i));
            contactlist.add(line);
        }
    }

    public void setMap(){
        if(contactlist.isEmpty()) {
            Map<String, String> line = new HashMap<String, String>();
            line.put("Name", "リストに登録されている人がいません");
            line.put("ID", "");
            contactlist.add(line);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}