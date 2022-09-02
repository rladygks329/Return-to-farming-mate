package com.android.become_a_farmer;

import java.util.ArrayList;

public class UserDTO {
    private String email = "";
    private String password = "";
    private String name = "";
    private String nickname = "";
    private int age = 0;
    private String keywords = "";
    private String recommendRegions = "";

    public UserDTO() {}
    public UserDTO(String email, String password, String name, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.age = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRecommendRegions() {
        return recommendRegions;
    }

    public void setRecommendRegions(String recommendRegions) {
        this.recommendRegions = recommendRegions;
    }
}
