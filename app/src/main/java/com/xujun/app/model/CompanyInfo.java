package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/8/7.
 */
@Table(name="t_company")
public class CompanyInfo extends BaseEntity{

    @Column(column = "cityId")
    private String  cityId;
    @Column(column = "name")
    private String  name;
    @Column(column = "title")
    private String  title;
    @Column(column = "logo")
    private String  logo;

    @Column(column = "image")
    private String  image;

    @Column(column = "content")
    private String  content;

    @Column(column = "remark")
    private String  remark;

    @Column(column = "logo")
    private String  address;

    @Column(column = "tel")
    private String  tel;

    @Column(column = "contact")
    private String  contact;

    @Column(column = "logo")
    private String  category;

    @Column(column = "website")
    private String  website;

    @Column(column = "scale")
    private String  scale;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
