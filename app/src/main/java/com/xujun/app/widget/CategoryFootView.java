package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/9/3.
 */
public class CategoryFootView extends LinearLayout {

    private Context mContext;
    private View mContentView;

    private CustListView    listView;


    public CategoryFootView(Context context) {
        super(context,null);
        this.mContext=context;
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.category_foot,null);
        listView=(com.xujun.app.widget.CustListView)mContentView.findViewById(R.id.list);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

    public CustListView getListView(){
        return listView;
    }

}
