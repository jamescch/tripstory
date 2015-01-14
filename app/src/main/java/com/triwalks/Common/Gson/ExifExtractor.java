package com.triwalks.Common.Gson;

import android.media.ExifInterface;

import com.triwalks.Common.CommonFunction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExifExtractor {
    private String path;
    private double latitude;
    private double longitude;
    private String timestamp;

    public String getPath(){
        return path;
    }

    public double getLat(){
        return latitude;
    }

    public double getLong(){
        return longitude;
    }

    public Date getTimeStamp(){
        SimpleDateFormat f = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        Date date = new Date();
        try {
            date = f.parse(timestamp);
        }
        catch (ParseException e){
            //e.printStackTrace();
        }
        return date;
    }

    public double[] getGeoTag(){
        return new double[] {latitude, longitude};
    }

    public void setPath(String s){
        path = s;
    }

    public void setLat(double s){
        latitude = s;
    }

    public void setLong(double s){
        longitude = s;
    }

    public void setTimestamp(String s){
        timestamp = s;
    }

    public static ExifExtractor getExifExtractor(String path){
        ExifExtractor exif = new ExifExtractor();
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) == null)
                return null;
            exif.setPath(path);
            exif.setLat(
                    getQuadrant(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).charAt(0))*
                            CommonFunction.gpsToDecimal(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE))
            );
            exif.setLong(
                    getQuadrant(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).charAt(0))*
                            CommonFunction.gpsToDecimal(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE))
            );
            exif.setTimestamp(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
        return exif;
    }

    private static double getQuadrant(char s){
        switch(s){
            case 'S':
            case 'W':
                return -1;
            case 'N':
            case 'E':
                return 1;
        }
        return -1;
    }
}