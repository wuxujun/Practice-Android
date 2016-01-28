package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/12/6.
 */
public class NearbyResp  extends BaseResp{
    private List<NearbyEntity>  root;

    public List<NearbyEntity> getRoot() {
        return root;
    }

    public void setRoot(List<NearbyEntity> root) {
        this.root = root;
    }
}
