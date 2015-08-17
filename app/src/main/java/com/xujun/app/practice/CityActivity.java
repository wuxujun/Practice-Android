package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/7.
 */
public class CityActivity extends BaseActivity {


    List<String> items=new ArrayList<String>();
    ItemAdapter  mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mHeadTitle.setText(getText(R.string.city_select));
        mHeadBack.setImageDrawable(getResources().getDrawable(R.drawable.back));
        mHeadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mHeadBtnRight.setVisibility(View.INVISIBLE);

        mAdapter=new ItemAdapter();
        mListView=(ListView)findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("cityName",items.get(i));
                CityActivity.this.setResult(RESULT_OK, intent);
                CityActivity.this.finish();
            }
        });
    }

    public void onResume(){
        super.onResume();
        loadData();
    }

    public void loadData(){
        items.clear();
        items.add("上海");
        items.add("北京");
        items.add("杭州");
        mAdapter.notifyDataSetChanged();
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
            holder.title.setText(items.get(position));
            return convertView;
        }
    }
}
