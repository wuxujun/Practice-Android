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
public class CategoryHeadView extends LinearLayout {

    private Context mContext;
    private View mContentView;

    private LinearLayout    linearLayout3;
    private LinearLayout    linearLayout4;

    private View            line2;
    private View            line3;

    private TextView    mCategory1;
    private TextView    mCategory2;
    private TextView    mCategory3;
    private TextView    mCategory4;

    private ImageView   mArrow1;
    private ImageView   mArrow2;
    private ImageView   mArrow3;
    private ImageView   mArrow4;


    public CategoryHeadView(Context context,View.OnClickListener clickListener) {
        super(context,null);
        this.mContext=context;
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.category_head,null);
        mContentView.findViewById(R.id.btnCategory1).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnCategory2).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnCategory3).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnCategory4).setOnClickListener(clickListener);

        linearLayout3=(LinearLayout)mContentView.findViewById(R.id.btnCategory3);
        linearLayout4=(LinearLayout)mContentView.findViewById(R.id.btnCategory4);

        line2=mContentView.findViewById(R.id.line2);
        line3=mContentView.findViewById(R.id.line3);

        mCategory1=(TextView)mContentView.findViewById(R.id.tvCategory1);
        mCategory2=(TextView)mContentView.findViewById(R.id.tvCategory2);
        mCategory3=(TextView)mContentView.findViewById(R.id.tvCategory3);
        mCategory4=(TextView)mContentView.findViewById(R.id.tvCategory4);

        mArrow1=(ImageView)mContentView.findViewById(R.id.ivCategory1);
        mArrow2=(ImageView)mContentView.findViewById(R.id.ivCategory2);
        mArrow3=(ImageView)mContentView.findViewById(R.id.ivCategory3);
        mArrow4=(ImageView)mContentView.findViewById(R.id.ivCategory4);


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

    public LinearLayout getLinearLayout3() {
        return linearLayout3;
    }

    public LinearLayout getLinearLayout4() {
        return linearLayout4;
    }

    public View getLine2() {
        return line2;
    }

    public View getLine3() {
        return line3;
    }
}
