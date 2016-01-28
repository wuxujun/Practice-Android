package com.xujun.app.model;

import java.util.List;

/**
 * Created by xujunwu on 15/10/7.
 */
public class ResumeLangResp extends BaseResp{
    private List<ResumeLang>   root;

    public List<ResumeLang> getRoot() {
        return root;
    }

    public void setRoot(List<ResumeLang> root) {
        this.root = root;
    }
}
