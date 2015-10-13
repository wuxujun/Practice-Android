package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeLife extends BaseResp{
    private int     rid;
    private int     mid;
    private String  orgName;
    private String  officeName;
    private String  beginTime;
    private String  endTime;


    private List<ResumeLifeInfo> infos;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public List<ResumeLifeInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<ResumeLifeInfo> infos) {
        this.infos = infos;
    }
}
