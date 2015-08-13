package com.xujun.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xujun.app.adapter.NetworkImageHolderView;
import com.xujun.app.practice.AppContext;
import com.xujun.app.practice.R;
import com.xujun.banner.BViewHolderCreator;
import com.xujun.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/2.
 */
public class HeadBannerView extends LinearLayout{

    private List<String>  items=new ArrayList<String>();

    private Context     mContext;
    private AppContext  mAppContext;
    private View        mContentView;

    private Banner      banner;

    public HeadBannerView(Context context){
        super(context,null);
        this.mContext=context;

        mContentView= LayoutInflater.from(mContext).inflate(R.layout.head_banner,null);
        banner=(Banner)mContentView.findViewById(R.id.banner);

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        addView(mContentView,lp);
    }

    public void addAll(List<String>  list){
        items.clear();
        items.addAll(list);
        banner.setPages(new BViewHolderCreator<NetworkImageHolderView>(){
            @Override
            public NetworkImageHolderView createHolder(){
                return new NetworkImageHolderView();
            }
        }, items).setPageIndicator(new int[]{R.drawable.ic_page_indicator,R.drawable.ic_page_indicator_focused}).setPageTransformer(Banner.Transformer.DefaultTransformer);
    }

}
