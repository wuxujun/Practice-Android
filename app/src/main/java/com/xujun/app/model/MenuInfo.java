package com.xujun.app.model;

import java.io.Serializable;

/**
 * Created by xujunwu on 15/8/26.
 */
public class MenuInfo  implements Serializable{

    private int     id;
    private String  title;
    private int     resid;

    public MenuInfo(int id,String title){
        this.id=id;
        this.title=title;
        this.resid=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }
}
