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
public class MongoMapDetailsTask extends AsyncTask<Void, Void, Void> {

    static ArrayList geoList = new ArrayList<MapData>();

    @Override
    protected Void doInBackground(Void... params) {
        MongoClientURI mongoLab = new MongoClientURI("mongodb://admin:admin@ds031852.mongolab.com:31852/team6");
        MongoClient client;
        MapData m = new MapData();
        String latitude;
        String longitude;
        try {
            client = new MongoClient(mongoLab);
            DB database = client.getDB("team6");
            DBCollection userCollection = database.getCollection("user");
            DBCursor dcursor = userCollection.find();
            while(dcursor.hasNext()){
                DBObject doc = dcursor.next();
                latitude = doc.get("latitude").toString();
                longitude = doc.get("longitude").toString();
                m.setLatitude(latitude);
                m.setLongtitude(longitude);
                geoList.add(m);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}