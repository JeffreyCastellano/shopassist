package com.fa.google.shopassist.models;

/**
 * Created by mjoyce on 2/24/15.
 */
public class User {
    private String mFirstName;
    private String mLastName;
    private String mEmail;

    public User() {}

    public User(String firstName, String lastName, String email) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail=email;
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public String getLastName() {
        return this.mLastName;
    }
    public String getEmail() {
        return this.mEmail;
    }

    public String getFullName() {
        return this.mFirstName + " " + this.mLastName;
    }

}
