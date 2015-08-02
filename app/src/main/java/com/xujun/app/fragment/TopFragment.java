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
import com.xujun.app.practice.R;

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
    public void onClick(View view) {

    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
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

            }else{
                holder=(ItemView)convertView.getTag();
            }


            return convertView;
        }
    }
}
