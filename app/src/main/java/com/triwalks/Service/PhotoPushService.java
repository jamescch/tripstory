package com.triwalks.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.triwalks.Common.TripLineInfo;
import com.triwalks.Common.Tripline;
import com.triwalks.Common.UserData;
import com.triwalks.RestClient.api.TripLine;
import com.triwalks.RestClient.model.CreateTripLineData;
import com.triwalks.RestClient.model.ResultData;
import com.triwalks.RestClient.model.TripLineData;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by james on 1/4/15.
 */
public class PhotoPushService extends IntentService {
    String userid;
    String sessionid;
    double[] geotag;
    ArrayList<String> imageUrls;
    ArrayList<String> imageCids;

    public PhotoPushService(){
        super("PhotoPushService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        Bundle bundle = workIntent.getExtras();
        geotag = bundle.getDoubleArray("geotag");
        imageUrls = bundle.getStringArrayList("imageUrls");
        imageCids = new ArrayList<>();
//        userid = "54b0ec4c586e1f1870e4fc26";
        userid = UserData.getInstance().getUser_ID();
//        sessionid = "12345";
        sessionid = UserData.getInstance().getSession_ID();

        createTripLine(userid, sessionid, geotag);

//        System.out.println(dataString);
        // Do work here, based on the contents of dataString
        Intent localIntent =
                new Intent()
                        // Puts the status into the Intent
                        .putExtra("response", "test");
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


    }

    public void uploadTripLinePhoto(ArrayList<String> imageUrls, final String triplineID){

        for(int i = 0; i < imageUrls.size(); i++){
            File file = new File(imageUrls.get(i));
            TypedFile image = new TypedFile("image/jpg", file);
            TripLine.getTripApiClient().uploadPhoto(
                    image, new TypedString(userid), new TypedString(sessionid),
                    new TypedString(imageCids.get(i)), new TypedString(triplineID), new Callback<ResultData>() {
                        @Override
                        public void success(ResultData resultData, Response response) {
                            System.out.println("uploadTripLinePhoto success");
                            Gson gson = new Gson();
                            String str = gson.toJson(resultData);
                            System.out.println(str);


                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            System.out.println("uploadTripLinePhoto fail");
                        }
                    });
        }
    }

    public void createTripLine(String userID ,String sessionID, double[] geotag) {
        String path = createPath(geotag);
        String json = "{" +
                "\"userid\":\""+userID+"\"," +
                "\"sessionid\":\""+sessionID+"\","+
                "\"path\": [" +
                path+
                "],"+
                "\"ispublic\":1,"+
                "\"favorite\":1"+
                "}";
        Gson tgson = new Gson();
        final CreateTripLineData tripLineData = tgson.fromJson(json, CreateTripLineData.class);
        TripLineDataReceiver tripLineDataReceiver = new TripLineDataReceiver();
        TripLine.getTripApiClient().createTripLine(tripLineData, tripLineDataReceiver);
    }

    public String createPath(double[] geotag){
        String path="";
        for(int i = 0; i < geotag.length/2; i++){
            String cid = String.valueOf((int)(Math.random()*10000));
            System.out.println("cid: "+cid);
            imageCids.add(cid);
            String x = String.valueOf(geotag[i*2]);
            String y = String.valueOf(geotag[i*2+1]);
            path+="{ \"cID\":\""+cid+"\",\"store_path\":\"client\", \"location\": {\"x\":"+x+",\"y\":"+y+"} }";
            if(i <= geotag.length/2 - 2){
                path+=",";
            }
        }

        return path;
    }

    public class TripLineDataReceiver implements Callback<TripLineData>{
        String mTriplineID;
        @Override
        public void success(TripLineData tripLineData, Response response) {

            System.out.println("create TripLineData success");
            Gson gson = new Gson();
            String str = gson.toJson(tripLineData);
            mTriplineID = tripLineData.getTriplineID();
            Tripline tripline = new Tripline(mTriplineID);
            TripLineInfo.getInstance().addTripLine(mTriplineID, tripline);

            System.out.println(str);
            uploadTripLinePhoto(imageUrls, mTriplineID);
        }
        @Override
        public void failure(RetrofitError retrofitError) {
            System.out.println("create TripLineData fail");
        }

        public String getTriplineID(){
            return mTriplineID;
        }
    }
}
