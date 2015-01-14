package com.triwalks.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.CustomTextWatcher;
import com.triwalks.Common.UserData;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.R;
import com.triwalks.Service.TripLineInitService;

public class Activity_Main extends Activity_Navi {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createNaviToogle();

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("account");

        setToolbarTitle("HI "+name);
        // Inflate user main page fragment
        Frag_user_main fragment = new Frag_user_main();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_user_main);
        displayFragment(fragment);
    }

    private void createNaviToogle(){
        // the code following will automatically create options animation
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        this.mDrawerLayout.setScrimColor(getResources().getColor(R.color.navdrawer_fading_background));
        this.mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        // 實作 drawer toggle 並放入 toolbar
        this.mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, this.getToolbar(), R.string.drawer_open, R.string.drawer_close);
        this.mDrawerToggle.syncState();
        this.mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static class Frag_user_main extends Fragment_CustomMap {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // FAB
            getFragView().findViewById(R.id.fab_favorite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(
                            CommonFunction.gotoIntent(getActivity(), Activity_User_Profile.class)
                    );
                }
            });

            getFragView().findViewById(R.id.fab_add_single_trip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CommonFunction.gotoIntent(getActivity(), Activity_Edit_tripline_info.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("title", "Choose Photo");
                    startActivity(
                            CommonFunction.gotoIntent_WithObject(getFragView().getContext(), Activity_Gallery_Single.class, bundle)
                    );

                    /*Intent intent = new Intent();
                    intent.setClass(getActivity(), Activity_Gallery_Single.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);*/

                    /*Bundle bundle = new Bundle();
                    bundle.putString("title", "Choose Avator Photo");
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Activity_Gallery_Single.class);
                    // onActivityResult will not working if setFlags
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);*/
                }
            });

            getFragView().findViewById(R.id.fab_add_trip_line).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(
                            CommonFunction.gotoIntent(getActivity(), Activity_Gallery_Tripline.class)
                    );
                }
            });
            locateMyLocation();

            getMap().addMarker(
                    new MarkerOptions()
                            .position(new LatLng(0, 0))//former is horizontal
            );

            getMap().addMarker(
                    new MarkerOptions()
                            .position(new LatLng(10, 10))//former is horizontal
            );

            getMap().addPolyline((new PolylineOptions())
                    .add(
                            new LatLng(0, 0),
                            new LatLng(10, 10)
                    ).width(5).color(Color.BLUE)
                    .geodesic(true));

            initUserData();

            return getFragView();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            if (requestCode == 1) {
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Integer imgUrl = bundle.getInt("imageUrl");
                }
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        public void initUserData(){
            Intent serviceIntent = new Intent(this.getActivity(), TripLineInitService.class);
            Bundle bundle = new Bundle();
            bundle.putString("userID", UserData.getInstance().getUser_ID());
            bundle.putString("sessionID", UserData.getInstance().getSession_ID());
            serviceIntent.putExtras(bundle);
            this.getActivity().startService(serviceIntent);

        }
    }
}