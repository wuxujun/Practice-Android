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
            holder.icon=(ImageView)convertView.findViewById(R.id.ivIcon);
            holder.horLine=(View)convertView.findViewById(R.id.horLine);
            holder.verLine=(View)convertView.findViewById(R.id.verLine);
            convertView.setTag(holder);
        }else{
            holder=(ItemView)convertView.getTag();
        }
        CategoryInfo info=items.get(position);
        holder.horLine.setVisibility(View.INVISIBLE);
        holder.verLine.setVisibility(View.GONE);
        if (info!=null){
            holder.title.setText(info.getCategory());
            if (info.getCode().equals("103001")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_2);
            }else if(info.getCode().equals("103002")){
                holder.horLine.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.ic_home_cate_2);
            }else if(info.getCode().equals("103005")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_3);
                holder.horLine.setVisibility(View.VISIBLE);
            }else if(info.getCode().equals("104001")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_4);
                holder.horLine.setVisibility(View.VISIBLE);
            }else if(info.getCode().equals("104002")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_5);

                holder.verLine.setVisibility(View.VISIBLE);
            }else if(info.getCode().equals("104003")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_6);
                holder.horLine.setVisibility(View.VISIBLE);
                holder.verLine.setVisibility(View.VISIBLE);
            }else if(info.getCode().equals("104004")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_7);
                holder.horLine.setVisibility(View.VISIBLE);
                holder.verLine.setVisibility(View.VISIBLE);
            }else if(info.getCode().equals("104006")){
                holder.icon.setImageResource(R.drawable.ic_home_cate_8);
                holder.horLine.setVisibility(View.VISIBLE);
                holder.verLine.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private class ItemView{
        public TextView     title;
        public View         verLine;
        public View         horLine;
        public ImageView    icon;
    }
}
