package com.example.vinothbaalaji.pollencarefinal;

import android.os.AsyncTask;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by VinothBaalaji on 5/2/2015.
 */
public class MongoQueryTask extends AsyncTask<Void, Void, String> {

    static ArrayList deviceList = new ArrayList<String>();
    static ArrayList deviceIds = new ArrayList<String>();

    @Override
    protected String doInBackground(Void... params) {
        MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031852.mongolab.com:31852/team6");
        MongoClient client;

        String deviceName;
        String deviceId;
        try {
            client = new MongoClient(mongoLab);
            DB database = client.getDB("team6");
            DBCollection userCollection = database.getCollection("user");
            //BasicDBObject query = new BasicDBObject("deviceId","BUY");
            DBCursor dcursor = userCollection.find();
            //dcursor.sort(new BasicDBObject("timestamp",-1));
            while(dcursor.hasNext()){
                DBObject doc = dcursor.next();
                deviceName = doc.get("deviceName").toString();
                deviceId = doc.get("deviceId").toString();
                deviceList.add(deviceName);
                deviceIds.add(deviceId);

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}