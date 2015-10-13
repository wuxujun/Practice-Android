package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/8.
 */
public class InputList extends BaseResp{
    private List<InputInfo> root;

    public List<InputInfo> getRoot() {
        return root;
    }

    public void setRoot(List<InputInfo> root) {
        this.root = root;
    }
}
