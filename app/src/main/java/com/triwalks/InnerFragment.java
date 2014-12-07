package com.triwalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InnerFragment extends Fragment{
    private View v;
    private int layout;

    public InnerFragment(){}

    protected void onCreate(){}

    protected void setFragLayout(int layout){
        this.layout = layout;
    }

    protected View getFragView(){
        return this.v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(this.layout, container, false);
        // the order of tow following lines of code must not change!
        this.v = rootView;
        onCreate();
        return rootView;
    }
}
