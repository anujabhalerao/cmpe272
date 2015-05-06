package com.example.vinothbaalaji.pollencarefinal;

import android.os.AsyncTask;

import com.mongodb.BasicDBObject;
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
public class MongoPollenDataTask extends AsyncTask<Void, Void, Void> {

    static ArrayList deviceList = new ArrayList<String>();
    static ArrayList deviceIds = new ArrayList<String>();
    static String pollenCount;
    static String pollenType;
    static String timeStamp;
    @Override
    protected Void doInBackground(Void... params) {
        MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031852.mongolab.com:31852/team6");
        MongoClient client;
        try {
            client = new MongoClient(mongoLab);
            DB database = client.getDB("team6");
            DBCollection pollenCollection = database.getCollection("pollen");
            BasicDBObject query = new BasicDBObject("deviceid","d45e2aaa3ddf");
            DBCursor dcursor = pollenCollection.find(query);
            dcursor.sort(new BasicDBObject("timestamp",-1));
            if(dcursor.hasNext()){
                DBObject doc = dcursor.next();
                pollenType = doc.get("pollentype").toString();
                pollenCount = doc.get("pollencount").toString();
                timeStamp = doc.get("timestamp").toString();
             }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
           return null;
    }
}