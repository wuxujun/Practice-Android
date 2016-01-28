package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.practice.R;

import java.util.List;

/**
 * Created by xujunwu on 15/9/4.
 */
public class CityCheckBoxAdapter extends BaseAdapter{

    private Context mContext;

    private List<CityInfo>   items;

    private boolean             isCheck;

    public CityCheckBoxAdapter(Context context, List<CityInfo> list, boolean isCheck){
        this.mContext=context;
        this.items=list;
        this.isCheck=isCheck;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ItemView    holder;
        if (convertView==null){
            holder=new ItemView();
            convertView=View.inflate(mContext, R.layout.category_check_box_item,null);
            holder.title=(TextView)convertView.findViewById(R.id.tvTitle);
            holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        holder.checkBox.setVisibility(View.GONE);
        if (isCheck){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        CityInfo info=items.get(position);
        if (info!=null){
            holder.title.setText(info.getCityName());
        }
        return convertView;
    }

    private class ItemView{
        public CheckBox checkBox;
        public TextView title;
    }
}
