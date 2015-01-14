package com.triwalks.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.Common.TripLineInfo;
import com.triwalks.Common.Tripline;
import com.triwalks.Common.UserData;
import com.triwalks.RestClient.api.TripLine;
import com.triwalks.RestClient.model.AnalyzeTripInData;
import com.triwalks.RestClient.model.AnalyzeTripOutData;
import com.triwalks.RestClient.model.TripLineData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by james on 1/11/15.
 */
public class TripLinePullService extends IntentService {
    String userid;
    String sessionid;
//    String tripLineID = "54b25a0b586e1f7651ab810c";
    String tripLineID;
    int p = 0;
    List<String> imageSids;
    ArrayList<Bitmap> bitmaps;
    double[] coor;
    int[] pixel;

    Tripline tripline;

    public static final String DOWNLOAD_TRIP_LINE_ACTION = "download trip line";


    public TripLinePullService(){
        super("TripLinePullService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        System.out.println("start service");
        Bundle bundle = workIntent.getExtras();
        coor = bundle.getDoubleArray("coor");
        pixel = bundle.getIntArray("pixel");
        p = bundle.getInt("position");
        tripline = TripLineInfo.getInstance().getTripLine(p);
        if(tripline.isLoaded()){
            System.out.println("tripline has already been loaded");
            return;
        }
        tripLineID = tripline.getTriplineID();

        System.out.println("pull tripline: "+tripline+" triplineID: "+ tripLineID);
//        userid = "54b0ec4c586e1f1870e4fc26";
        userid = UserData.getInstance().getUser_ID();
//        sessionid = "12345";
        sessionid = UserData.getInstance().getSession_ID();
//        imageSids = new ArrayList<>();
        bitmaps = new ArrayList<>();
        Map test =  new HashMap<String, String>();
        test.put("path","");
        TripLineDataReceiver tripLineDataReceiver = new TripLineDataReceiver();
        TripLine.getTripApiClient().getTripInfo(userid, sessionid
            , tripLineID, test, tripLineDataReceiver);




    }

    public Parcelable[] toParcelableArray(){
        Parcelable[] images = new Parcelable[bitmaps.size()];
        for(int i = 0; i < bitmaps.size(); i++){
            images[i] = bitmaps.get(i);
        }
        return images;
    }

    public void onGetTripLineDataSuccess(){
        Map<String, String> map = new HashMap<>();
        map.put("big", "");
        System.out.println("imageSid: "+imageSids);
//        tripline = new Tripline(tripLineID, imageSids.size());

        TripLinePhotoReceiver tripLinePhotoReceiver = new TripLinePhotoReceiver();
        tripLinePhotoReceiver.taskCount = imageSids.size();
        for(int i = 0; i < imageSids.size(); i++){
            TripLine.getTripApiClient().downloadPhoto(userid, sessionid, tripLineID,
                    imageSids.get(i), map, tripLinePhotoReceiver);
        }
    }

    public void onGetPhotoSuccess(){
        getTripTree(userid, sessionid, tripLineID);
    }

    public void getTripTree(String userID ,String sessionID,String triplineid) {
        String json = "{" +
                "\"userid\":\""+userID+"\"," +
                "\"sessionid\":\""+sessionID+"\","+
                "\"triplineid\":\""+triplineid+"\","+
                "\"nlevel\": 15,"+
                "\"windowX\": "+pixel[0]+","+
                "\"windowY\": "+pixel[1]+","+
                "\"picture_pixel\": 100,"+
                "\"GPS_left_up\":{\"x\":"+coor[0]+",\"y\":"+coor[1]+"},"+
                "\"GPS_right_down\":{\"x\":"+coor[2]+",\"y\":"+coor[3]+"}"+
                "}";
        Gson tgson = new Gson();
        AnalyzeTripInData test = tgson.fromJson(json, AnalyzeTripInData.class);
        TripTreeReceiver tripTreeReceiver = new TripTreeReceiver();
        TripLine.getTripApiClient().getTripTree(test, tripTreeReceiver);
    }

    public void createTripLine(){
//        TripLineInfo.getInstance().addTripLine(tripLineID, tripline);
        tripline.setLoaded(true);
        Intent localIntent = new Intent(DOWNLOAD_TRIP_LINE_ACTION);
        Bundle bundle = new Bundle();
//        bundle.putParcelableArray("images", toParcelableArray());
        bundle.putString("triplineID", tripLineID);
        localIntent.putExtras(bundle);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public class TripLineDataReceiver implements Callback<TripLineData> {
        @Override
        public void success(TripLineData tripLineData, Response response) {

            System.out.println("Download TripLineData success");
            Gson gson = new Gson();
            String str = gson.toJson(tripLineData);
            imageSids = tripLineData.getPath();
            System.out.println(str);
            if(imageSids != null) {
                onGetTripLineDataSuccess();
            }
        }
        @Override
        public void failure(RetrofitError retrofitError) {
            System.out.println("create TripLineData fail");
        }

//        public void addImageSid(List<TripLineData.path> path){
//            for(int i = 0; i < path.size(); i++){
//                imageSids.add(path.get(i).getPhotoID());
//            }
//        }

    }

    public class TripLinePhotoReceiver implements Callback<Response>{
        int taskCount;
        int sidCount = 0;
        @Override
        public void success(Response resultData, Response response) {
            taskCount--;
            System.out.println("Download TripLine photo success");
            try{

                Bitmap bitmap = ImageLoader.decodeSampleBitmapFromStream(resultData.getBody().in(), 100, 100);
//                Bitmap bitmap = BitmapFactory.decodeStream(resultData.getBody().in());
                tripline.addBitmap(imageSids.get(sidCount++), bitmap); // It may have wrong order!!
//                bitmaps.add(bitmap);

                System.out.println("decode bitmap: "+bitmap);
                if(taskCount == 0){
                    onGetPhotoSuccess();
                }

            }catch(IOException e){
                e.printStackTrace();
            }

        }
        @Override
        public void failure(RetrofitError retrofitError) {
            taskCount--;
            System.out.println("Download TripLine photo fail");
        }
    }

    public class TripTreeReceiver implements Callback<AnalyzeTripOutData>{
        @Override
        public void success(AnalyzeTripOutData analyzeTripOutData, Response response) {
            System.out.println("Get trip tree success");
            Gson gson = new Gson();
            String str = gson.toJson(analyzeTripOutData);
            System.out.println(str);
            List<AnalyzeTripOutData.PhotoTree> tree = analyzeTripOutData.getPhoto_tree();

            for(int i = 0; i < tree.size(); i++){
                if(tree.get(i).getLevel() == 15){
                    List<AnalyzeTripOutData.PhotoTree.group> groups = tree.get(i).getGrouplist();
                    tripline.setSize(groups.size());
                    for(int j = 0; j < groups.size(); j++){
                        tripline.addSid(groups.get(j).getDeputy());
                        tripline.addLocation(groups.get(j).getDeputy(), groups.get(j).getLocation());

                    }
                    break;
                }
            }

            createTripLine();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            System.out.println("Get trip tree fail");
        }

    }
}
