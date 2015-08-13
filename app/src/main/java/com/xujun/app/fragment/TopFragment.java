package com.xujun.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.R;
import com.xujun.app.widget.RoundedLetterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class TopFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG="TopFragment";

    List<String> items=new ArrayList<String>();

    private ItemAdapter     mAdapter;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.fragment_list,null);
        mListView=(ListView)mContentView.findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        return mContentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    private void loadData(){
        items.clear();
        items.add("互联网");
        items.add("计算机软件");
        items.add("房地产/建筑");
        items.add("金融");
        items.add("通讯电子");
        items.add("快消");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    static class ItemView
    {
        public RoundedLetterView icon;

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
                convertView= LayoutInflater.from(mContext).inflate(R.layout.category_listview_item,null);
                holder=new ItemView();
                holder.icon=(RoundedLetterView)convertView.findViewById(R.id.rlName);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.title.setText(items.get(i));
            holder.icon.setTitleText(items.get(i).substring(0,1));
            return convertView;
        }
    }
}
