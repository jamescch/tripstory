package com.triwalks.Common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// all tripline related functions are here, including user tripline
public class TripLineInfo {
    ArrayList<String> triplineIDs;
    Map<String, Tripline> triplines;


    private static TripLineInfo instance = null;

    public static TripLineInfo getInstance(){
        if(instance == null){
            synchronized (TripLineInfo.class){
                if(instance == null){
                    instance = new TripLineInfo();
                }
            }
        }
        return  instance;
    }

    public TripLineInfo(){
        triplineIDs = new ArrayList<>();
        triplines = new HashMap<>();
    }

    public void init(){

    }

    public Tripline getTripLine(String triplineID){
        // get specific user trip line with user id
        return triplines.get(triplineID);
    }

    public Tripline getTripLine(int p){
        // get specific user trip line with user id
        if(p < triplineIDs.size()) {
            return triplines.get(triplineIDs.get(p));
        }else{
            return null;
        }
    }

    public void addTripLine(String triplineID, Tripline tripline){
        triplineIDs.add(triplineID);
        triplines.put(triplineID, tripline);
    }

    public boolean updateTripLine(){
        // update trip line info after any modification
        return true; //if suc
    }

    public int getNumberOfTripLines(){
        return triplineIDs.size();
    }
}
