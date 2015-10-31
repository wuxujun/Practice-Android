package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.practice.R;
import com.xujun.app.widget.RoundedLetterView;

import java.util.List;

/**
 * Created by xujunwu on 15/8/27.
 */
public class HomeCateAdapter extends BaseAdapter {

    private Context mContext;
    private List<CategoryInfo> items;

    public HomeCateAdapter(Context context,List<CategoryInfo> list){
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
            convertView=View.inflate(mContext, R.layout.item_home_cate,null);
            holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
            holder.name=(RoundedLetterView)convertView.findViewById(R.id.rlItemName);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        CategoryInfo info=items.get(position);
        if (info!=null){
            holder.title.setText(info.getCategory());
            holder.name.setTitleText(info.getCategory().substring(0, 1));
        }
        return convertView;
    }

    private class ItemView{
        public TextView     title;
        RoundedLetterView   name;
    }
}
