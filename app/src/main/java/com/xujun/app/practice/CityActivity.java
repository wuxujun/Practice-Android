package com.xujun.app.practice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.xujun.app.model.CityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/7.
 */
public class CityActivity extends BaseActivity implements View.OnClickListener {


    List<CityInfo> items=new ArrayList<CityInfo>();
    ItemAdapter  mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mHeadTitle.setText(getText(R.string.city_select));
        initHeadBackView();

        mAdapter=new ItemAdapter();
        mListView=(ListView)findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConfig.PARAM_CITY_INFO,items.get(i));
                intent.putExtras(bundle);
                CityActivity.this.setResult(RESULT_OK, intent);
                CityActivity.this.finish();
            }
        });
    }

    public void onResume(){
        super.onResume();
        loadData();
    }

    @Override
    public void loadData(){
        items.clear();
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<CityInfo> cityInfoList=db.findAll(CityInfo.class);
            if (cityInfoList!=null&&cityInfoList.size()>0){
                items.addAll(cityInfoList);
            }
        }catch (DbException e){
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void parserHttpResponse(String result){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHeadBack:{
                finish();
                break;
            }
            default:
                break;
        }
    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
    }

    class ItemAdapter extends BaseAdapter {


        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView holder;
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.city_listview_item,null);
                holder=new ItemView();
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            CityInfo cityInfo=items.get(position);
            if (cityInfo!=null) {
                holder.title.setText(cityInfo.getCityName());
            }
            return convertView;
        }
    }
}
