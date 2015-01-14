package com.triwalks.Activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.triwalks.Fragment.Fragment_CustomMap;
import com.triwalks.Fragment.Fragment_Inner;
import com.triwalks.R;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Activity_Tripline_info extends Activity_Navi {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String triplineID = this.getIntent().getStringExtra("triplineID");

        Toast.makeText(getApplicationContext(), triplineID, Toast.LENGTH_SHORT).show();

        // get triplin info through tripline id
        setToolbarTitle("TriplineInfo");

        // Inflate user main page fragment
        Frag_user_info fragment = new Frag_user_info();

        // Must set
        if(triplineID.equals("0"))
            fragment.setFragLayout(R.layout.frag_layout_tripline_info);
        else
            fragment.setFragLayout(R.layout.frag_layout_single_tripline_info);
        displayFragment(fragment);
    }

    public static class Frag_user_info extends Fragment_CustomMap {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            // initialize
            getFragView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            ((ScrollView)getFragView().findViewById(R.id.horizontalScrollView1)).setSmoothScrollingEnabled(true);
            ((TextView)getFragView().findViewById(R.id.textView1)).setMovementMethod(new ScrollingMovementMethod());

            return getFragView();
        }
    }
}
