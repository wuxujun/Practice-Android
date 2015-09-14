package com.xujun.app.practice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.CityInfo;
import com.xujun.util.JsonUtil;
import com.xujun.util.SystemBarTintManager;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xujunwu on 15/7/31.
 */
public abstract class BaseActivity extends SherlockActivity implements View.OnClickListener{

    protected AppContext    mAppContext;
    protected Context       mContext;


    protected TextView          mHeadTitle;
    protected ImageButton       mHeadBack;
    protected LinearLayout      mHeadBtnLeft;
    protected Button            mHeadBtnRight;
    protected LinearLayout      mHeadSearch;

    protected ListView      mListView;


    protected CityInfo          mCurrentCityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext=(AppContext)getApplication();
        mContext=getApplicationContext();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager=new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.action_bar_color);
        }

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        View actionbarLayout= LayoutInflater.from(this).inflate(R.layout.default_actionbar,null);
        mHeadTitle=(TextView)actionbarLayout.findViewById(R.id.tvHeadTitle);
        mHeadBack=(ImageButton)actionbarLayout.findViewById(R.id.ibHeadBack);
        mHeadBack.setOnClickListener(this);
        mHeadBtnLeft=(LinearLayout)actionbarLayout.findViewById(R.id.btnHeadLeft);
        mHeadBtnRight=(Button)actionbarLayout.findViewById(R.id.btnHeadRight);
        mHeadSearch=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadSearch);
        getActionBar().setCustomView(actionbarLayout);
    }


    public void initHeadBackView(){
        mHeadBack.setOnClickListener(this);
    }

    public void showSearchEditView(){
        mHeadTitle.setVisibility(View.GONE);
        mHeadSearch.setVisibility(View.VISIBLE);
    }

    public void hideSearchEditView(){
        mHeadTitle.setVisibility(View.VISIBLE);
        mHeadSearch.setVisibility(View.GONE);
    }

    public boolean dispatchKeyEvent(KeyEvent event){
        int keyCode=event.getKeyCode();
        if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public RequestParams getRequestParams(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
        maps.put("umeng_token", UmengRegistrar.getRegistrationId(mContext));
        try{
            String json= JsonUtil.toJson(maps);
            params.setBodyEntity(new StringEntity(json));
        }catch (JSONException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return  params;
    }

    public abstract void loadData();
    public abstract void parserHttpResponse(String result);

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHeadBack:{
                finish();
                break;
            }
        }
    }
}
