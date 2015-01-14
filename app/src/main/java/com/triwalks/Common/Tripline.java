package com.triwalks.Common;

import android.graphics.Bitmap;

import com.triwalks.RestClient.model.AnalyzeTripOutData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tripline {
    private String triplineID;
    boolean isLoaded = false;
    int size;
    private ArrayList<String> sids;
//    ArrayList<Bitmap> triplinePhotos;
    Map<String, Bitmap> triplinePhotos;
//    ArrayList<AnalyzeTripOutData.PhotoTree.group.location> locations;
    Map<String, AnalyzeTripOutData.PhotoTree.group.location> locations;

    public Tripline(String triplineID){
        this.triplineID = triplineID;
        sids = new ArrayList<>();
        triplinePhotos = new HashMap<>();
        locations = new HashMap<>();
    }

    public String getTriplineID(){
        return triplineID;
    }

    public Tripline addSid(String sid){
        sids.add(sid);
        return this;
    }

    public String getSid(int p){
        return sids.get(p);
    }

    public Tripline addBitmap(String sid, Bitmap bitmap){
        triplinePhotos.put(sid, bitmap);
        return this;
    }

    public Bitmap getBitmap(String sid){
        return triplinePhotos.get(sid);
    }

    public Tripline addLocation(String sid, AnalyzeTripOutData.PhotoTree.group.location l){
        locations.put(sid, l);
        return this;
    }

    public double[] getLocation(String sid){
        double[] position = new double[2];
        AnalyzeTripOutData.PhotoTree.group.location l = locations.get(sid);
        position[0] = l.getX();
        position[1] = l.getY();
        return position;
    }

    public Tripline setSize(int len){
        size = len;
        return this;
    }

    public int getSize(){
        return size;
    }

    public boolean isLoaded(){
        return isLoaded;
    }

    public void setLoaded(boolean b){
        isLoaded = b;
    }

}
