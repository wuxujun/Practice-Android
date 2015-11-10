package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xujun.app.model.CompanyInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.DataUtils;
import com.xujun.app.practice.R;
import com.xujun.util.StringUtil;

/**
 * Created by xujunwu on 15/10/31.
 */
public class CompanyHeadView extends LinearLayout{

    private Context mContext;
    private View mContentView;

    private DataUtils       dataUtils;

    public CompanyHeadView(Context context, OnClickListener clickListener) {
        super(context,null);
        this.mContext=context;
        dataUtils=new DataUtils(context);
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.item_company_info,null);
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        addView(mContentView, lp);

        mContentView.findViewById(R.id.btnFav).setOnClickListener(clickListener);
    }

    public void setCompany(CompanyInfo info){
        if (!StringUtil.isEmpty(info.getName())){
            ((TextView)mContentView.findViewById(R.id.tvCompany)).setText(info.getName());
        }
        if (!StringUtil.isEmpty(info.getAddress())){
            ((TextView)mContentView.findViewById(R.id.tvCompanyAddress)).setText(info.getAddress());
        }
//        dataUtils.displayImage(mContentView.findViewById(R.id.ivIcon), AppConfig.DATA_TYPE_COMPANY_LOGO,String.valueOf(info.getCompanyId()));
    }

}
