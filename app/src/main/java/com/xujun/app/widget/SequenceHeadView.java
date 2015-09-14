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
 * Created by xujunwu on 15/9/4.
 */
public class SequenceHeadView extends LinearLayout{
    private Context mContext;
    private View mContentView;


    private TextView    mDataOrder1;
    private TextView    mDataOrder2;
    private TextView    mDataOrder3;
    private TextView    mDataOrder4;

    private ImageView   mArrow1;
    private ImageView   mArrow2;
    private ImageView   mArrow3;
    private ImageView   mArrow4;


    public SequenceHeadView(Context context, View.OnClickListener clickListener) {
        super(context, null);
        this.mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.list_sequence, null);

        mContentView.findViewById(R.id.btnDataOrder1).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnDataOrder2).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnDataOrder3).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnDataOrder4).setOnClickListener(clickListener);

        mDataOrder1=(TextView)mContentView.findViewById(R.id.tvDataOrder1);
        mDataOrder2=(TextView)mContentView.findViewById(R.id.tvDataOrder2);
        mDataOrder3=(TextView)mContentView.findViewById(R.id.tvDataOrder3);
        mDataOrder4=(TextView)mContentView.findViewById(R.id.tvDataOrder4);

        mArrow1=(ImageView)mContentView.findViewById(R.id.ivDataOrder1);
        mArrow2=(ImageView)mContentView.findViewById(R.id.ivDataOrder2);
        mArrow3=(ImageView)mContentView.findViewById(R.id.ivDataOrder3);
        mArrow4=(ImageView)mContentView.findViewById(R.id.ivDataOrder4);

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

    public TextView getDataOrder1() {
        return mDataOrder1;
    }

    public TextView getDataOrder2() {
        return mDataOrder2;
    }

    public TextView getDataOrder3() {
        return mDataOrder3;
    }

    public TextView getDataOrder4() {
        return mDataOrder4;
    }

    public ImageView getArrow1() {
        return mArrow1;
    }

    public ImageView getArrow2() {
        return mArrow2;
    }

    public ImageView getArrow3() {
        return mArrow3;
    }

    public ImageView getArrow4() {
        return mArrow4;
    }
}
