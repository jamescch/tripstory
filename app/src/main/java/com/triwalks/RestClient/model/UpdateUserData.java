package com.triwalks.RestClient.model;

/**
 * Created by tutul on 2014/12/31.
 */
public class UpdateUserData {
    String userid;
    String sessionid;
    String name;
    String nick;
    String gender;  // (man,woman)
    String age;
    String e_mail;
    String phone;
    location location;
    String birthday;  //1991/05/27
    String description;

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
