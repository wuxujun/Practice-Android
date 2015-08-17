package com.xujun.app.model;

/**
 * Created by xujunwu on 15/8/16.
 */
public class CategoryInfo extends BaseResp{

    private Long  id;
    private String  code;
    private String  category;
    private String  parent_code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}
