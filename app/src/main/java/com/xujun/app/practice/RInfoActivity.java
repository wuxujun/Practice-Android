package com.xujun.app.practice;

import android.os.Bundle;

import com.xujun.app.model.MenuInfo;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RInfoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mHeadTitle.setText(getText(R.string.resume_head_1));
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
