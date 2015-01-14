package com.triwalks.Common;

public class UserData {
    private static UserData instance = null;
    private static String User_ID;
    private static String Session_ID;
    private static String User_Name;

    // implement Userdara as a singleton class
    // to ensure there is no more than one Userdata instance in whole program
    public static UserData getInstance(){
        if(instance == null){
            synchronized (UserData.class){
                if(instance == null){
                    instance = new UserData();
                }
            }
        }
        return instance;
    }

    public void init() {
        // call backend REST API to retrieve user information
    }

    public boolean isPersonalPage(){
        return true;
    }

    public UserData setUserID(String userID){
        User_ID = userID;
        return this;
    }

    public String getUser_ID(){
        return User_ID;
    }

    public UserData setSessionID(String session_ID){
        Session_ID = session_ID;
        return this;
    }

    public String getSession_ID(){
        return Session_ID;
    }
}
