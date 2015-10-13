package com.xujun.app.model;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeWorkInfo extends BaseResp{
    private int     wiid;
    private int     wid;
    private int     mid;
    private String  content;
    private String  addtime;

    public int getWiid() {
        return wiid;
    }

    public void setWiid(int wiid) {
        this.wiid = wiid;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
