package prgc.snct.sos.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


import prgc.snct.sos.R;

public class DatabaseMain extends ActionBarActivity {

    private static final String url = "jdbc:mysql://160.16.91.195:3306/sos_db";
    private static final String user = "snctprocon2015";
    private static final String pass = "kadai";
    String result = " ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
        final Handler handler=new Handler();
        final TextView tv=new TextView(this);
        tv.setText("It's now trying to connect.");
        layout.addView(tv);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver"); // JDBCドライバをロード
                } catch (ClassNotFoundException e) {
                    result = "Cannot read the driver. "+ e.toString();
                }

                try {

                    Connection con = (Connection) DriverManager.getConnection(url, user, pass); // サーバに接続

                    result = "We have successfully connected to the sos_db.\n";
                    Statement st = (Statement) con.createStatement();

                    ResultSet rs = st.executeQuery("select * from sos_stat");
                    result += "We have successfully connected to the table 'sos_stat'.\n";

                    ResultSet rs2 = st.executeQuery("select * from sos_nlist");
                    result += "We have successfully connected to the table 'sos_nlist'.\n";

                    rs2.close();
                    rs.close();
                    st.close();
                    con.close();
                }  catch (SQLException e) {
                    e.printStackTrace();
                    result = e.toString();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(result);
                    }
                });
            }
        }).start();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
