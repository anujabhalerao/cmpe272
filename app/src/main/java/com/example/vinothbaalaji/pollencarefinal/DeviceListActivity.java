package com.example.vinothbaalaji.pollencarefinal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DeviceListActivity extends Activity {
ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        lv = (ListView)findViewById(R.id.deviceListView);
        lv.setAdapter(new MyCustomAdapter(this));
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
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
}

class MyCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list;

   MyCustomAdapter(Context c){
    this.context = c;
    list = new ArrayList<String>();
    for (int i =0;i<MongoQueryTask.deviceList.size();i++){
        list.add(MongoQueryTask.deviceList.get(i).toString());
    }

   }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.device_row, parent, false);
        TextView name = (TextView) row.findViewById(R.id.deviceName);
        String temp = list.get(position);
        name.setText(temp);
        return row;
    }
}
