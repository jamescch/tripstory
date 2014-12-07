package com.triwalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

public class MainActivity extends NaviActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the code following will automatically create options animation
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.navdrawer_fading_background));
        mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, this.getToolbar(), R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setToolbarTitle("WELCOME");

        // Inflate user main page fragment
        Frag_user_main fragment = new Frag_user_main();
        // must call setFragLayout right after the initialization
        fragment.setFragLayout(R.layout.frag_layout_user_main);
        displayFragment(fragment);

       /* MaterialDialog ask = new MaterialDialog.Builder(this)
                .title("MAIN ACTIVITY")
                .content("Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
                .positiveText("Agree")
                .negativeText("Disagree")
                .show();*/
    }

    public static class Frag_user_main extends InnerFragment {
        @Override
        public void onCreate(){
            View v = getFragView();
            // get widget or object from fragment view
            v.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SecondActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}