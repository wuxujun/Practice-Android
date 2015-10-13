package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/9/30.
 */
public class ParamResp extends BaseResp{
    private List<ParamInfo>     root;

    public List<ParamInfo> getRoot() {
        return root;
    }

    public void setRoot(List<ParamInfo> root) {
        this.root = root;
    }
}
