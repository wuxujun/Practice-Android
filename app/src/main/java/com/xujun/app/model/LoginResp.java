package com.xujun.app.model;

/**
 * Created by xujunwu on 15/10/4.
 */
public class LoginResp extends BaseResp{
    private Member      data;
    private int         isExist;

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }
}
