package com.linkedin.replica.connections.models;

public class UserInFriendsList {

    private String userId;
    private String firstName;
    private String lastName;
    private String imageURL;

    public UserInFriendsList(){};

    public UserInFriendsList(String userId, String firstName, String lastName, String imageURL, String headline) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
    }

    public UserInFriendsList(User user){
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageURL = user.getImageUrl();
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

}
