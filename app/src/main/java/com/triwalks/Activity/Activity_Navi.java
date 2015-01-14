package com.triwalks.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.triwalks.Common.CommonFunction;
import com.triwalks.R;
import com.triwalks.RestClient.api.UserProfile;
import com.triwalks.RestClient.model.ResultData;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Activity_Navi extends ActionBarActivity {
    private Toolbar toolbar = null;
    protected String sessionID = null;
    protected String userID = null;
    protected DrawerLayout drawerLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activtiy_view);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitleTextColor(getResources().getColor(R.color.text_secondary));

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.text_secondary), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary));

        final View drawer_View = this.findViewById(R.id.drawer_view);

        // navi drawer
        drawer_View.findViewById(R.id.navi_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawerLayout.closeDrawers();
            startActivity(
                    CommonFunction.gotoIntent(getApplicationContext(), Activity_User_Profile.class)
            );
            }
        });

        drawer_View.findViewById(R.id.navi_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawerLayout.closeDrawers();
            startActivity(
                    CommonFunction.gotoIntent(getApplicationContext(), Activity_User_Profile.class)
            );
            }
        });

        drawer_View.findViewById(R.id.navi_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawerLayout.closeDrawers();
            startActivity(
                    CommonFunction.gotoIntent(getApplicationContext(), Activity_User_Profile.class)
            );
            }
        });

        drawer_View.findViewById(R.id.navi_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showLogoutDialog();
            }
        });
    }

    public String getSessionID(){
        return this.sessionID;
    }

    public String getUserID(){
        return this.userID;
    }

    private void showLogoutDialog(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("Do you really want to logout?")
                .positiveText("Yes")
                .negativeText("Not yet")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        UserProfile.getUserApiClient().logout(getUserID(), getSessionID(), new Callback<ResultData>() {
                            @Override
                            public void success(ResultData resultData, Response response) {
                                deleteFile(getResources().getText(R.string.usf).toString());
                                Toast.makeText(getApplicationContext(), "SEE YOU SOON", Toast.LENGTH_SHORT).show();
                                startActivity(
                                        CommonFunction.gotoIntent(getApplicationContext(), Activity_Login.class)
                                );
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                System.out.println("Error");
                            }
                        });
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        dialog.show();
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public void setToolbarTitle(String title){
        this.getSupportActionBar().setTitle(title);
    }

    public void displayFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Only work when theme is not NoActionBar
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // call navigation up. OFFICIAL RECOMMENDATION
                // NavUtils.navigateUpFromSameTask(this);
                // however, finish() is more user friendly due to the display transition is more smooth
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}