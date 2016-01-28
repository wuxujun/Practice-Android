package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/12/6.
 */
@Table(name = "t_params_nearby")
public class NearbyEntity extends BaseEntity {
    @Column(column = "province")
    private String  province;
    @Column(column = "cityId")
    private String  cityId;
    @Column(column = "type")
    private int  type;
    @Column(column = "pId")
    private String  pId;
    @Column(column = "code")
    private String  code;
    @Column(column = "title")
    private String  title;
    @Column(column = "state")
    private int     state;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
