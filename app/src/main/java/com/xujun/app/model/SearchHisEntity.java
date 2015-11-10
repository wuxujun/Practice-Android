package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/11/8.
 */

@Table(name="search_his")
public class SearchHisEntity extends BaseIDEntity{

    @Column(column = "title")
    private String          title;
    @Column(column = "addtime")
    private long            addtime;

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
