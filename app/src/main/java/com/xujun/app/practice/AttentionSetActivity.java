package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.CityInfo;

/**
 * Created by xujunwu on 15/9/14.
 */
public class AttentionSetActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    @ViewInject(R.id.list)
    private ListView mListView;

    private ItemAdapter     mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        mHeadTitle.setText("修改我的关注");
        ViewUtils.inject(this);

        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemView holder;
            if (convertView==null){


            }else{
                holder=(ItemView)convertView.getTag();
            }
            return convertView;
        }
    }

    class ItemView{
        public TextView     title;
        public TextView     value;

    }
}

