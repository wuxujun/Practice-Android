package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xujun.app.model.MenuInfo;
import com.xujun.app.practice.R;
import com.xujun.util.StringUtil;

import java.util.List;

/**
 * Created by xujunwu on 15/8/26.
 */
public class MenuItemAdapter extends BaseAdapter{

    private Context     mContext;
    private List<MenuInfo>    items;

    public MenuItemAdapter(Context context,List<MenuInfo> list){
        this.mContext=context;
        this.items=list;
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
            convertView=View.inflate(mContext, R.layout.item_head_menu,null);
            holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
            holder.icon=(ImageView)convertView.findViewById(R.id.ivItemIcon);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        MenuInfo info=items.get(position);
        if (info!=null){
            holder.title.setText(info.getTitle());
        }
        return convertView;
    }


    private class ItemView{
        TextView    title;
        ImageView   icon;
    }
}
