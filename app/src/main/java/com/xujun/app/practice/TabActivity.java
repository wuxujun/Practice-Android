package com.xujun.app.practice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xujun.app.fragment.HomeFragment;
import com.xujun.app.fragment.MyFragment;
import com.xujun.app.fragment.SearchFragment;
import com.xujun.app.fragment.TopFragment;
import com.xujun.util.StringUtil;

import org.w3c.dom.Text;

/**
 * Created by xujunwu on 15/8/1.
 */
public class TabActivity extends SherlockFragmentActivity implements View.OnClickListener{

    private static final String TAG="TabActivity";

    private Context     mContext;
    private AppContext  appContext;

    @ViewInject(R.id.ll_tab_home)
    private LinearLayout        mTabHome;
    @ViewInject(R.id.ll_tab_top)
    private LinearLayout        mTabTop;
    @ViewInject(R.id.ll_tab_search)
    private LinearLayout        mTabSearch;
    @ViewInject(R.id.ll_tab_my)
    private LinearLayout        mTabMy;

    @ViewInject(R.id.ib_tab_home)
    private ImageButton         mImgHome;
    @ViewInject(R.id.ib_tab_top)
    private ImageButton         mImgTop;
    @ViewInject(R.id.ib_tab_search)
    private ImageButton         mImgSearch;
    @ViewInject(R.id.ib_tab_my)
    private ImageButton         mImgMy;

    @ViewInject(R.id.tv_tab_home)
    private TextView            mTvHome;
    @ViewInject(R.id.tv_tab_top)
    private TextView            mTvTop;
    @ViewInject(R.id.tv_tab_search)
    private TextView            mTvSearch;
    @ViewInject(R.id.tv_tab_my)
    private TextView            mTvMy;

    private SherlockFragment    mTab01;
    private SherlockFragment    mTab02;
    private SherlockFragment    mTab03;
    private SherlockFragment    mTab04;


    private TextView        mHeadTitle;
    private ImageButton     mHeadBack;
    private Button          mHeadBtnLeft;
    private Button          mHeadBtnRight;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_tab);

        appContext=(AppContext)getApplication();
        mContext=getApplicationContext();


        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        View actionbarLayout= LayoutInflater.from(this).inflate(R.layout.custom_actionbar,null);
        mHeadTitle=(TextView)actionbarLayout.findViewById(R.id.tvHeadTitle);
        mHeadBack=(ImageButton)actionbarLayout.findViewById(R.id.ibHeadBack);
        mHeadBack.setVisibility(View.INVISIBLE);
        mHeadBack.setOnClickListener(this);
        mHeadBtnLeft=(Button)actionbarLayout.findViewById(R.id.btnHeadLeft);
        mHeadBtnLeft.setOnClickListener(this);
        mHeadBtnLeft.setText(getText(R.string.city));
        mHeadBtnRight=(Button)actionbarLayout.findViewById(R.id.btnHeadRight);
        mHeadBtnRight.setOnClickListener(this);
        getActionBar().setCustomView(actionbarLayout);

        ViewUtils.inject(this);

        mTabHome.setOnClickListener(this);
        mTabTop.setOnClickListener(this);
        mTabSearch.setOnClickListener(this);
        mTabMy.setOnClickListener(this);

        UmengUpdateAgent.update(this);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);

        PushAgent pushAgent=PushAgent.getInstance(mContext);
        pushAgent.enable();


        setSelect(0);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                if (!StringUtil.isEmpty(data.getStringExtra("cityName"))) {
                    mHeadBtnLeft.setText(data.getStringExtra("cityName"));
                }
            }
        }
    }

    private void setSelect(int type){
        resetTabImg();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragment(transaction);
        switch (type){
            case 0:{
                if (mTab01==null){
                    mTab01=new HomeFragment();
                    transaction.add(R.id.content_frame,mTab01);
                }else{
                    transaction.attach(mTab01);
                }
                mHeadBtnLeft.setVisibility(View.VISIBLE);
                mHeadTitle.setText(getText(R.string.tab_home));
                mTvHome.setTextColor(getResources().getColor(R.color.white));
                mImgHome.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 1:{

                if (mTab02==null){
                    mTab02=new TopFragment();
                    transaction.add(R.id.content_frame,mTab02);
                }else{
                    transaction.attach(mTab02);
                }
                mHeadTitle.setText(getText(R.string.tab_top));
                mTvTop.setTextColor(getResources().getColor(R.color.white));
                mImgTop.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 2:{

                if (mTab03==null){
                    mTab03=new SearchFragment();
                    transaction.add(R.id.content_frame,mTab03);
                }else{
                    transaction.attach(mTab03);
                }
                mHeadTitle.setText(getText(R.string.tab_search));
                mTvSearch.setTextColor(getResources().getColor(R.color.white));
                mImgSearch.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 3:{
                if (mTab04==null){
                    mTab04=new MyFragment();
                    transaction.add(R.id.content_frame,mTab04);
                }else{
                    transaction.attach(mTab04);
                }
                mHeadTitle.setText(getText(R.string.tab_my));
                mHeadBtnLeft.setVisibility(View.INVISIBLE);
                mTvMy.setTextColor(getResources().getColor(R.color.white));
                mImgMy.setImageResource(R.drawable.ic_tab_my);
                break;
            }
        }
        transaction.commit();
    }

    private void resetTabImg(){
        mImgHome.setImageResource(R.drawable.ic_tab_home);
        mImgTop.setImageResource(R.drawable.ic_tab_home);
        mImgSearch.setImageResource(R.drawable.ic_tab_home);
        mImgMy.setImageResource(R.drawable.ic_tab_my);

        mTvHome.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvTop.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvSearch.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvMy.setTextColor(getResources().getColor(R.color.app_listtab_off));
    }


    private void hideFragment(FragmentTransaction ft){
        if (mTab01!=null){
            ft.detach(mTab01);
        }
        if (mTab02!=null){
            ft.detach(mTab02);
        }
        if (mTab03!=null){
            ft.detach(mTab03);
        }
        if (mTab04!=null){
            ft.detach(mTab04);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_tab_home:
                setSelect(0);
                break;
            case R.id.ll_tab_top:
                setSelect(1);
                break;
            case R.id.ll_tab_search:
                setSelect(2);
                break;
            case R.id.ll_tab_my:
                setSelect(3);
                break;
            case R.id.btnHeadLeft:
            {
                Intent intent=new Intent(TabActivity.this,CityActivity.class);
               startActivityForResult(intent,1);
                break;
            }
        }
    }
}
