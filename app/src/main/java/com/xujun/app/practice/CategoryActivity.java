package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;

/**
 * Created by xujunwu on 15/9/4.
 */
public class CategoryActivity extends BaseActivity{

    private CategoryInfo        categoryInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        categoryInfo=(CategoryInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CATEGORY_INFO);
        if (categoryInfo!=null){
            mHeadTitle.setText(categoryInfo.getCategory());
        }
        ViewUtils.inject(this);

        initHeadBackView();
        hideSearchEditView();
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
