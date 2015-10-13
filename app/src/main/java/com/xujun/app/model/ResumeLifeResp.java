package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeLifeResp extends BaseResp{
    private List<ResumeLife>    root;

    public List<ResumeLife> getRoot() {
        return root;
    }

    public void setRoot(List<ResumeLife> root) {
        this.root = root;
    }
}
