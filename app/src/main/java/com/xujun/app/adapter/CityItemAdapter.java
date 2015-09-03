package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.practice.R;

import java.util.List;

/**
 * Created by xujunwu on 15/8/27.
 */
public class CityItemAdapter extends BaseAdapter{

    private Context mContext;
    private List<CityInfo>  items;
    private int             resId;
    public CityItemAdapter(Context context,List<CityInfo> list,int rid){
        this.mContext=context;
        this.items=list;
        this.resId=rid;
    }

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ItemView    holder;
        if (convertView==null){
            holder=new ItemView();
            convertView=View.inflate(mContext, resId,null);
            holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        CityInfo info=items.get(position);
        if (info!=null){
            holder.title.setText(info.getCityName());
        }
        return convertView;
    }

    private class ItemView{
        public TextView     title;
    }
}
