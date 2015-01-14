package com.triwalks.RestClient.model;

import java.util.List;

/**
 * Created by tutul on 2014/12/31.
 * 也可以被用來更新 photo info
 */
public class UpdateTripLinePathData {
    String userid;
    String triplineid;
    String sessionid;
    List<path> path;
    class path
    {
        String cID;         //必要
        location location;
        String store_path;
        String time;
        String introduction;
        int ispublic;       //1 or 0
        String description;
        int favorite;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getFavorite() {
            return favorite;
        }

        public void setFavorite(int favorite) {
            this.favorite = favorite;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getIspublic() {
            return ispublic;
        }

        public void setIspublic(int ispublic) {
            this.ispublic = ispublic;
        }

        public String getcID() {
            return cID;
        }

        public void setcID(String cID) {
            this.cID = cID;
        }

        public String getStore_path() {
            return store_path;
        }

        public void setStore_path(String store_path) {
            this.store_path = store_path;
        }

        public UpdateTripLinePathData.path.location getLocation() {
            return location;
        }

        public void setLocation(UpdateTripLinePathData.path.location location) {
            this.location = location;
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
}
