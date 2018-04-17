package com.linkedin.replica.connections.models;

import java.util.ArrayList;

public  class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String imageUrl;
    private ArrayList<UserInFriendsList> friendsList;


    public User() {};

    public User(String userId, String firstName, String lastName, String headline,
                String imageUrl, ArrayList<UserInFriendsList> userInFriendsList) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.imageUrl = imageUrl;
        this.friendsList = userInFriendsList;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHeadline() {
        return headline;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<UserInFriendsList> getFriendsList() {
        return friendsList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFriendsList(ArrayList<UserInFriendsList> userInFriendsList) {
        this.friendsList = userInFriendsList;
    }

}
