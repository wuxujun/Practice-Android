package com.xujun.app.practice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

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
import com.xujun.app.model.Member;
import com.xujun.app.model.PhotoEntity;
import com.xujun.app.model.PhotoResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.URLs;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/10/4.
 */
public class NotifyService extends Service{

    private static final String TAG="NotifyService";

    private MyBinder       myBinder=new MyBinder();

    private List<PhotoEntity>   items=new ArrayList<PhotoEntity>();

    private AppContext          mAppContext;
    private Member              mMember;
    private PhotoEntity         mPhotoEntity;

    @Override
    public void onCreate(){
        super.onCreate();
        L.e(TAG, "onCreate() executed");
        mAppContext=(AppContext)getApplication();
        mMember=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
        Intent startIntent=new Intent(this,AlarmReceiver.class);
        startIntent.setAction(AppConfig.ACTION_ALARM);
        PendingIntent  sender=PendingIntent.getBroadcast(this,0,startIntent,0);
        long firsttime= SystemClock.elapsedRealtime();
        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firsttime,1*60*1000,sender);

    }

    private void queryPhotoForStatus(){
        try{
            L.e(TAG,"queryPhotoForStatus....");
            DbUtils db=DbUtils.create(this, AppConfig.DB_NAME);
            List<PhotoEntity> list=db.findAll(Selector.from(PhotoEntity.class).where("status","=",0));
            if (list!=null){
                L.e(TAG,"queryPhotoForStatus =>"+list.size());
                items.clear();
                items.addAll(list);
            }
            if (items.size()>0){
                uploadImage(items.get(0));
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
        requestMap.put("imageName",fileName);
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
        queryPhotoForStatus();
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        L.d(TAG,"onDestroy() ...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    class MyBinder extends Binder{
        public void startUpload() {
            L.e(TAG, "startUpload() executed");
        }
    }
}
