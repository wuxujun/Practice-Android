package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/12/6.
 */
public class EduResp extends BaseResp{
    private List<EduEntity>     root;

    public List<EduEntity> getRoot() {
        return root;
    }

    public void setRoot(List<EduEntity> root) {
        this.root = root;
    }
}
