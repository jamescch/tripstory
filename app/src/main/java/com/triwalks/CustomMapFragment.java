package com.triwalks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class CustomMapFragment extends InnerFragment {
    private Bundle mapViewSavedInstanceState;
    private Bundle mapViewSaveState;
    private MapView mMapView;
    private GoogleMap mMap;

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
                startActivity(intent);
            }
        });
        return getFragView();
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
}