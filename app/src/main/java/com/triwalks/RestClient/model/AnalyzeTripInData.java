package com.triwalks.RestClient.model;

/**
 * Created by tutul on 2015/1/11.
 */
public class AnalyzeTripInData {
    String userid;
    String sessionid;
    String triplineid;
    int nlevel;
    int windowX;
    int windowY;
    int picture_pixel;
    location GPS_left_up;
    location GPS_right_down;

    public String getTriplineid() {
        return triplineid;
    }

    public void setTriplineid(String triplineid) {
        this.triplineid = triplineid;
    }

    public int getPicture_pixel() {
        return picture_pixel;
    }

    public void setPicture_pixel(int picture_pixel) {
        this.picture_pixel = picture_pixel;
    }

    public location getGPS_right_down() {
        return GPS_right_down;
    }

    public void setGPS_right_down(location GPS_right_down) {
        this.GPS_right_down = GPS_right_down;
    }

    public location getGPS_left_up() {
        return GPS_left_up;
    }

    public void setGPS_left_up(location GPS_left_up) {
        this.GPS_left_up = GPS_left_up;
    }

    public int getWindowX() {
        return windowX;
    }

    public void setWindowX(int windowX) {
        this.windowX = windowX;
    }

    public int getWindowY() {
        return windowY;
    }

    public void setWindowY(int windowY) {
        this.windowY = windowY;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getNlevel() {
        return nlevel;
    }

    public void setNlevel(int nlevel) {
        this.nlevel = nlevel;
    }

    public class location
    {
        double x;
        double y;

        location(double _x,double _y)
        {
            x = _x;
            y = _y;
        }
        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }
    }

}
