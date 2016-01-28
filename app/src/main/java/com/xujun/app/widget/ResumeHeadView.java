package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/8/28.
 */
public class ResumeHeadView extends LinearLayout{

    private Context mContext;

    private View mContentView;

    public ResumeHeadView(Context context, View.OnClickListener clickListener) {
        super(context,null);
        this.mContext=context;
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.resume_head,null);
        mContentView.findViewById(R.id.btnResume1).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResume2).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResume3).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResume4).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResume5).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResume6).setOnClickListener(clickListener);

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

}
