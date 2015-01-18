package com.triwalks.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.triwalks.Common.TripLineInfo;
import com.triwalks.Common.Tripline;
import com.triwalks.RestClient.api.TripLine;
import com.triwalks.RestClient.api.UserProfile;
import com.triwalks.RestClient.model.ListStringData;
import com.triwalks.RestClient.model.ResultData;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by james on 1/12/15.
 */
public class TripLineInitService extends IntentService {
    String userID;
    public TripLineInitService(){
        super("TripLinePullService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        Bundle bundle = workIntent.getExtras();
        String userID = bundle.getString("userID");
        String sessionID = bundle.getString("sessionID");
        getTripIDs(userID, sessionID);

    }

    private void getTripIDs(String userID ,String sessionID) {

        TripLine.getTripApiClient().getTripids(userID, sessionID, new Callback<ListStringData>(){
            @Override
            public void success(ListStringData resultData, Response response) {
                System.out.println("get trip ids success");
                Gson gson = new Gson();
                String str = gson.toJson(resultData);
                System.out.println(str);
                onGetTripIDsSuccess(resultData.getList_string());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("get trip ids Error");
            }
        });
    }

    public void onGetTripIDsSuccess(List<String> triplineIDs){
        if(triplineIDs != null) {
            for (String triplineID : triplineIDs) {
                Tripline tripline = new Tripline(triplineID);
                TripLineInfo.getInstance().addTripLine(triplineID, tripline);
            }
        }
        stopSelf();
    }
}
