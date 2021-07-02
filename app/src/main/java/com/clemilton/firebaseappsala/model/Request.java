package com.clemilton.firebaseappsala.model;

public class Request {
    private String userId,type;

    public Request(){

    }

    public Request(String userId,String type){
        this.userId = userId;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String TYPE_ADD ="add";
    public static String TYPE_SEND="send";
    public static String TYPE_RECEIVE="receive";

    public static String TYPE_FRIEND="friend";
    public static String TYPE_REJECT="reject";



}
