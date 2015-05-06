package com.example.vinothbaalaji.pollencarefinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
static TextView lbl_guest;
static String pollenValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MongoPollenDataTask pollenTask = new MongoPollenDataTask();
        pollenTask.execute();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String t = date.toString();
        MongoQueryTask task = new MongoQueryTask();
        task.execute();
        TextView pollenCount = (TextView)findViewById(R.id.pollenCount);
        pollenCount.setText(MongoPollenDataTask.pollenCount);
        TextView pollenType = (TextView)findViewById(R.id.pollenType);
        pollenType.setText("Predominant Pollen Type:" +MongoPollenDataTask.pollenType);
        TextView dateTime = (TextView)findViewById(R.id.date);
        dateTime.setText(t);
        lbl_guest= (TextView)findViewById(R.id.lbl_guest);
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
        if (id == R.id.action_devices) {
           // Intent device = new Intent(MainActivity.this,DevicesActivity.class);
            Intent device = new Intent(MainActivity.this,Temp.class);
            startActivity(device);
            //return true;
        }
        if (id == R.id.action_pollenMap) {
            Intent maps = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(maps);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
}