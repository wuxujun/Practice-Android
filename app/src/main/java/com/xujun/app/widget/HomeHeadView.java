package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xujun.app.practice.R;
import com.xujun.banner.Banner;

/**
 * Created by xujunwu on 15/8/27.
 */
public class HomeHeadView extends LinearLayout {

    private Context     mContext;
    private View        mContentView;
    private CustGridView        mGridView;

    private Banner              mBanner;


    public HomeHeadView(Context context) {
        super(context,null);
        this.mContext=context;
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.home_category_choose,null);
        mGridView=(CustGridView)mContentView.findViewById(R.id.gridview);
        mBanner=(Banner)mContentView.findViewById(R.id.banner);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

    public CustGridView getGridView(){
        return mGridView;
    }

    public Banner   getBanner(){
        return mBanner;
    }
}
