package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;

import com.xujun.app.model.MenuInfo;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RTemplateActivity extends BaseActivity{

    private MenuInfo        menuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        menuInfo=(MenuInfo)getIntent().getSerializableExtra(AppConfig.PARAM_MENU_INFO);
        if (menuInfo!=null) {
            mHeadTitle.setText(menuInfo.getTitle());
        }
        initHeadBackView();
        hideSearchEditView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {

    }
}
