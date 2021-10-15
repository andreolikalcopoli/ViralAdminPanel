package com.magma.viraladminpanel;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String birthday;
    private String userDescription;
    private String profilePhoto;
    private String token;
    private String phoneNumber;
    private String search;

    public User() { }

    public User(String userId, String firstName, String lastName, String birthday, String userDescription, String profilePhoto, String token, String phoneNumber, String search) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.userDescription = userDescription;
        this.profilePhoto = profilePhoto;
        this.token = token;
        this.phoneNumber = phoneNumber;
        this.search = search;
    }

    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return birthday; }
    public String getUserDescription() { return userDescription; }
    public String getProfilePhoto() { return profilePhoto; }
    public String getToken() { return token; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSearch() { return search; }
}
