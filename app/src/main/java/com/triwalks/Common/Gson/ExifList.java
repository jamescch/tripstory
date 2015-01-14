package com.triwalks.Common.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ExifList {
    // ArrayList<ExifExtractor> only store the photo that contains exif info
    private ArrayList<ExifExtractor> photos;
    // HashMap<Integer, String> store all photo path and whether it contains exif
    private HashMap<String, Boolean> photosHash;

    public ExifExtractor get(int posi){
        return photos.get(posi);
    }

    public ExifList init(){
        photos = new ArrayList<ExifExtractor>();
        photosHash = new HashMap<String, Boolean>();
        return this;
    }

    public void add(ExifExtractor e){
        /*for(int i=0; i<photos.size(); i++){
            if (e.getTimeStamp().before(photos.get(i).getTimeStamp())) {
                photos.add(i, e);
                photosHash.put(e.getPath(), true);
                return;
            }
        }*/
        photos.add(e);
        //photosHash.put(e.getPath(), e.getLat()==0?false:true);
    }

    public void addToHashMap(String path){
        photosHash.put(path, false);
    }

    public void remove(int posi){
        photosHash.remove(photos.get(posi).getPath());
        photos.remove(posi);
    }

    public int getIndex(String s){
        for(int i=0; i<photos.size(); i++){
            if(s.equals(photos.get(i).getPath()))
                return i;
        }
        return -1;
    }

    public int size(){
        return photos.size();
    }

    public boolean isExist(String s){
        return photosHash.containsKey(s);
    }

    public boolean hasExif(String s){
        return photosHash.get(s);
    }

    public ExifExtractor getLastElement(){
        return photos.get(photos.size()-1);
    }
}
