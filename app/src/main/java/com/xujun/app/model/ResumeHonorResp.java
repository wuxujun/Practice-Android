package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeHonorResp extends BaseResp{
    private List<ResumeHonor>   root;

    public List<ResumeHonor> getRoot() {
        return root;
    }

    public void setRoot(List<ResumeHonor> root) {
        this.root = root;
    }
}
