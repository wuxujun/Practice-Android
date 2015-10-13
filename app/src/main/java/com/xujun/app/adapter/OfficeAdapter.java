package com.xujun.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/9/4.
 */
public class OfficeAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfficeInfo> items=new ArrayList<OfficeInfo>();

    public void addAll(List<OfficeInfo> list){
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public OfficeAdapter(Context context){
        this.mContext=context;
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


    static class ItemView
    {
        public ImageView icon;
        public TextView title;
    }
}
