package com.linkedin.replica.connections.models;

public class FriendsList {

    private String userId;
    private String firstName;
    private String lastName;
    private String imageURL;
    private String headline;

    public FriendsList(){};

    public FriendsList(String userId, String firstName, String lastName, String imageURL, String headline) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.headline = headline;
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

    public String getImageURL() {
        return imageURL;
    }

    public String getHeadline() {
        return headline;
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

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
