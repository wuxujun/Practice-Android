package com.xujun.app.practice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.BaseResp;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.CityResp;
import com.xujun.app.model.EduEntity;
import com.xujun.app.model.EduResp;
import com.xujun.app.model.Member;
import com.xujun.app.model.PhotoEntity;
import com.xujun.app.model.PhotoResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/10/4.
 */
public class NotifyService extends Service implements AMapLocationListener{

    private static final String TAG="NotifyService";

    private MyBinder       myBinder=new MyBinder();

    private List<PhotoEntity>   items=new ArrayList<PhotoEntity>();

    private AppContext          mAppContext;
    private Member              mMember;
    private PhotoEntity         mPhotoEntity;

    private LocationManagerProxy mLocationManagerProxy;

    private int                 nTotal;
    /**
     * 学校信息同步
     */
    private Handler handlerEduSync=new Handler();

    @Override
    public void onCreate(){
        super.onCreate();
        L.e(TAG, "onCreate() executed");
        mAppContext=(AppContext)getApplication();
        mMember=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
        mLocationManagerProxy=LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,10*60*1000,5,this);
        mLocationManagerProxy.setGpsEnable(false);

        Intent startIntent=new Intent(this,AlarmReceiver.class);
        startIntent.setAction(AppConfig.ACTION_ALARM);
        PendingIntent  sender=PendingIntent.getBroadcast(this,0,startIntent,0);
        long firsttime= SystemClock.elapsedRealtime();
        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firsttime,1*60*1000,sender);
        initEduInfo();
    }

    private void initEduInfo(){
        handlerEduSync.post(edu_run);
    }

    private Runnable edu_run=new Runnable(){
        @Override
        public void run() {
            loadSchool("0","20");
        }
    };


    /**
     * 加载学校信息
     */
    private void loadSchool(String start,String end){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start",start);
        requestMap.put("end",end);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.SCHOOL_QUERY, getRequestParams2(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e(TAG, responseInfo.result);
                parserSchoolResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                L.e(TAG, +error.getExceptionCode() + " " + msg);
            }
        });
    }

    private void parserSchoolResponse(String result){
        try{
            EduResp resp=(EduResp)JsonUtil.ObjFromJson(result,EduResp.class);
            if (resp.getRoot() != null) {
                addEduInfo(resp.getRoot());
            }
            if (resp.getEnd()<resp.getTotal()){
                loadSchool(""+resp.getEnd(),""+(resp.getEnd()+20));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addEduInfo(List<EduEntity> list){
        try{
            DbUtils db=DbUtils.create(this, AppConfig.DB_NAME);
            db.saveOrUpdateAll(list);
            L.e("addEduInfo  save ....record:"+list.size());
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    /**
     * 查询照片状态
     */
    private void queryPhotoForStatus(){
        try{
            DbUtils db=DbUtils.create(this, AppConfig.DB_NAME);
            List<PhotoEntity> list=db.findAll(Selector.from(PhotoEntity.class).where("status", "=", 0));
            if (list!=null){
                items.clear();
                items.addAll(list);
            }
            if (items.size()>0){
                uploadImage(items.get(0));
            }

            if (nTotal%15==0) {
                Map<String,Object> requestMap=new HashMap<String,Object>();
                requestMap.put("longitude", mAppContext.getProperty(AppConfig.CONF_CURRENT_LONGITUDE));
                requestMap.put("latitude", mAppContext.getProperty(AppConfig.CONF_CURRENT_LATITUDE));
                HttpUtils http=new HttpUtils();
                http.send(HttpRequest.HttpMethod.POST, URLs.USER_LOC_URL, getRequestParams2(requestMap), new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        L.e(TAG, responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        L.e(TAG, +error.getExceptionCode() + " " + msg);
                    }
                });
                nTotal=0;
            }
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    private void uploadImage(PhotoEntity entity){
        mPhotoEntity=entity;
        RequestParams params=new RequestParams();
        params.addBodyParameter("file", new File(entity.getFilePath()));
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.UPLOAD_IMAGE, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e(TAG,responseInfo.result);
                parserHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                L.e(TAG,+error.getExceptionCode()+" "+msg);
            }
        });
    }

    private void addMemberPhoto(String fileName){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("mid",mMember.getId());
        requestMap.put("photoName", mPhotoEntity.getFileName());
        requestMap.put("imageName", fileName);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.MEMBER_PHOTO_ADD, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e(TAG, responseInfo.result);
                parserPhotoHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                L.e(TAG, +error.getExceptionCode() + " " + msg);
            }
        });
    }

    private void parserPhotoHttpResponse(String result){
        try{
            BaseResp resp=(BaseResp)JsonUtil.ObjFromJson(result,BaseResp.class);
            if (resp.getSuccess()==1){
                queryPhotoForStatus();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parserHttpResponse(String result) {
        try{
            PhotoResp resp=(PhotoResp) JsonUtil.ObjFromJson(result,PhotoResp.class);
            if (resp.getSuccess()==1&&resp.getFilename()!=null){
                if (mMember!=null&&mPhotoEntity!=null){
                    updatePhotoEntity(resp.getFilename());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private RequestParams getRequestParams2(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
        try{
            String json= JsonUtil.toJson(maps);
            L.e(""+json);
            params.addBodyParameter("content", json);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return  params;
    }

    private RequestParams getRequestParams(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
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

    private void updatePhotoEntity(String fileName){
        try{
            mPhotoEntity.setImageUrl(fileName);
            mPhotoEntity.setIsUpload(1);
            mPhotoEntity.setStatus(1);
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.saveOrUpdate(mPhotoEntity);
            L.e(TAG,"--> "+mPhotoEntity.getId()+"  "+mPhotoEntity.getFilePath()+"  "+mPhotoEntity.getImageUrl()+"  "+mPhotoEntity.getStatus());
        }catch (DbException e){
            e.printStackTrace();
        }
        addMemberPhoto(fileName);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        L.e(TAG,"onStartCommand...");
        nTotal++;
        queryPhotoForStatus();
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        L.d(TAG,"onDestroy() ...");
        if (mLocationManagerProxy!=null){
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy=null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getAMapException().getErrorCode()==0){
            Double geoLat=aMapLocation.getLatitude();
            Double geoLng=aMapLocation.getLongitude();
            mAppContext.setProperty(AppConfig.CONF_CURRENT_LATITUDE, StringUtil.doubleToString6(geoLat));
            mAppContext.setProperty(AppConfig.CONF_CURRENT_LONGITUDE,StringUtil.doubleToString6(geoLng));
//            L.e(""+aMapLocation.getCityCode()+"  "+aMapLocation.getCity()+"  "+aMapLocation.getAdCode()+"  "+aMapLocation.getAddress()+"  "+aMapLocation.getLongitude()+" "+aMapLocation.getLatitude());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    class MyBinder extends Binder{
        public void startUpload() {
            L.e(TAG, "startUpload() executed");
        }
    }
}
