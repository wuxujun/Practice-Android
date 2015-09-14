package com.xujun.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.CityInfo;
import com.xujun.app.practice.AppContext;
import com.xujun.app.practice.R;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/1.
 */
public abstract class BaseFragment extends SherlockFragment{
    protected Context       mContext;
    protected AppContext    mAppContext;

    protected View          mContentView;


    protected CityInfo cityInfo;

    public void setCityInfo(CityInfo cityInfo){
        this.cityInfo=cityInfo;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mContext=getSherlockActivity().getApplicationContext();
        mAppContext=(AppContext)getSherlockActivity().getApplication();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }


    public RequestParams  getRequestParams(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
        maps.put("umeng_token", UmengRegistrar.getRegistrationId(mContext));
        try{
            String json= JsonUtil.toJson(maps);
            L.i("getRequestParams() "+json);
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
}
