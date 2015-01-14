package com.triwalks.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.UserData;
import com.triwalks.Fragment.Fragment_Inner;
import com.triwalks.R;
import com.triwalks.RestClient.api.UserProfile;
import com.triwalks.RestClient.model.CreateUserData;
import com.triwalks.RestClient.model.UserProfileData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_Create_User extends Activity_Navi {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle("Join Us");
        // Inflate user main page fragment
        Frag_create_user fragment = new Frag_create_user();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_create_user);
        displayFragment(fragment);
    }

    public static class Frag_create_user extends Fragment_Inner {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            getFragView().findViewById(R.id.join_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText account = (EditText) getFragView().findViewById(R.id.account);
                    EditText passwd = (EditText) getFragView().findViewById(R.id.passwd);
                    //Toast.makeText(getActivity().getApplicationContext(), account.getText() + ":" + passwd.getText(), Toast.LENGTH_SHORT).show();
                    LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    createUser(
                            account.getText().toString(),
                            passwd.getText().toString(),
                            account.getText().toString(),
                            CommonFunction.getLocationProvider(mLocationManager)
                    );
                }
            });

            getFragView().findViewById(R.id.cancel_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            // set country from devices configuration
            ((AutoCompleteTextView)getFragView().findViewById(R.id.country)).setAdapter(
                    new ArrayAdapter(getActivity(), R.layout.autocomplete_country, getLocale())
            );

            return getFragView();
        }

        private void createUser(String account, String passwd, final String name, Location location) {
            String json = "{" +
                    "\"account\":\""+account+"\"," +
                    "\"passwd\":\""+passwd+"\","+
                    "\"location\": {\"x\":"+location.getLatitude()+",\"y\":"+location.getLongitude()+"},"+
                    "\"name\":\""+name+"\""+
                    "}";
            Gson tgson = new Gson();
            CreateUserData test = tgson.fromJson(json, CreateUserData.class);

            UserProfile.getUserApiClient().createUser(test, new Callback<UserProfileData>() {
                @Override
                public void success(UserProfileData userProfileData, Response response) {
                    Toast.makeText(getFragView().getContext(), "WELCOME "+name.toUpperCase(), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // error control?
                    Toast.makeText(getFragView().getContext(), "OOPS, "+name.toUpperCase()+" EXISTED", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResume(){
            super.onResume();
        }

        private void showCountriesList() {
            new MaterialDialog.Builder(getActivity())
                    .title("Now live at?")
                    .items(R.array.country)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .positiveText("Choose")
                    .show();
        }
    }
}