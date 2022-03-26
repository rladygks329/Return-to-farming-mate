package com.android.become_a_farmer;

import java.io.Serializable;

public class RecyclerItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String sub;
    private String introduction;
    private String crop;
    private String experienceTitle;
    private String experienceContent;

    public RecyclerItem() {
    }

    public RecyclerItem(String title, String sub, String introduction) {
        this.title = title;
        this.sub = sub;
        this.introduction = introduction;
    }

    public RecyclerItem(String title, String sub, String introduction, String crop, String experienceTitle,
                        String experienceContent) {
        this.title = title;
        this.sub = sub;
        this.introduction = introduction;
        this.crop = crop;
        this.experienceTitle = experienceTitle;
        this.experienceContent = experienceContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getExperienceTitle() {
        return experienceTitle;
    }

    public void setExperienceTitle(String experienceTitle) {
        this.experienceTitle = experienceTitle;
    }

    public String getExperienceContent() {
        return experienceContent;
    }

    public void setExperienceContent(String experienceContent) {
        this.experienceContent = experienceContent;
    }
}
