package com.example.vinothbaalaji.pollencarefinal;

import android.os.AsyncTask;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Created by VinothBaalaji on 5/2/2015.
 */
public class MongoService extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031852.mongolab.com:31852/team6");
        MongoClient client;
        for(String param : params) {
            String[] paramArray = param.split("\\|");
            String deviceId = paramArray[0];
            String deviceName = paramArray[1];
            String latitude = paramArray[2];
            String longitude = paramArray[3];
            try {
                client = new MongoClient(mongoLab);
                DB database = client.getDB("team6");
                DBCollection userCollection = database.getCollection("user");
                BasicDBObject userData = new BasicDBObject();
                userData.append("deviceId", deviceId);
                userData.append("deviceName", deviceName);
                userData.append("latitude", latitude);
                userData.append("longitude", longitude);
                userCollection.insert(userData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }
}



