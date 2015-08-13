package com.xujun.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.xujun.app.practice.LoginActivity;
import com.xujun.app.practice.R;
import com.xujun.app.practice.RegisterActivity;
import com.xujun.pullzoom.PullToZoomListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    public static final String TAG="MyFragment";

    List<String>  items=new ArrayList<String>();

    private ItemAdapter     mAdapter;

    private PullToZoomListView   listView;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.pull_to_zoom_listview,null);
        listView=(PullToZoomListView)mContentView.findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.getHeaderView().findViewById(R.id.tv_login).setOnClickListener(this);
        listView.getHeaderView().findViewById(R.id.tv_register).setOnClickListener(this);

        
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        return mContentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    private void loadData(){
        items.clear();
        items.add("0");
        items.add("每日推荐推送");
        items.add("移动网络图片质量");
        items.add("清除缓存");
        items.add("版本更新");
        items.add("关于我们");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login:{
                Log.e(TAG,"------> Login");
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_register:{
                Log.e(TAG,"-------> Register");
                Intent intent=new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG,"---------->"+i);
    }

    static class ItemView
    {
        public ImageView        icon;
        public TextView         title;

        public LinearLayout     head;
        public LinearLayout     item;
    }

    class ItemAdapter extends BaseAdapter{


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
                convertView= LayoutInflater.from(mContext).inflate(R.layout.my_listview_item,null);
                holder=new ItemView();
                holder.head=(LinearLayout)convertView.findViewById(R.id.llHead);
                holder.item=(LinearLayout)convertView.findViewById(R.id.llItem);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            if (items.get(position).equals("0")){
                holder.item.setVisibility(View.GONE);
            }else {
                holder.head.setVisibility(View.GONE);
                holder.title.setText(items.get(position));
            }
            return convertView;
        }
    }
}
