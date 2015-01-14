package com.triwalks.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CommonFunction {
    public static boolean Personal_User_Page(){
        return true;
    }

    public static Intent gotoIntent(Context context, Class c){
        Intent intent = new Intent();
        intent.setClass(context, c);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent gotoIntent_WithObject(Context context, Class c, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(context, c);
        intent.putExtras(bundle);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent gotoIntent_WithString(Context context, Class c, String accessName, String value){
        Bundle bundle = new Bundle();
        bundle.putString(accessName, value);
        Intent intent = new Intent();
        intent.setClass(context, c);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent returnIntent_WithObject(Activity activity, Bundle bundle){
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, returnIntent);
        return returnIntent;
    }

    public static boolean isGPSEnabled(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean hasGpsTag(String file) {
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            // north and south
            if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) == null)
                return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }

    // convert gps to decimal
    public static double gpsToDecimal(String s){
        double gps = 0.0;
        double[] offset = {1.0, 60.0, 3600.0};
        String[] latitude = s.split(",");

        for(int i=0; i<latitude.length; i++){
            String[] temp = latitude[i].split("/");
            gps += Double.parseDouble(temp[0])/(Double.parseDouble(temp[1])*offset[i]);
        }
        return gps;
    }

    public static int[] getScreenDP(Context context){
        int[] screenSize = new int[2];
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenSize[0] = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        screenSize[1] = Math.round(displayMetrics.heightPixels / displayMetrics.density);
        return screenSize;
    }

    public static void logPhotoInfo(String text){
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists()){
            try{
                logFile.createNewFile();
            }
            catch (IOException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try{
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeToFile(Context context, String data, String path) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(path, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            ;
        }
    }

    public static String readFromFile(Context context, String path) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(path);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            //Log.e("login activity", "File not found: " + e.toString());
            return null;
        } catch (IOException e) {
            //Log.e("login activity", "Can not read file: " + e.toString());
            return null;
        }
        return ret;
    }

    public static Location getLocationProvider(LocationManager lm){
        Location l = null;
        try{
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            l = lm.getLastKnownLocation(lm.getBestProvider(criteria, true));
        }
        catch (Exception e){
            //Toast.makeText(getFragView().getContext(), "Failed to get location provider", Toast.LENGTH_SHORT).show();
        }
        return l;
    }

    public static boolean saveImageToExternalStorage(Context context, String storagePath, String fileName, Bitmap image) {
        try {
            File dir = new File(storagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(storagePath, fileName);
            file.createNewFile();
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 30, fOut);
            fOut.flush(); fOut.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
