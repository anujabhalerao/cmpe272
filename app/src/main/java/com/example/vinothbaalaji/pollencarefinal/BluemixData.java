package com.example.vinothbaalaji.pollencarefinal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by VinothBaalaji on 4/26/2015.
 */
public class BluemixData extends Activity {

    private static final String UPDATE_UI = "UPDATE_UI";
    private static final String OP_COMPLETE = "OP_COMPLETE";
    private DeviceItem dataItemToAdd = null;
    private DeviceItem dataItemToDelete = null;
    private ProgressDialog progressDialog;

    private final int DELETE_COMPLETE = 1 << 0;
    private final int ADD_COMPLETE = 1 << 1;
    private final int RETRIEVE_COMPLETE = 1 << 2;
    private int COMPLETES = 0;

    private BroadcastReceiver UIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("VALUE");
            //deviceId.setText(value);
            progressDialog.dismiss();
            progressDialog = null;
        }
    };

    private BroadcastReceiver OPReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            COMPLETES |= intent.getIntExtra("VALUE",0);
            if(COMPLETES >= ADD_COMPLETE) {
                COMPLETES = 0;
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        }
    };

    /*@Override
    public void onResume(){
        super.onResume();
        //deviceId = (EditText)findViewById(R.id.text_deviceId);
        LocalBroadcastManager.getInstance(this).registerReceiver(UIReceiver,new IntentFilter(UPDATE_UI));
        LocalBroadcastManager.getInstance(this).registerReceiver(OPReceiver,new IntentFilter(OP_COMPLETE));
        retrieveData();
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UIReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(OPReceiver);
    }*/

    private void retrieveData(){
        progressDialog = ProgressDialog.show(this,"Retrieving data","Please wait...");
        try {
            Log.i("BMT", "Attempting to query");
            IBMQuery<DeviceItem> query = IBMQuery.queryForClass(DeviceItem.class);
            query.find().continueWith(new Continuation<List<DeviceItem>, Object>() {
                @Override
                public Object then(Task<List<DeviceItem>> listTask) throws Exception {
                    final List<DeviceItem> object = listTask.getResult();
                    if(listTask.isCancelled()){
                        Log.i("BMT","Task cancelled : " + listTask.toString());
                    }
                    else if(listTask.isFaulted()){
                        Log.i("BMT","Task fault : " + listTask.getError().getMessage());
                        throw listTask.getError();
                    }
                    else{
                        // process the success
                        for(DeviceItem dItem:object){
                            dataItemToDelete = dItem;   // assign to dataItemToDelete as this will
                            // be the next to be deleted
                            Log.i("BMT","Received data following fetch : " + dataItemToDelete.getValue());
                            Intent intent = new Intent(UPDATE_UI);
                            intent.putExtra("VALUE",dataItemToDelete.getValue());
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                        }
                    }
                    Intent intent = new Intent(OP_COMPLETE);
                    intent.putExtra("VALUE",RETRIEVE_COMPLETE);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                    return null;
                }
            });
        }
        catch(IBMDataException ex){
            Log.e(this.getLocalClassName(), "Exception : " + ex.getMessage());
        }
    }
    public void initializeConnection(){
        // initialise Bluemix
        Log.i("BMT", "Initialising Bluemix");
        IBMBluemix.initialize(this,         // context
                "http://272pollen.mybluemix.net",        // applicationId
                "ecf626219f2d54838058059d9d925baaf82fb193",    // applicationSecret
                "http://272pollen.mybluemix.net");    // applicationRoute

        Log.i("BMT","Initialising IBM Data");
        IBMData.initializeService();
        DeviceItem.registerSpecialization(DeviceItem.class);
    }
    public void submitData(String value){
        Log.i("BMT", "Attempting to submit : " + value);
        initializeConnection();
        progressDialog = ProgressDialog.show(this,"Submitting data","Please wait");
        deleteItem();

        dataItemToAdd = new DeviceItem();
        dataItemToAdd.setValue(value);
        dataItemToAdd.save().continueWith(new Continuation<IBMDataObject, Void>() {
            @Override
            public Void then(Task<IBMDataObject> ibmDataObjectTask) throws Exception {
                if(ibmDataObjectTask.isCancelled()){
                    Log.i("BMT","Task cancelled : " + ibmDataObjectTask.toString());
                }
                else if(ibmDataObjectTask.isFaulted()){
                    Log.i("BMT","Task fault : " + ibmDataObjectTask.getError().getMessage());
                    throw ibmDataObjectTask.getError();
                }
                else{
                    Log.i("BMT","Successfully submitted data");
                    Intent intent = new Intent(OP_COMPLETE);
                    intent.putExtra("VALUE",ADD_COMPLETE);
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                }
                return null;
            }
        });
    }

    public void deleteItem(){
        if(dataItemToDelete != null) {
            dataItemToDelete.delete().continueWith(new Continuation<IBMDataObject, Void>() {
                @Override
                public Void then(Task<IBMDataObject> ibmDataObjectTask) throws Exception {
                    if (ibmDataObjectTask.isCancelled()) {
                        Log.i("BMT", "Task cancelled : " + ibmDataObjectTask.toString());
                    } else if (ibmDataObjectTask.isFaulted()) {
                        Log.i("BMT", "Task fault : " + ibmDataObjectTask.getError().getMessage());
                        throw ibmDataObjectTask.getError();
                    } else {
                        Log.i("BMT","Successfully deleted Item");
                        dataItemToDelete = dataItemToAdd;   // assign the last item added as this
                        // as this will be the next to be deleted
                        Intent intent = new Intent(OP_COMPLETE);
                        intent.putExtra("VALUE",DELETE_COMPLETE);
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                    }
                    return null;
                }
            });
        }
    }
}
