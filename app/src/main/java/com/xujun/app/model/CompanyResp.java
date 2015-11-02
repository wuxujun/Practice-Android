package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/8/16.
 */
public class CompanyResp extends BaseResp{
    private List<CompanyInfo>  root;

    public List<CompanyInfo> getRoot() {
        return root;
    }

    public void setRoot(List<CompanyInfo> root) {
        this.root = root;
    }
}
