package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.CityInfo;

/**
 * Created by xujunwu on 15/8/27.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        ViewUtils.inject(this);
        initHeadBackView();
        showSearchEditView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                super.onClick(view);
        }
    }
}
