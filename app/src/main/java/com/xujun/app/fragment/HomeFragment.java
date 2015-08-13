package com.xujun.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.OfficeActivity;
import com.xujun.app.practice.R;
import com.xujun.app.widget.HeadBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    public static final String TAG="HomeFragment";

    List<OfficeInfo> items=new ArrayList<OfficeInfo>();

    private ItemAdapter     mAdapter;

    private HeadBannerView  mHeadView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mHeadView=new HeadBannerView(mContext);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.fragment_list,null);
        mListView=(ListView)mContentView.findViewById(R.id.list);
        mListView.addHeaderView(mHeadView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return mContentView;
    }

    public void onResume(){
        super.onResume();
        loadData();
    }

    private void loadData(){
        List<String>  images=new ArrayList<String>();
        images.add("http://sx.asiainstitute.cn/images/ai0.png");
        images.add("http://sx.asiainstitute.cn/images/ai1.png");
        mHeadView.addAll(images);


        items.clear();
        OfficeInfo info=new OfficeInfo();
        info.setId(1);
        info.setName("测试工程师");
        items.add(info);
        info=new OfficeInfo();
        info.setId(2);
        info.setName("销售人员");
        items.add(info);

        info=new OfficeInfo();
        info.setId(3);
        info.setName("文秘");
        items.add(info);
        info=new OfficeInfo();
        info.setId(4);
        info.setName("UI设计");
        items.add(info);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(getSherlockActivity(), OfficeActivity.class);
        startActivity(intent);
    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
    }

    class ItemAdapter extends BaseAdapter {

        @Override
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

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ItemView        holder;
            if (convertView==null){
               convertView= LayoutInflater.from(mContext).inflate(R.layout.home_listview_item,null);
                holder=new ItemView();
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            OfficeInfo info=items.get(i);
            if (info!=null){
                holder.title.setText(info.getName());
            }

            return convertView;
        }
    }
}
