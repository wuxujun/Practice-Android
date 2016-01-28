package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xujun.app.model.CategoryInfo;
import com.xujun.app.practice.R;

import java.util.List;

/**
 * Created by xujunwu on 15/9/4.
 */
public class CategoryCheckBoxAdapter extends BaseAdapter{

    private Context mContext;

    private List<CategoryInfo>   items;

    private boolean             isCheck;
    private int                 resid;

    public CategoryCheckBoxAdapter(Context context, List<CategoryInfo> list,boolean isCheck,int resId){
        this.mContext=context;
        this.items=list;
        this.isCheck=isCheck;
        this.resid=resId;
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
            convertView=View.inflate(mContext, resid,null);
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
        CategoryInfo info=items.get(position);
        if (info!=null){
            holder.title.setText(info.getCategory());
        }
        return convertView;
    }

    private class ItemView{
        public CheckBox checkBox;
        public TextView title;
    }
}
