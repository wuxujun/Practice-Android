package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/9/30.
 */
@Table(name="param")
public class ParamInfo extends BaseEntity {
    @Column(column = "type")
    private int type;
    @Column(column = "code")
    private String  code;
    @Column(column = "name")
    private String  name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
