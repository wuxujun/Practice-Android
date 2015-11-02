package com.xujun.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.DataUtils;
import com.xujun.app.practice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/9/4.
 */
public class OfficeAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfficeInfo> items=new ArrayList<OfficeInfo>();
    private DataUtils           dataUtils;

    public void addAll(List<OfficeInfo> list){
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void add(List<OfficeInfo> list){
        items.addAll(list);
        notifyDataSetChanged();
    }

    public OfficeAdapter(Context context){
        this.mContext=context;
        dataUtils=new DataUtils(context);
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
        ItemView        holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.home_listview_item,null);
            holder=new ItemView();
            holder.company=(TextView)convertView.findViewById(R.id.tvItemCompany);
            holder.office=(TextView)convertView.findViewById(R.id.tvItemOffice);
            holder.content=(TextView)convertView.findViewById(R.id.tvItemContent);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        OfficeInfo info=items.get(position);
        if (info!=null){
            holder.office.setText(info.getName());
            dataUtils.displayText(holder.company, AppConfig.DATA_TYPE_COMPANY,String.valueOf(info.getCompanyId()));
        }
        return convertView;
    }


    static class ItemView
    {
        public ImageView icon;
        public TextView company;
        public TextView office;
        public TextView content;
    }

}
