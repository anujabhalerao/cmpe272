package com.example.vinothbaalaji.pollencarefinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.location.IBMLocation;
import com.ibm.mobile.services.location.device.IBMLocationContext;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class DevicesActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest = new LocationRequest();
    boolean requestingLocationUpdate=true;
    Button register;
    double latitude;
    double longitude;
    EditText deviceId;
    EditText deviceName;
    BluemixData mobileData;
    IBMLocationContext locationContext;
    static List<DeviceItem> deviceList;
    static ArrayAdapter<DeviceItem> lvArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        setContentView(R.layout.activity_devices);
        initializeBluemixConnection();
        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        createItem(v);
        Intent mainPage = new Intent(DevicesActivity.this,MainActivity.class);
        startActivity(mainPage);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        return true;
    }

    public void initializeBluemixConnection(){
        Context c = this.getApplicationContext();
        // initialise Bluemix
        Log.i("BMT", "Initialising Bluemix");
        IBMBluemix.initialize(c,"5cb410e4-c4af-4eb8-b01f-7fa2eee4c87c","5d187aa611f7aa8fabbd8f08281536ff59165190","http://pollencare.mybluemix.net");
        Log.i("BMT","Initialising IBM Data");
        IBMData.initializeService();
        IBMLocation.initializeService();
        DeviceItem.registerSpecialization(DeviceItem.class);
    }
     public void createItem(View v) {
        EditText itemToAdd = (EditText) findViewById(R.id.text_deviceId);
        EditText nameToAdd = (EditText) findViewById(R.id.text_deviceName);
        String toAdd = itemToAdd.getText().toString()+'|'+nameToAdd.getText().toString()+'|'+latitude+'|'+longitude;
         MongoQueryTask queryTask = new MongoQueryTask();
         queryTask.execute();
        MongoService mongoTask = new MongoService();
         mongoTask.execute(new String[] {toAdd});
        DeviceItem deviceItem = new DeviceItem();
        if (!toAdd.equals("")) {
            deviceItem.setValue(toAdd);
            final Context context = this.getApplicationContext();
            // Use the IBMDataObject to create and persist the Item object.
            deviceItem.save().continueWith(new Continuation<IBMDataObject, Void>() {

                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
                    // Log if the save was cancelled.
                    if (task.isCancelled()){
                        Log.e("DevicesActivity", "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the save task fails.
                    else if (task.isFaulted()) {
                        Log.e("DeviceActivity", "Exception : " + task.getError().getMessage());
                    }

                    // If the result succeeds, load the list.
                    else {
                        //listItems();
                    }
                    return null;
                }

            });

            // Set text field back to empty after item is added.
            itemToAdd.setText("");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    private Location getLocation() {
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return lastKnownLocation;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        Location currentLocation = getLocation();
        if(currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
