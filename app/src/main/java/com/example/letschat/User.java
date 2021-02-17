package com.example.letschat;

public class User {


    private String username,phoneno,email,id,status,imageurl,search;

    public User(String fullName, String phoneNo, String email,String imageURL,String id,String status,String search) {
        this.username = fullName;
        this.phoneno = phoneNo;
        this.email = email;
        this.imageurl=imageURL;
        this.id=id;
        this.status=status;
        this.search=search;

    }
    public User(){


    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNo() {
        return phoneno;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageurl;
    }

    public String getId() {
        return id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
