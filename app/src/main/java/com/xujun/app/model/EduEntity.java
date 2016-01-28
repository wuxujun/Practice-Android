package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/12/6.
 */
@Table(name = "t_params_edu")
public class EduEntity extends BaseEntity{
    @Column(column = "province")
    private String      province;
    @Column(column = "cityId")
    private String      cityId;
    @Column(column = "type")
    private int      type;
    @Column(column = "pId")
    private String      pId;
    @Column(column = "eduCode")
    private String      eduCode;
    @Column(column = "eduName")
    private String      eduName;
    @Column(column = "eduAddress")
    private String      eduAddress;
    @Column(column = "eduTel")
    private String      eduTel;
    @Column(column = "eduContact")
    private String      eduContact;
    @Column(column = "state")
    private int         state;

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

    public String getEduCode() {
        return eduCode;
    }

    public void setEduCode(String eduCode) {
        this.eduCode = eduCode;
    }

    public String getEduName() {
        return eduName;
    }

    public void setEduName(String eduName) {
        this.eduName = eduName;
    }

    public String getEduAddress() {
        return eduAddress;
    }

    public void setEduAddress(String eduAddress) {
        this.eduAddress = eduAddress;
    }

    public String getEduTel() {
        return eduTel;
    }

    public void setEduTel(String eduTel) {
        this.eduTel = eduTel;
    }

    public String getEduContact() {
        return eduContact;
    }

    public void setEduContact(String edutContact) {
        this.eduContact = edutContact;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
