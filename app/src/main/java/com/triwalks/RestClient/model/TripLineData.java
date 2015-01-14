package com.triwalks.RestClient.model;
import java.util.List;

/**
 * Created by tutul on 2014/12/28.
 */
public class TripLineData {
    Boolean result;
    String triplineID;
    String information;
    String userID;
    int ispublic;
    String trip_name;
    List<String> path;
    String description;
    int worldrank;
    int like;
    int favorite;

    public String getTriplineID() {
        return triplineID;
    }

    public void setTriplineID(String triplineID) {
        this.triplineID = triplineID;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWorldrank() {
        return worldrank;
    }

    public void setWorldrank(int worldrank) {
        this.worldrank = worldrank;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }



}
