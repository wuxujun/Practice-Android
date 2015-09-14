package com.xujun.app.practice;

import android.os.Bundle;

/**
 * 我的收藏
 * Created by xujunwu on 15/9/6.
 */
public class MCollectionActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mHeadTitle.setText(getText(R.string.my_item_1));
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
