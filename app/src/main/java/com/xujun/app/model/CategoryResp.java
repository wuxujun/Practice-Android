package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/8/16.
 */
public class CategoryResp extends BaseResp{
    private List<CategoryInfo>  root;

    public List<CategoryInfo> getRoot() {
        return root;
    }

    public void setRoot(List<CategoryInfo> root) {
        this.root = root;
    }
}
