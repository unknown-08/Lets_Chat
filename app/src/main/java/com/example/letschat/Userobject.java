package com.example.letschat;

public class Userobject {

    private String uid,username,phoneNo;

    Userobject(String uid,String username,String phoneNo)
    {
        this.uid=uid;        this.username=username;
        this.phoneNo=phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
