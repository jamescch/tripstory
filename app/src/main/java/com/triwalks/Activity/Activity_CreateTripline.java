package com.triwalks.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.Gson.ExifExtractor;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.R;
import com.triwalks.RestClient.api.TripLine;
import com.triwalks.RestClient.model.AnalyzePhotoInData;
import com.triwalks.RestClient.model.AnalyzePhotoOutData;
import com.triwalks.Service.PhotoPushService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_CreateTripline extends Activity_Navi {
    private static boolean enableBackButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get triplin info through tripline id
        setToolbarTitle("Edit Tripline");
        Frag_CreateTripline fragment = new Frag_CreateTripline();
        fragment.setFragLayout(R.layout.frag_layout_create_tripline);

        displayFragment(fragment);
    }

    public static void toogleBackEvent(){
        enableBackButton = enableBackButton?false:true;
    }

    @Override
    public void onBackPressed() {
        if(enableBackButton)
            super.onBackPressed();
    }

    public static class Frag_CreateTripline extends Fragment_CustomMap {
        private ArrayList<ExifExtractor> mImageUrls;
        private CardView mCardView;
        private ImageView cardViewPhoto;
        private FloatingActionButton btn_done;
        private RelativeLayout fab_toogle;
        private String[] ID;
        private AnalyzePhotoOutData tmpGrouping = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            mCardView = (CardView)getFragView().findViewById(R.id.card_view);
            fab_toogle = (RelativeLayout)getFragView().findViewById(R.id.fab_toogle);
            cardViewPhoto = (ImageView)getFragView().findViewById(R.id.photo);
            btn_done = (FloatingActionButton)getFragView().findViewById(R.id.done);

            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCardView.setVisibility(View.INVISIBLE);
                    fab_toogle.setVisibility(View.INVISIBLE);
                    Activity_CreateTripline.toogleBackEvent();
                }
            });

            ArrayList<Integer> checkItems = (ArrayList<Integer>)getActivity().getIntent().getExtras().get("imageUrls");
            mImageUrls = new ArrayList<ExifExtractor>();

            for(int i=0; i<checkItems.size(); i++){
                mImageUrls.add(getExifList().get(checkItems.get(i)));
            }

            locateMyLocation();
            ID = CommonFunction.readFromFile(getFragView().getContext(), getResources().getText(R.string.usf).toString()).split(",");
            //analyzePhotoTree(ID[0], ID[1], mImageUrls);


            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int[] pixel = CommonFunction.getScreenDP(this.getActivity());
            pixel[0] = pixel[0];//*(int)displayMetrics.density;
            pixel[1] = (pixel[1]-40);//*(int)displayMetrics.density;
            int markerSize = 60;
            System.out.println(screenCoor[0]+" "+screenCoor[1]+" "+screenCoor[2]+" "+screenCoor[3]);

            String photoArray = "";

            for(int i=0; i<mImageUrls.size(); i++){
                photoArray += "{ \"cID\":\""+mImageUrls.get(i).getPath()+"\", \"location\":{ \"x\":"+mImageUrls.get(i).getLat()+",\"y\":"+mImageUrls.get(i).getLong()+" }},";
            }
            photoArray = photoArray.substring(0, photoArray.length()-1);

            String json = "{" +
                    "\"userid\":\""+ID[0]+"\"," +
                    "\"sessionid\":\""+ID[1]+"\","+
                    "\"nlevel\": 14,"+
                    "\"windowX\": "+pixel[0]+","+
                    "\"windowY\": "+pixel[1]+","+
                    "\"picture_pixel\": "+markerSize+","+
                    "\"GPS_left_up\":{\"x\":"+screenCoor[0]+",\"y\":"+screenCoor[3]+"},"+
                    "\"GPS_right_down\":{\"x\":"+screenCoor[2]+",\"y\":"+screenCoor[1]+"},"+
                    "\"info_list\": [" +photoArray+
                    "]"+
                    "}";
            System.out.println(json);
            Gson tgson = new Gson();
            AnalyzePhotoInData test = tgson.fromJson(json, AnalyzePhotoInData.class);
            TripLine.getTripApiClient().analyzePhotoTree(test, new Callback<AnalyzePhotoOutData>() {
                @Override
                public void success(AnalyzePhotoOutData analyzePhotoOutData, Response response) {
                    System.out.println("Success");
                    Gson gson = new Gson();
                    String str = gson.toJson(analyzePhotoOutData);
                    System.out.println(str);
                    tmpGrouping = analyzePhotoOutData;
                    List<AnalyzePhotoOutData.PhotoTree> photoTrees = tmpGrouping.getPhoto_tree();
                    for(int i=0; i<photoTrees.size(); i++){
                        List<AnalyzePhotoOutData.PhotoTree.group> group = photoTrees.get(i).getGrouplist();
                        for(int j=0; j<group.size(); j++){
                            String follower = "";
                            for(int k=0; k<group.get(j).getPhotolist().size(); k++) {
                                follower += group.get(j).getPhotolist().get(k)+",";
                            }
                            follower = follower.substring(0, follower.length()-1);
                            addMarkerAtZoom(
                                    addGroupMarker(group.get(j).getDeputy(), follower, group.get(j).getLocation().getX(), group.get(j).getLocation().getY()),
                                    photoTrees.get(i).getLevel()
                            );
                        }
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println("Error");
                }
            });

            /*for(int i=0; i<mImageUrls.size(); i+=2){
                for(int k=0; k<2; k++){
                    try {
                        addMarkerAtZoom(addMarker(mImageUrls.get(i)), (int)(Math.random()*10)+7);
                    }
                    catch (IndexOutOfBoundsException e){
                        ;
                    }
                }
            }*/

           /* double[][] geotag = new double[mImageUrls.size()][2];
            for(int i = 0; i < mImageUrls.size(); i++){
                geotag[i] = new double[] {mImageUrls.get(i).getLat(), mImageUrls.get(i).getLong()};
            }

            double[] centerPoint = getCenterPoint(geotag);
            Location location = new Location("CurrentPosi");
            location.setLatitude(centerPoint[0]);
            location.setLongitude(centerPoint[1]);
            updateMapCamera(location, 10);
            showMarkerAtZoom(0, 10);*/

            getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String deputy = marker.getSnippet().split("-")[0];
                    String[] follower = marker.getSnippet().split("-")[1].split(",");
                    if(follower.length == 1) {
                        cardViewPhoto.setImageBitmap(ImageLoader.decodeTripLineBitmapFromPath(deputy));
                        mCardView.setVisibility(View.VISIBLE);
                        fab_toogle.setVisibility(View.VISIBLE);
                        Activity_CreateTripline.toogleBackEvent();
                    }
                    else{
                        ;
                    }
                    return false;
                }
            });


            pushTripLine();
            return getFragView();
        }

        public Marker addMarker(final ExifExtractor e){
            View markerView =  ((LayoutInflater) getFragView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
            Marker marker = getMap().addMarker(
                    new MarkerOptions()
                            .position(new LatLng(e.getLat(), e.getLong()))//former is horizontal
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444)
                        ))
                );
            System.out.println(e.getLat()+", "+e.getLong());
            marker.setVisible(false);
            loadBitmap(e.getPath(), marker, markerView, R.id.photo);
            return marker;
        }

        public Marker addGroupMarker(String path, String follower, double Lat, double Long){
            View markerView =  ((LayoutInflater) getFragView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
            Marker marker = getMap().addMarker(
                    new MarkerOptions()
                            .position(new LatLng(Lat, Long))//former is horizontal
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444)
                            ))
            );
            marker.setVisible(false);
            marker.setSnippet(path+"-"+follower);
            loadBitmap(path, marker, markerView, R.id.photo);
            return marker;
        }

        private void analyzePhotoTree(String userID, String sessionID, ArrayList<ExifExtractor> mImageUrls) {

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int[] pixel = CommonFunction.getScreenDP(this.getActivity());
            pixel[0] = pixel[0]*(int)displayMetrics.density;
            pixel[1] = (pixel[1]-173)*(int)displayMetrics.density;
            System.out.println(screenCoor[0]+" "+screenCoor[1]+" "+screenCoor[2]+" "+screenCoor[3]);

            String photoArray = "";

            for(int i=0; i<mImageUrls.size(); i++){
                photoArray += "{ \"cID\":\""+mImageUrls.get(i).getPath()+"\", \"location\":{ \"x\":"+mImageUrls.get(i).getLat()+",\"y\":"+mImageUrls.get(i).getLong()+" }},";
            }
            photoArray = photoArray.substring(0, photoArray.length()-1);

            String json = "{" +
                    "\"userid\":\""+userID+"\"," +
                    "\"sessionid\":\""+sessionID+"\","+
                    "\"nlevel\": 15,"+
                    "\"windowX\": "+pixel[0]+","+
                    "\"windowY\": "+pixel[1]+","+
                    "\"picture_pixel\": 80,"+
                    "\"GPS_left_up\":{\"x\":"+screenCoor[0]+",\"y\":"+screenCoor[2]+"},"+
                    "\"GPS_right_down\":{\"x\":"+screenCoor[1]+",\"y\":"+screenCoor[3]+"},"+
                    "\"info_list\": [" +photoArray+
                    "]"+
                    "}";
            System.out.println(json);
            Gson tgson = new Gson();
            AnalyzePhotoInData test = tgson.fromJson(json, AnalyzePhotoInData.class);
            TripLine.getTripApiClient().analyzePhotoTree(test, new Callback<AnalyzePhotoOutData>() {
                @Override
                public void success(AnalyzePhotoOutData analyzePhotoOutData, Response response) {
                    System.out.println("Success");
                    Gson gson = new Gson();
                    String str = gson.toJson(analyzePhotoOutData);
                    System.out.println(str);
                    tmpGrouping = analyzePhotoOutData;
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println("Error");
                }
            });
        }

        public void pushTripLine(){
            Intent intentService = new Intent(this.getActivity(), PhotoPushService.class);
            intentService.setData(Uri.parse("test"));
            Bundle bundle = new Bundle();
            double geotags[] = new double[mImageUrls.size()*2];
            for(int i = 0; i < mImageUrls.size(); i++){
                geotags[i*2] = mImageUrls.get(i).getLong();
                geotags[i*2+1] = mImageUrls.get(i).getLat();
            }
            bundle.putDoubleArray("geotag", geotags);

            ArrayList<String> imageUrls = new ArrayList<>();
            for(int i = 0; i < mImageUrls.size(); i++){
                imageUrls.add(mImageUrls.get(i).getPath());
            }
            bundle.putStringArrayList("imageUrls", imageUrls);
            intentService.putExtras(bundle);

            this.getActivity().startService(intentService);

        }
    }
}
