package com.android.activity;

public class Finger {
    String name;
    int id;
    int fingerID; 
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getFingerID(){
        return fingerID;
    }
    
    public void setFingerID(int fingerid){
        fingerID = fingerid;
    }
}
