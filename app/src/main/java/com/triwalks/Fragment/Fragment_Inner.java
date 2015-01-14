package com.triwalks.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triwalks.Common.Gson.ExifExtractor;
import com.triwalks.Common.Gson.ExifList;
import com.triwalks.R;

public class Fragment_Inner extends Fragment{
    private String[] Countries = null;
    protected View v;
    // prevent screen rotate inflate error
    private static int layout;
    private static ExifList exifList = null;
    private static SQLiteDatabase db;

    public Fragment_Inner(){}

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

    protected String[] getLocale() {
        if(this.Countries == null) {
            this.Countries = getResources().getStringArray(R.array.country);
        }
        return this.Countries;
    }

    public static void initExifList(Cursor imagecursor){
        exifList = new ExifList().init();

        int PHOTO_PATH = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int PHOTO_LATITUDE = imagecursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE);
        int PHOTO_LONGTITUDE = imagecursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE);
        int PHOTO_DATETAKEN = imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            ExifExtractor e = new ExifExtractor();
            e.setPath(imagecursor.getString(PHOTO_PATH));
            e.setLat(imagecursor.getDouble(PHOTO_LATITUDE));
            e.setLong(imagecursor.getDouble(PHOTO_LONGTITUDE));
            e.setTimestamp(imagecursor.getString(PHOTO_DATETAKEN));
            exifList.add(e);
        }
    }

    public static ExifList getExifList(){
        return exifList;
    }
}
