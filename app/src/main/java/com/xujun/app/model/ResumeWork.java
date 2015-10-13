package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeWork extends BaseResp{
    private int  wid;
    private int  mid;
    private String  companyName;
    private String  officeName;
    private String  beginTime;
    private String  endTime;

    private List<ResumeWorkInfo>    infos;

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ResumeWorkInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<ResumeWorkInfo> infos) {
        this.infos = infos;
    }
}
