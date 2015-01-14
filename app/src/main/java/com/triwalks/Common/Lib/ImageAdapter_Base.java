package com.triwalks.Common.Lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.triwalks.Common.Gson.ExifExtractor;
import com.triwalks.Common.Gson.ExifList;
import com.triwalks.R;

import java.util.ArrayList;

public class ImageAdapter_Base extends BaseAdapter {
    protected ExifList mList;
    protected Context mContext;
    protected SparseBooleanArray mSparseBooleanArray;
    protected ImageLoader mImageLoader;
    protected int[] range = {-1, -1};
    protected int imageViewLayout;

    public ImageAdapter_Base(Context context, ExifList imageList, ImageLoader l, int imageViewLayout) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mSparseBooleanArray = new SparseBooleanArray();
        this.mList = imageList;
        this.mImageLoader = l;
        this.imageViewLayout = imageViewLayout;
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

    public Integer getSingleCheckedItem(){
        return range[0];
    }

    public int[] getRange(){
        return range;
    }

    public SparseBooleanArray getSparseBooleanArray(){
        return mSparseBooleanArray;
    }

    @Override
    //Must be given at first
    public int getCount() {
        if(mList != null) {
            return mList.size();
        }
        else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(imageViewLayout, parent, false);
        }
        ImageView imageView = ViewHolder.get(convertView, R.id.imageView);
        mImageLoader.loadBitmap(mList.get(position).getPath(), imageView);
        imageView.setTag(position);
        /*check.setTag(position);
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
        });*/

        /*if(imageViewLayout == R.layout.multi_item_photo) {
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
        }
        else if(imageViewLayout == R.layout.single_item_photo){*/
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
//        }

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