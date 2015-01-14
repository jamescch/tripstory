package com.triwalks.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.triwalks.Common.CommonFunction;
import com.triwalks.Common.UserData;
import com.triwalks.Fragment.Fragment_Inner;
import com.triwalks.R;
import com.triwalks.RestClient.api.UserProfile;
import com.triwalks.RestClient.model.UserProfileData;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Activity_Login extends Activity_Navi {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String userToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createNaviToogle();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        userToken = CommonFunction.readFromFile(getApplicationContext(), getResources().getText(R.string.usf).toString());
        boolean loginSuc;
        if(userToken != null){
            // check session is right
            final String[] ID = userToken.split(",");
            Map<String, String> select = new HashMap<String, String>();
            UserProfile.getUserApiClient().getUserInfo(ID[0], ID[1], select, new Callback<UserProfileData>() {
                @Override
                public void success(UserProfileData userProfileData, Response response) {
                    if (!userProfileData.getResult()) {
                        userToken = null;
                        return;
                    }
//                    Gson gson = new Gson();
//                    String str = gson.toJson(userProfileData);
//                    System.out.println(str);
                    startActivity(
                            CommonFunction.gotoIntent_WithString(getApplicationContext(), Activity_Main.class, "account", userProfileData.getAccount().toUpperCase())
                    );
                    finish();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    deleteFile(getResources().getText(R.string.usf).toString());
                    Toast.makeText(getApplicationContext(), "OOPS, NO SUCH USER", Toast.LENGTH_SHORT).show();
                    userToken = null;
                    return;
                }
            });
        }

        if(userToken == null) {
            setToolbarTitle("Share ur world");
            // Inflate user main page fragment
            Frag_create_user fragment = new Frag_create_user();
            // must call setFragLayout right after the initialization
            fragment.setFragLayout(R.layout.frag_layout_login);
            displayFragment(fragment);
        }
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

    public static class Frag_create_user extends Fragment_Inner {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            getFragView().findViewById(R.id.login_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText account = (EditText) getFragView().findViewById(R.id.account);
                    EditText passwd = (EditText) getFragView().findViewById(R.id.passwd);
                    UserProfile.getUserApiClient().login(account.getText().toString(), passwd.getText().toString() , new Callback<UserProfileData>() {
                        @Override
                        public void success(UserProfileData userProfileData, Response response) {
                            Gson gson = new Gson();
                            String str = gson.toJson(userProfileData);
                            System.out.println(str);
                            UserData.getInstance().setUserID(userProfileData.getUserid());
                            System.out.println("user profile"+userProfileData.getUserid());
                            UserData.getInstance().setSessionID(userProfileData.getSessionid());
                            CommonFunction.writeToFile(getFragView().getContext(), userProfileData.getUserid() + "," + userProfileData.getSessionid(), getResources().getText(R.string.usf).toString());
                            Map<String, String> select = new HashMap<String, String>();

                            UserProfile.getUserApiClient().getUserInfo(userProfileData.getUserid(), userProfileData.getSessionid(), select, new Callback<UserProfileData>() {
                                @Override
                                public void success(UserProfileData userProfileData, Response response) {
                                    Toast.makeText(getFragView().getContext(), "WELCOME BACK ", Toast.LENGTH_SHORT).show();
                                    startActivity(
                                            CommonFunction.gotoIntent_WithString(getFragView().getContext(), Activity_Main.class, "account", userProfileData.getAccount().toUpperCase())
                                    );
                                    getActivity().finish();
                                }

                                @Override
                                public void failure(RetrofitError retrofitError) {
                                    Toast.makeText(getFragView().getContext(), "OOPS, NO SUCH USER", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        @Override
                        public void failure(RetrofitError retrofitError) {
                            System.out.println("Error");
                        }
                    });
                }
            });

            getFragView().findViewById(R.id.cancel_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            getFragView().findViewById(R.id.no_account).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunction.gotoIntent(getActivity(), Activity_Create_User.class);
                }
            });

            return getFragView();
        }
    }
}