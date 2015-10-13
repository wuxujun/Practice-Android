package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeWorkResp extends BaseResp{
    private List<ResumeWork>  root;

    public List<ResumeWork> getRoot() {
        return root;
    }

    public void setRoot(List<ResumeWork> root) {
        this.root = root;
    }
}
