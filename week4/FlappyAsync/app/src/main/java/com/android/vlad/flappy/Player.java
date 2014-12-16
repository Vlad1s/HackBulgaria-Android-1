package com.android.vlad.flappy;

public class Player {
    private String mName;
    private String mEmail;
    private String mUniversity;

    public Player(String name, String email, String university) {
        mName = name;
        mEmail = email;
        mUniversity = university;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUniversity() {
        return mUniversity;
    }

    public void setUniversity(String university) {
        mUniversity = university;
    }
}
