package com.xujun.app.practice;

import android.os.Bundle;

/**
 * Created by xujunwu on 15/9/6.
 */
public class MessageActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mHeadTitle.setText(getText(R.string.my_message));
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
