package com.xujun.app.practice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.AttentionEntity;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CategoryResp;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.CityResp;
import com.xujun.app.model.CompanyInfo;
import com.xujun.app.model.EduEntity;
import com.xujun.app.model.NearbyEntity;
import com.xujun.app.model.ParamInfo;
import com.xujun.app.model.ParamResp;
import com.xujun.app.model.PhotoEntity;
import com.xujun.app.model.SearchHisEntity;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/7/31.
 */
public class StartActivity extends Activity{


    private AppContext    mAppContext;
    private Context mContext;

    private int     dataType=0;
    private String  dataVersion="1";

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        mAppContext=(AppContext)getApplication();
        mContext=getApplicationContext();

        createShortcut();
        ViewUtils.inject(this);
        MobclickAgent.updateOnlineConfig(this);
    }

    private DbUtils.DbUpgradeListener mUpdateListener=new DbUtils.DbUpgradeListener() {
        @Override
        public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
            L.e("old:"+oldVersion+"  "+newVersion);
            try{
                db.dropTable(CategoryInfo.class);
                db.createTableIfNotExist(CategoryInfo.class);
                db.createTableIfNotExist(CityInfo.class);
                db.createTableIfNotExist(AttentionEntity.class);
                db.createTableIfNotExist(ParamInfo.class);
                db.createTableIfNotExist(PhotoEntity.class);
                db.createTableIfNotExist(CompanyInfo.class);
                db.createTableIfNotExist(SearchHisEntity.class);
                db.createTableIfNotExist(EduEntity.class);
                db.createTableIfNotExist(NearbyEntity.class);
            }catch (DbException e){
                e.printStackTrace();
            }
        }
    };

    public void loadData() {
        dataVersion=MobclickAgent.getConfigParams(mContext,"DataVersion");
        String localDataVirsion=mAppContext.getProperty(AppConfig.LOCAL_DATABASE_VERSION);
        L.e("loadData ..................." + dataVersion);
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME,1, mUpdateListener);
            db.configAllowTransaction(true);
            db.configDebug(true);
            L.e("begin Create Table.....");
            db.createTableIfNotExist(CategoryInfo.class);
            db.createTableIfNotExist(CityInfo.class);
            db.createTableIfNotExist(AttentionEntity.class);
            db.createTableIfNotExist(ParamInfo.class);
            db.createTableIfNotExist(PhotoEntity.class);
            db.createTableIfNotExist(CompanyInfo.class);
            db.createTableIfNotExist(SearchHisEntity.class);
            db.createTableIfNotExist(EduEntity.class);
            db.createTableIfNotExist(NearbyEntity.class);
            db.close();
        }catch (DbException e){
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(localDataVirsion)){
            L.e("...........First load");
            loadCategoryInfo("0","20");
        }else if(!localDataVirsion.equals(dataVersion)){
            L.e("..........LocalDatabaseVersion "+localDataVirsion+"  Server version:"+dataVersion);
            loadCategoryInfo("0","20");
        }else {
            if (queryCategoryInfo()==0){
                loadCategoryInfo("0","20");
            }else {
                L.e("......... No updated .....");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openHome();
                    }
                }, 1);
            }
        }
    }

    private void openHome(){
        Intent intent = new Intent(StartActivity.this, TabActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadCategoryInfo(String start,String end){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start",start);
        requestMap.put("end", end);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.CATEGORY_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() " + responseInfo.result);
                parserHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() " + s);
            }
        });
    }

    private void loadCityInfo(String start,String end){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start",start);
        requestMap.put("end", end);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.CITY_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() " + responseInfo.result);
                dataType = 1;
                parserHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() " + s);
            }
        });
    }

    private void loadParamInfo(String start,String end){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start",start);
        requestMap.put("end", end);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.PARAM_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() " + responseInfo.result);
                dataType = 2;
                parserHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() " + s);
            }
        });
    }

    public void parserHttpResponse(String result) {
        try{
            if (dataType==0) {
                CategoryResp resp = (CategoryResp) JsonUtil.ObjFromJson(result, CategoryResp.class);
                if (resp.getRoot() != null) {
                    addCategoryInfo(resp.getRoot());
                }
                if (resp.getEnd()<resp.getTotal()){
                    loadCategoryInfo(""+resp.getEnd(),""+(resp.getEnd()+20));
                }else{
                    loadCityInfo("0","20");
                }
            }else if(dataType==1) {
                CityResp resp=(CityResp)JsonUtil.ObjFromJson(result,CityResp.class);
                if (resp.getRoot()!=null){
                    addCityInfo(resp.getRoot());
                }
                if (resp.getEnd()<resp.getTotal()){
                    loadCityInfo(""+resp.getEnd(),""+(resp.getEnd()+20));
                }else{
                   loadParamInfo("0","20");
                }
            }else{
                ParamResp resp=(ParamResp)JsonUtil.ObjFromJson(result,ParamResp.class);
                if (resp.getRoot()!=null){
                    addParamInfo(resp.getRoot());
                }
                if (resp.getEnd()<resp.getTotal()){
                    loadParamInfo(""+resp.getEnd(),""+resp.getEnd()+20);
                }else{
                    mAppContext.setProperty(AppConfig.LOCAL_DATABASE_VERSION,dataVersion);
                    openHome();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addParamInfo(List<ParamInfo> list){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.saveOrUpdateAll(list);
            L.e("addParamInfo   save ....record:"+list.size());
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    private void addCityInfo(List<CityInfo> list){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.saveOrUpdateAll(list);
            L.e("addCityInfo  save ....record:"+list.size());
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    private void addCategoryInfo(List<CategoryInfo> list){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.saveOrUpdateAll(list);
            L.e("addCategoryInfo  save.... record:"+list.size());
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    private int queryCategoryInfo(){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
           List<CategoryInfo> categoryInfoList=db.findAll(CategoryInfo.class);
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                return categoryInfoList.size();
            }
        }catch (DbException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }

    /**
     * 创建桌面快捷方式
     */
    private void createShortcut() {
        SharedPreferences setting = getSharedPreferences("silent.preferences", 0);
        // 判断是否第一次启动应用程序（默认为true）
        boolean firstStart = setting.getBoolean("FIRST_START", true);
        // 第一次启动时创建桌面快捷方式
        if (firstStart) {
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            // 不允许重复创建
            shortcut.putExtra("duplicate", false);
            // 指定快捷方式的启动对象
            ComponentName comp = new ComponentName(this.getPackageName(), "." + this.getLocalClassName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
            // 快捷方式的图标
            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            // 发出广播
            sendBroadcast(shortcut);
            // 将第一次启动的标识设置为false
            SharedPreferences.Editor editor = setting.edit();
            editor.putBoolean("FIRST_START", false);
            // 提交设置
            editor.commit();
        }
    }


    private RequestParams getRequestParams(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
        maps.put("umeng_token", UmengRegistrar.getRegistrationId(mContext));
        try{
            String json= JsonUtil.toJson(maps);
            L.e(""+json);
            params.addBodyParameter("content", json);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return  params;
    }
}
