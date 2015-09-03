package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/9/3.
 */
public class CategoryHeadView extends LinearLayout {

    private Context mContext;
    private View mContentView;

    private TextView    mCategory1;
    private TextView    mCategory2;
    private TextView    mCategory3;
    private TextView    mCategory4;


    public CategoryHeadView(Context context,View.OnClickListener clickListener) {
        super(context,null);
        this.mContext=context;
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.category_head,null);
        mContentView.findViewById(R.id.tvCategory1).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.tvCategory2).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.tvCategory3).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.tvCategory4).setOnClickListener(clickListener);

        mCategory1=(TextView)mContentView.findViewById(R.id.tvCategory1);
        mCategory2=(TextView)mContentView.findViewById(R.id.tvCategory2);
        mCategory3=(TextView)mContentView.findViewById(R.id.tvCategory3);
        mCategory4=(TextView)mContentView.findViewById(R.id.tvCategory4);

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

    public TextView getCategory1() {
        return mCategory1;
    }

    public TextView getCategory2() {
        return mCategory2;
    }

    public TextView getCategory3() {
        return mCategory3;
    }

    public TextView getCategory4() {
        return mCategory4;
    }
}
