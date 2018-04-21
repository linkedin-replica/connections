package com.linkedin.replica.connections.models;

public class UserInFriendsList {

    private String userId;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;

    public UserInFriendsList(){};

    public UserInFriendsList(String userId, String firstName, String lastName, String profilePictureUrl) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UserInFriendsList(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserInFriendsList{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }

    public UserInFriendsList(User user){
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profilePictureUrl = user.getImageUrl();
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
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

    public void setProfilePictureUrl(String imageURL) {
        this.profilePictureUrl = imageURL;
    }

}
