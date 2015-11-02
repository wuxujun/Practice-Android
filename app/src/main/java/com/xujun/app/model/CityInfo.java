package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/8/27.
 */
@Table(name="city")
public class CityInfo extends BaseEntity{

    @Column(column = "cityId")
    private String    cityId;
    @Column(column = "type")
    private int       type;  //0 省 1地市 2 县区
    @Column(column = "pid")
    private String  pid;

    @Column(column = "cityName")
    private String    cityName;
    @Column(column = "top")
    private int       top;

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override
    public String toString(){
        return "CityInfo{"+"id:"+getId()+",cityId="+cityId+ ",cityName=" +cityName+",type="+type+",top="+top+"}";
    }
}
