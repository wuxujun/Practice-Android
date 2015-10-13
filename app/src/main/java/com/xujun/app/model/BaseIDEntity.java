package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import java.io.Serializable;

/**
 * Created by xujunwu on 15/8/27.
 */
public abstract class BaseIDEntity implements Serializable{
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
