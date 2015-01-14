package com.triwalks.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Fragment_CustomMap extends Fragment_Inner implements OnMapReadyCallback {
    private ArrayList<ArrayList<Marker>> markers;
    private Bundle mapViewSavedInstanceState;
    private Bundle mapViewSaveState;
    private MapView mMapView;
    private GoogleMap mMap;
    private DisplayMetrics displayMetrics;
    protected static double[] screenCoor = new double[4];

    // VERY SPECIAL HACK FOR EXCEPTION (android.os.BadParcelableException) WHEN SCREEN ROTATION
    // https://code.google.com/p/gmaps-api-issues/issues/detail?id=6237#c9
    // STILL HAVE SOME NULL POINTER ERROR
    // BUT NOW CAN ROTATE
    // WTF!?

    @Override
    public void onMapReady(GoogleMap map) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        displayMetrics = getFragView().getResources().getDisplayMetrics();
        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) getFragView().findViewById(R.id.map);
        if (mapViewSavedInstanceState != null)
            mMapView.onCreate(mapViewSavedInstanceState);
        else
            mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        mMap = mMapView.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            private int currentZoom = -1;
            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom) {
                    //updateMarker(currentZoom, pos.zoom);
                    if (markers != null) {
                        int offset = (pos.zoom - Math.round(pos.zoom) > 0.5) ? 1 : 0;
                        int zoomLevel = Math.round(pos.zoom + offset) > 17 ? 17 : Math.round(pos.zoom + offset);
                        updateMapCamera(getScreenLocation(), zoomLevel);
                        showMarkerAtZoom(currentZoom, zoomLevel);
                        currentZoom = zoomLevel;
                    }
                }
                screenCoor = getScreenGps();
            }
        });
        return getFragView();
    }

    public void locateMyLocation(){
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location l = CommonFunction.getLocationProvider(mLocationManager);
        if (l != null)
            updateMapCamera(l, 2);
    }

    public void addMarkerAtZoom(Marker marker, int zoomLevel){
        if(markers == null) {
            markers = new ArrayList<ArrayList<Marker>>();
            for(int i=0; i<20; i++){
                markers.add(new ArrayList<Marker>());
            }
        }
        markers.get(zoomLevel).add(marker);
    }

    public ArrayList<ArrayList<Marker>> getMarkers(){
        return markers;
    }

    public void showMarkerAtZoom(int preZoom, int newZoom){
        if(preZoom < 0)
            return;
        for (int i = 0; i < markers.get(preZoom).size(); i++) {
            markers.get(preZoom).get(i).setVisible(false);
        }
        for(int i=0; i<markers.get(newZoom).size(); i++){
            markers.get(newZoom).get(i).setVisible(true);
        }
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

    public Location getScreenLocation(){
        Location location = new Location("zoom");
        location.setLatitude(mMap.getCameraPosition().target.latitude);
        location.setLongitude(mMap.getCameraPosition().target.longitude);
        return location;
    }

    public double[] getScreenGps() {
        double[] coor = new double[4];
        Projection proj = mMap.getProjection();
        VisibleRegion vRegion = proj.getVisibleRegion();
        coor[0] = vRegion.latLngBounds.northeast.latitude;
        coor[1] = vRegion.latLngBounds.northeast.longitude;
        coor[2] = vRegion.latLngBounds.southwest.latitude;
        coor[3] = vRegion.latLngBounds.southwest.longitude;
        //System.out.println(vRegion.latLngBounds.northeast.latitude+" "+coor[1]+" "+coor[2]+" "+coor[3]);
        return coor;
    }

    // convert gps to decimal
    public static double gpsToDecimal(String s){
        double gps = 0.0;
        double[] offset = {1.0, 60.0, 3600.0};
        String[] latitude = s.split(",");

        for(int i=0; i<latitude.length; i++){
            String[] temp = latitude[i].split("/");
            gps += Double.parseDouble(temp[0])/(Double.parseDouble(temp[1])*offset[i]);
        }
        return gps;
    }

    protected void updateMapCamera(Location location, float zoomLevel){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    public void loadBitmap(String path, Marker marker, View v, int resourceID) {
        BitmapWorkerTask task = new BitmapWorkerTask(marker, v, resourceID);
        task.execute(path);
        //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private Marker marker;
        private View view;
        private int resourceID;

        public BitmapWorkerTask(Marker marker, View v, int resID) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.resourceID = resID;
            this.view = v;
            this.marker = marker;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageLoader.decodeTripLineBitmapFromPath(params[0]);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ((ImageView)view.findViewById(resourceID)).setImageBitmap(bitmap);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getFragView().getContext(), view)));
            }
        }
    }

    public Bitmap createDrawableFromView(Context context, View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        //view.buildDrawingCache(); -> make everything slow
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public double[] getCenterPoint(double[][] geotag) {
        quicksort(geotag, 0, geotag.length-1);
        double[] center = new double[2];
        center[0] = geotag[geotag.length/2][0];
        center[1] = geotag[geotag.length/2][1];
        return center;
    }

    public void quicksort(double[][] A, int lo, int hi) {
        if (lo < hi) {
            int p = partition(A, lo, hi);
            quicksort(A, lo, p - 1);
            quicksort(A, p + 1, hi);
        }
    }

    public int partition(double[][] A, int lo, int hi) {
        int pivotIndex = choosePivot(lo, hi);
        double pivotValue = A[pivotIndex][1];
        // put the chosen pivot at A[hi]
        swap(A, pivotIndex, hi);
        int storeIndex = lo;
        // Compare remaining array elements against pivotValue = A[hi]
        for (int i = lo; i < hi; i++) {
            if (A[i][1] < pivotValue) {
                swap(A, i, storeIndex);
                storeIndex = storeIndex + 1;
            }
        }
        swap(A, storeIndex, hi); // Move pivot to its final place
        return storeIndex;
    }

    public int choosePivot(int lo, int hi){
        return (int)(Math.random() * (hi-lo+1)) + lo;
    }

    public void swap(double[][] a, int x, int y){
        double temp;
        temp = a[x][1];
        a[x][1] = a[y][1];
        a[y][1] = temp;
    }
}