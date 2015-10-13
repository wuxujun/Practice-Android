package com.xujun.app.model;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeLifeInfo extends BaseResp{
    private int     liid;
    private int     lid;
    private int     mid;
    private String  content;
    private String  addtime;

    public int getLiid() {
        return liid;
    }

    public void setLiid(int liid) {
        this.liid = liid;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
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
