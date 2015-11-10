package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.SearchHisEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/11/8.
 */
public class SearchResultActivity extends  BaseActivity{

    private SearchHisEntity     searchHisEntity;


    @ViewInject(R.id.list)
    private ListView        mListView;

    private OfficeAdapter           mAdapter;
    private List<OfficeInfo>        items=new ArrayList<OfficeInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ViewUtils.inject(this);
        initHeadBackView();

        searchHisEntity=(SearchHisEntity)getIntent().getSerializableExtra(AppConfig.PARAM_SEARCH_HIS);
        initView();
    }

    private void initView(){
        mAdapter=new OfficeAdapter(this);
        if (mListView!=null){
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }
    }

    @Override
    public void loadData() {
        if (searchHisEntity!=null){

        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }
}
