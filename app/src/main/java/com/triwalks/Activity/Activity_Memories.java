package com.triwalks.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.TripLineInfo;
import com.triwalks.Common.Tripline;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.R;
import com.triwalks.RestClient.api.TripLine;
import com.triwalks.Service.TripLinePullService;

import java.util.ArrayList;

public class Activity_Memories extends Activity_Navi {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("Memories");
        // Inflate user main page fragment
        Frag_memories fragment = new Frag_memories();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_memories);
        displayFragment(fragment);

       /* MaterialDialog ask = new MaterialDialog.Builder(this)
                .title("MAIN ACTIVITY")
                .content("Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
                .positiveText("Agree")
                .negativeText("Disagree")
                .show();*/
    }

    public static class Frag_memories extends Fragment_CustomMap {
        private ArrayList<Integer> l;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            l = (ArrayList<Integer>)getActivity().getIntent().getSerializableExtra("checkList");
            if(l.size() != 0){
                showTripLine(l.get(0));
                PhotoReceiver mPhotoReceiver = new PhotoReceiver();

                IntentFilter mStatusIntentFilter = new IntentFilter(
                        TripLinePullService.DOWNLOAD_TRIP_LINE_ACTION);

                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                        mPhotoReceiver,
                        mStatusIntentFilter);
            }
            // show corresponding triplines on googl map
            //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(l.get(0)), Toast.LENGTH_SHORT).show();
            return getFragView();
        }

        public void showTripLine(int p){
            Tripline mTripline= TripLineInfo.getInstance().getTripLine(p);
            if(mTripline != null) {

                if (mTripline.isLoaded()) {
                    hasLoaded(mTripline);
                } else {
                    locateMyLocation();
                    double[] coor = getScreenGps();

                    coor[0] = 24.875802259936528;
                    coor[1] = 121.05868689715862;
                    coor[2] = 24.701568455396586;
                    coor[3] = 120.9350910410285;
                    System.out.println(coor[0] + " " + coor[1] + " " + coor[2] + " " + coor[3]);
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    int[] pixel = CommonFunction.getScreenDP(this.getActivity());
                    pixel[1] = (pixel[1] - 173) * (int) displayMetrics.density;
                    pixel[0] = pixel[0] * (int) displayMetrics.density;

                    Intent mServiceIntent = new Intent(getActivity(), TripLinePullService.class);
                    Bundle bundle = new Bundle();
                    bundle.putDoubleArray("coor", coor);
                    bundle.putIntArray("pixel", pixel);
                    bundle.putInt("position", p);
                    mServiceIntent.putExtras(bundle);
                    getActivity().startService(mServiceIntent);
                }
            }
        }

        public void hasLoaded(Tripline mTripline){
//            tripline = TripLineInfo.getInstance().getTripLine(0);
            for(int i = 0; i < mTripline.getSize(); i++) {
                String sid = mTripline.getSid(i);
                double[] position = mTripline.getLocation(sid);
                System.out.println("sid: "+sid+" position: "+position);
                addMarker(mTripline.getBitmap(sid), position[0], position[1]);
            }
        }

        public void addMarker(Bitmap bitmap, double x, double y){
            if(bitmap != null) {
//                System.out.println("add marker Bitmap: " + bitmap + " x: " + x + " y: " + y);
                View markerView =  ((LayoutInflater) getFragView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
                Marker marker = getMap().addMarker(
                        new MarkerOptions()
                                .position(new LatLng(y, x))//former is horizontal
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(
                                        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444)
                                ))
                );
//                marker.setVisible(false);
//                marker.setSnippet(path+"-"+follower);
//                loadBitmap(path, marker, markerView, R.id.photo);
                ((ImageView)markerView.findViewById(R.id.photo)).setImageBitmap(bitmap);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getFragView().getContext(), markerView)));
            }

        }

        public class PhotoReceiver extends BroadcastReceiver {
            Tripline tripline;
            // Called when the BroadcastReceiver gets an Intent it's registered to receive
            @Override
            public void onReceive(Context context, Intent intent) {
                String triplineID = intent.getStringExtra("triplineID");
//            System.out.println("map: "+map+" bitmap: "+bitmap);
                tripline = TripLineInfo.getInstance().getTripLine(triplineID);
                hasLoaded(tripline);
//                for(int i = 0; i < tripline.getSize(); i++) {
//                    String sid = tripline.getSid(i);
//                    double[] position = tripline.getLocation(sid);
//                    System.out.println("sid: "+sid+" position: "+position);
//                    addMarker(tripline.getBitmap(sid), position[0], position[1]);
//                }
            }
        }
    }
}