package com.android.become_a_farmer;

import java.io.Serializable;

public class RecyclerItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String sub;
    private String introduction;

    public RecyclerItem() {
    }

    public RecyclerItem(String title, String sub, String introduction) {
        this.title = title;
        this.sub = sub;
        this.introduction = introduction;
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
}
