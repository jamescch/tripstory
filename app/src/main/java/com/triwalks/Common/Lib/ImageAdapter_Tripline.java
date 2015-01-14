package com.triwalks.Common.Lib;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.triwalks.Common.Gson.ExifList;
import com.triwalks.R;

import java.util.ArrayList;

public class ImageAdapter_Tripline extends ImageAdapter_Base {
    public ImageAdapter_Tripline(Context context, ExifList imageList, ImageLoader l, int imageViewLayout) {
        super(context, imageList, l, imageViewLayout);
    }

    public ArrayList<Integer> getCheckedItems() {
        ArrayList<Integer> mTempArry = new ArrayList<Integer>();

        if(range[0] == -1 || range[1] == -1)
            return null;

        for(int i = range[1]; i <= range[0]; i++){
            mTempArry.add(i);
        }

        return mTempArry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(imageViewLayout, parent, false);
        }
        ImageView imageView = ViewHolder.get(convertView, R.id.imageView);
        mImageLoader.loadBitmap(mList.get(position).getPath(), imageView);
        imageView.setTag(position);

        LinearLayout flight = ViewHolder.get(convertView, R.id.img_select_flight);
        LinearLayout home = ViewHolder.get(convertView, R.id.img_select_home);
        flight.setTag(position);
        home.setTag(position);
        if (mSparseBooleanArray.get(position)) {
            if (range[0] == position)
                flight.setVisibility(View.VISIBLE);
            else if (range[1] == position)
                home.setVisibility(View.VISIBLE);
        } else {
            flight.setVisibility(View.INVISIBLE);
            home.setVisibility(View.INVISIBLE);
        }

        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer) v.getTag() == range[0]) {
                    range[0] = -1;
                    v.setVisibility(View.INVISIBLE);
                }
                getSparseBooleanArray().put((Integer) v.getTag(), false);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer) v.getTag() == range[1]) {
                    range[1] = -1;
                    v.setVisibility(View.INVISIBLE);
                }
                getSparseBooleanArray().put((Integer) v.getTag(), false);
            }
        });
        return convertView;
    }
}