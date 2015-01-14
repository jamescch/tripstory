package com.triwalks.RestClient.model;

import java.util.List;

/**
 * Created by tutul on 2014/12/28.
 */
public class UserProfileData {
    Boolean result;
    String information;
    String userID;
    String sessionID;
    String account;
    String name;
    String nick;
    String gender;     //(man,woman)
    String age;
    String e_mail;
    String phone;
    location location;
    String birthday;    //1991/05/27
    List<String> invited;
    List<String> inviting;
    List<String> friend;
    List<String> black_list;
    String description;
    List<String> trace_user;
    List<String> trace_tripleline;

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

    public String getUserid() {
        return userID;
    }

    public void setUserid(String userid) {
        this.userID = userid;
    }

    public String getSessionid() {
        return sessionID;
    }

    public void setSessionid(String sessionid) {
        this.sessionID = sessionid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getInvited() {
        return invited;
    }

    public void setInvited(List<String> invited) {
        this.invited = invited;
    }

    public List<String> getInviting() {
        return inviting;
    }

    public void setInviting(List<String> inviting) {
        this.inviting = inviting;
    }

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(List<String> friend) {
        this.friend = friend;
    }

    public List<String> getBlack_list() {
        return black_list;
    }

    public void setBlack_list(List<String> black_list) {
        this.black_list = black_list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTrace_user() {
        return trace_user;
    }

    public void setTrace_user(List<String> trace_user) {
        this.trace_user = trace_user;
    }

    public List<String> getTrace_tripleline() {
        return trace_tripleline;
    }

    public void setTrace_tripleline(List<String> trace_tripleline) {
        this.trace_tripleline = trace_tripleline;
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

