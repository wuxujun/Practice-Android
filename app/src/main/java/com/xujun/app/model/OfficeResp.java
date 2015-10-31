package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/30.
 */
public class OfficeResp extends BaseResp{
    private List<OfficeInfo>        root;

    public List<OfficeInfo> getRoot() {
        return root;
    }

    public void setRoot(List<OfficeInfo> root) {
        this.root = root;
    }

}
