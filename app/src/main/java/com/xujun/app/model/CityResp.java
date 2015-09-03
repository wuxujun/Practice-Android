package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/9/2.
 */
public class CityResp extends BaseResp {
    private List<CityInfo> root;

    public List<CityInfo> getRoot() {
        return root;
    }
    public void setRoot(List<CityInfo> root) {
        this.root = root;
    }
}
