package pl.kmikulski.kalendarzyk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView) findViewById(R.id.main_text);
        Button bYes = (Button) findViewById(R.id.button_yes);
        Button bNo = (Button) findViewById(R.id.button_no);
        final SharedPreferences prefs = getSharedPreferences(Util.PREFS_DATA, 0);
        Date date = new Date();
        final String key = Util.getIntervalKey(date);
        if((!prefs.contains(key)) && Util.shouldAsk(date)) {
            tv.setText(key + "?");
            bYes.setVisibility(View.VISIBLE);
            bNo.setVisibility(View.VISIBLE);
            bYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefs.edit().putBoolean(key, true).commit();
                    Util.scheduleJob(getApplicationContext());
                    finish();
                }
            });
            bNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefs.edit().putBoolean(key, false).commit();
                    Util.scheduleJob(getApplicationContext());
                    finish();
                }
            });
        } else {
            if(prefs.contains(key)) {
                tv.setText(key + " - " + (prefs.getBoolean(key, false) ? "TAK" : "NIE"));
            } else {
                tv.setText(key + " - zapytam wieczorem");
            }
            bYes.setVisibility(View.INVISIBLE);
            bNo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            return true;
        } else if (id == R.id.action_export) {
            Util.copyToClipboard(getApplicationContext(), "Kalendarzyk", Util.getXml(getApplicationContext()));
            Util.showToast(getApplicationContext(), "Dane skopiowano do schowka");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
