package com.xujun.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.DataUtils;
import com.xujun.app.practice.R;
import com.xujun.banner.Banner;
import com.xujun.util.StringUtil;

import org.w3c.dom.Text;

/**
 * Created by xujunwu on 15/10/31.
 */
public class OfficeHeadView  extends LinearLayout{

    private Context mContext;
    private View mContentView;

    private DataUtils       dataUtils;



    public OfficeHeadView(Context context,View.OnClickListener clickListener) {
        super(context,null);
        this.mContext=context;
        dataUtils=new DataUtils(context);
        mContentView= LayoutInflater.from(mContext).inflate(R.layout.head_office,null);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        addView(mContentView,lp);
        mContentView.findViewById(R.id.btnCollection).setOnClickListener(clickListener);
        mContentView.findViewById(R.id.btnResumeSel).setOnClickListener(clickListener);

    }

    public void setOfficeInfo(OfficeInfo info){
        if (!StringUtil.isEmpty(info.getName())){
            ((TextView)mContentView.findViewById(R.id.tvOffice)).setText(info.getName());
        }
        if (!StringUtil.isEmpty(info.getAddress())){
            ((TextView)mContentView.findViewById(R.id.tvOfficeAddress)).setText(info.getAddress());
        }
        if(info.getCompanyId()>0){
            dataUtils.displayText(mContentView.findViewById(R.id.tvCompany),AppConfig.DATA_TYPE_COMPANY,String.valueOf(info.getCompanyId()));
        }
        dataUtils.displayImage(mContentView.findViewById(R.id.ivIcon), AppConfig.DATA_TYPE_COMPANY_LOGO, String.valueOf(info.getCompanyId()));
    }

    public void setCollectionStatus(boolean state){
        if (state){
            mContentView.findViewById(R.id.btnCollection).setBackgroundResource(R.drawable.ic_collect_sel);
        }else{
            mContentView.findViewById(R.id.btnCollection).setBackgroundResource(R.drawable.ic_collect);
        }
    }

    public void showLoadingView(){
        mContentView.findViewById(R.id.btnResumeSel).setVisibility(GONE);
        mContentView.findViewById(R.id.llResult).setVisibility(VISIBLE);
    }

    public void showResult(String msg){
        mContentView.findViewById(R.id.progressBar).setVisibility(INVISIBLE);
        ((TextView)mContentView.findViewById(R.id.tvResult)).setText(msg);
    }

}
