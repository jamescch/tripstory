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

public class ImageAdapter_Single extends ImageAdapter_Base {
    public ImageAdapter_Single(Context context, ExifList imageList, ImageLoader l, int imageViewLayout) {
        super(context, imageList, l, imageViewLayout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(imageViewLayout, parent, false);
        }
        ImageView imageView = ViewHolder.get(convertView, R.id.imageView);
        mImageLoader.loadBitmap(mList.get(position).getPath(), imageView);
        imageView.setTag(position);
        LinearLayout check = ViewHolder.get(convertView, R.id.img_select_check);
        check.setTag(position);
        if (mSparseBooleanArray.get(position)) {
            if (range[0] == position)
                check.setVisibility(View.VISIBLE);
        } else {
            check.setVisibility(View.INVISIBLE);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer) v.getTag() == range[0]) {
                    range[0] = -1;
                    v.setVisibility(View.INVISIBLE);
                    getSparseBooleanArray().put((Integer) v.getTag(), false);
                }
            }
        });

        return convertView;
    }

    public void unSelect(){
        mSparseBooleanArray.put(range[0], false);
        mSparseBooleanArray.put(range[1], false);
        range[0] = -1;
        range[1] = -1;
    }

    public void setExifList(ExifList exifList){
        mList = exifList;
    }
}