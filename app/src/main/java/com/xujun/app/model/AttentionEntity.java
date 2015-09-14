package com.xujun.app.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by xujunwu on 15/9/14.
 */
@Table(name="attention")
public class AttentionEntity extends BaseEntity{

    @Column(column = "title")
    private String      title;
    @Column(column = "param")
    private String      param;
    @Column(column = "dataType")
    private String      dataType;
    @Column(column = "paramValue")
    private String      paramValue;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String value) {
        this.paramValue = value;
    }
}
