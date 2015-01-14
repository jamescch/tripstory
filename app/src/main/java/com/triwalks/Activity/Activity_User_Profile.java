package com.triwalks.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.Lib.ImageLoader;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.R;
import com.triwalks.RestClient.api.UserProfile;
import com.triwalks.RestClient.model.ResultData;

import java.io.File;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class Activity_User_Profile extends Activity_Navi {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if now is in user page inflate personal user page
        // if now visiting others user page inflate client user page

        setToolbarTitle("TA CHING CHEN");
        // Inflate user main page fragment
        Frag_User_Profile fragment = new Frag_User_Profile();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_user_profile);
        fragment.init(getToolbar());
        displayFragment(fragment);
    }

    public static class Frag_User_Profile extends Fragment_CustomMap implements GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener  {
        private float currentZoom = -1;
        private Toolbar toolbar;
        private ImageView userAvatar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            getFragView().findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog();
                }
            });

            getFragView().findViewById(R.id.camera_roll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent();
                    intent.setClass(getActivity(), Activity_RollList.class);
                    startActivity(intent);*/
                    startActivity(
                            CommonFunction.gotoIntent(getActivity(), Activity_RollList.class)
                    );
                }
            });

            userAvatar = (ImageView) getFragView().findViewById(R.id.avatar);
//            userAvatar.setImageBitmap(Activity_Crop.getAvatar());
            userAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*startActivity(
                            CommonFunction.gotoIntent(getActivity(), Activity_Crop.class)
                    );*/
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Activity_Crop.class);
                    startActivityForResult(intent, 2);
                }
            });

            updateMapMarker(10);
            getMap().setOnMarkerClickListener(this);
            //getMap().setOnCameraChangeListener(this);
            locateMyLocation();
            System.out.println(screenCoor[0]+" "+screenCoor[1]+" "+screenCoor[2]+" "+screenCoor[3]);

            return getFragView();
        }

        public void init(Toolbar t){
            toolbar = t;
        }

        private void updateMapMarker(int parameter){
            Random dice = new Random();
            for(int i=0; i<50; i++){
                getMap().addMarker( new MarkerOptions()
                                .position(new LatLng(25.033611+dice.nextInt(20), 121.565000+dice.nextInt(20)))
                                .title("台北101")
                                .snippet(Integer.toString(i%2))
                                .draggable(true)
                                .visible(true)
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                );
            }
        }

        // implement Marker Click listener
        @Override
        public boolean onMarkerClick(Marker marker) {
            String triplineID = marker.getSnippet();
            startActivity(
                    CommonFunction.gotoIntent_WithString(getActivity(), Activity_Tripline_info.class, "triplineID", triplineID)
            );
            return true;
        }

        EditText nameInput;
        View positiveAction;

        private void showEditDialog() {
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.edit_profile)
                    .customView(R.layout.dialog_personal_info_edit, false)
                    .positiveText(R.string.done)
                    .negativeText(R.string.cancel)
                    .callback(new MaterialDialog.Callback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            // through static function to get toolbar
                            toolbar.setTitle(nameInput.getText().toString());
                            // call REST API here to update user information
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                        }
                    }).build();

            positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
            nameInput = (EditText) dialog.getCustomView().findViewById(R.id.profile_name);
            nameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    positiveAction.setEnabled(s.toString().trim().length() > 0);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            ((AutoCompleteTextView) dialog.getCustomView().findViewById(R.id.profile_address)).setAdapter(
                    new ArrayAdapter(getActivity(), R.layout.autocomplete_country, getLocale())
            );

            dialog.show();
            positiveAction.setEnabled(false); // disabled by default
        }

        // handle Zoom In event
        @Override
        public void onCameraChange(CameraPosition pos) {
            if (pos.zoom != currentZoom){
                if(currentZoom > pos.zoom) // ZOOM OUT
                    ;
                else // ZOOM IN
                    ;
                currentZoom = pos.zoom;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            if (requestCode == 2) {
                if(resultCode == RESULT_OK){
                    userAvatar.setImageBitmap(Activity_Crop.getAvatar());
                    SaveFileToExternalStorage saver = new SaveFileToExternalStorage();
                    saver.execute(Activity_Crop.getAvatar());
                }
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private class SaveFileToExternalStorage extends AsyncTask<Bitmap, Void, Void>{
            @Override
            protected Void doInBackground(Bitmap... param) {
                CommonFunction.saveImageToExternalStorage(getFragView().getContext(), Activity_Crop.TARGET_PATH, Activity_Crop.AVATAR_FILE_NAME, param[0]);
                String[] ID = CommonFunction.readFromFile(getFragView().getContext(), getResources().getText(R.string.usf).toString()).split(",");
                File file = new File(Activity_Crop.AVATAR_FILE_PATH);
                UserProfile.getUserApiClient().uploadavator(new TypedFile("image/jpg", file), new TypedString(ID[0]), new TypedString(ID[1]), new TypedString("1"), new Callback<ResultData>() {
                    @Override
                    public void success(ResultData resultData, Response response) {
                        System.out.println("success");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        System.out.println("failure");
                    }
                });
                return null;
            }
        }
    }
}
