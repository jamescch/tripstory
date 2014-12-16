package com.triwalks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import lib.image.ImageLoader;

public class CustomMapFragment extends InnerFragment {
    private Bundle mapViewSavedInstanceState;
    private Bundle mapViewSaveState;
    private MapView mMapView;
    private GoogleMap mMap;

    public final static int SELECT_PHOTOS_ACTIVITY = 0;

    // VERY SPECIAL HACK FOR EXCEPTION (android.os.BadParcelableException) WHEN SCREEN ROTATION
    // https://code.google.com/p/gmaps-api-issues/issues/detail?id=6237#c9
    // STILL HAVE SOME NULL POINTER ERROR
    // BUT NOW CAN ROTATE
    // WTF!?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) getFragView().findViewById(R.id.map);
        if(mapViewSavedInstanceState!=null)
            mMapView.onCreate(mapViewSavedInstanceState);
        else
            mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMap = mMapView.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);

        getFragView().findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SecondActivity.class);
                startActivityForResult(intent, SELECT_PHOTOS_ACTIVITY);
            }
        });
        return getFragView();
    }

    public MapView getMapView(){
        return this.mMapView;
    }

    public GoogleMap getMap(){
        return this.mMap;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //This MUST be done before saving any of your own or your base class's variables
        mapViewSaveState = new Bundle(outState);
        mMapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        //Add any other variables here.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (CustomMapFragment.SELECT_PHOTOS_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    // Extract the data returned from the child Activity.
                    ArrayList<String> imageUrls = (ArrayList<String>)data.getExtras().get("imageUrls");

                    for(String path : imageUrls) {
                        System.out.println("path: " + path);
                        addPoi(path);
                    }
                }
                break;
            }
        }
    }

    public void addPoi(String imagePath){
//        String imagePath = imageUrls.get(0);
        float[] geoTag;
        Bitmap marker_background = BitmapFactory.decodeResource(getResources(), R.drawable.custom_marker);
        Bitmap marker_photo = ImageLoader.decodeSampledBitmapFromPath(imagePath, 100, 100);

        Bitmap marker = overlay(marker_background, marker_photo);
        geoTag = getGeoTag(imagePath);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(geoTag[0], geoTag[1]))//former is horizontal
                .title("Hello world")
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(marker)));
    }

    public float[] getGeoTag(String path) {
        //Get geo tag using ExifInterface class
        try {
            ExifInterface imgInfo = new ExifInterface(path);
            float[] geoTag = new float[2];
            imgInfo.getLatLong(geoTag);
            System.out.println("Latitude: " + geoTag[0] + " Longitude: "+geoTag[1]);
            return geoTag;
        }catch(Exception e){
            System.out.println("Exception: Exifinterface fails");
            return null;
        }
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        Matrix matrix = new Matrix(); // matrix used to transform bmp2
        float scale = (float)136/(float)bmp2.getWidth(); // 135 is the size of the content
        matrix.postScale(scale, scale);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.setMatrix(matrix);
        // 20 is the width of the frame
        // transformed by the current matrix, including bitmap and x y
        canvas.drawBitmap(bmp2, 22/scale, 18/scale, null);
        return bmOverlay;
    }

}