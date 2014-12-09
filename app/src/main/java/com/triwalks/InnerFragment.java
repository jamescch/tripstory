package com.triwalks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InnerFragment extends Fragment{
    protected View v;
    // prevent screen rotate inflate error
    private static int layout;

    public InnerFragment(){}

    public void setFragLayout(int layout){
        this.layout = layout;
    }

    public View getFragView(){
        return this.v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(this.layout, container, false);
        // the order of tow following lines of code must not change!
        return v;
    }
}
