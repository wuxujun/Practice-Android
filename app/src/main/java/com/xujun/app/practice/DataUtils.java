package com.xujun.app.practice;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
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
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CompanyInfo;
import com.xujun.app.model.CompanyResp;
import com.xujun.app.model.OfficeResp;
import com.xujun.app.practice.AppConfig;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.json.JSONException;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/11/1.
 */
public class DataUtils {

    private Context context;
    private BitmapUtils bitmapUtils;

    public DataUtils(Context context){
        if (context==null){
            throw  new IllegalArgumentException("context may not be null");
        }

        this.context=context.getApplicationContext();
        bitmapUtils=new BitmapUtils(context);
    }

    public <T extends View> void displayImage(T container,int dataType,String value){
        if (container==null){
            return;
        }
        container.clearAnimation();
        switch (dataType){
            case AppConfig.DATA_TYPE_COMPANY_LOGO:{
                try{
                    DbUtils db=DbUtils.create(context,AppConfig.DB_NAME);
                    List<CompanyInfo> companyInfoList=db.findAll(Selector.from(CompanyInfo.class).where("id", "=", value));
                    if (companyInfoList!=null&&companyInfoList.size()>0){
                        L.e(""+companyInfoList.get(0).getLogo()+"  "+companyInfoList.get(0).getImage());
                        if (!StringUtil.isEmpty(companyInfoList.get(0).getLogo())) {
                            bitmapUtils.display(container, companyInfoList.get(0).getLogo());
                        }
                    }
                }catch (DbException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public <T extends View> void displayText(T container,int dataType,String value){
        if (container==null){
            return;
        }
        container.clearAnimation();
        switch (dataType){
            case AppConfig.DATA_TYPE_COMPANY:{
                try{
                    DbUtils db=DbUtils.create(context,AppConfig.DB_NAME);
                    List<CompanyInfo> companyInfoList=db.findAll(Selector.from(CompanyInfo.class).where("id", "=", value));
                    if (companyInfoList!=null&&companyInfoList.size()>0){
                        ((TextView)container).setText(companyInfoList.get(0).getName());
                    }else{
                        requestData((TextView)container,value);
                    }
                }catch (DbException e){
                    e.printStackTrace();
                }
                break;
            }
            case AppConfig.DATA_TYPE_COMPANY_LOGO:{
                try{
                    DbUtils db=DbUtils.create(context,AppConfig.DB_NAME);
                    List<CompanyInfo> companyInfoList=db.findAll(Selector.from(CompanyInfo.class).where("id", "=", value));
                    if (companyInfoList!=null&&companyInfoList.size()>0){
                        if (!StringUtil.isEmpty(companyInfoList.get(0).getLogo())) {
                            bitmapUtils.display(container, companyInfoList.get(0).getLogo());
                        }
                    }
                }catch (DbException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void requestData(final TextView textView, String value){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("companyId",value);
        RequestParams params=new RequestParams();
        requestMap.put("umeng_token", UmengRegistrar.getRegistrationId(context));
        try{
            String json= JsonUtil.toJson(requestMap);
            params.addBodyParameter("content",json);
        }catch (JSONException e){
            e.printStackTrace();
        }
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.COMPANY_LIST_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() " + responseInfo.result);
                try {
                    CompanyResp resp = (CompanyResp) JsonUtil.ObjFromJson(responseInfo.result, CompanyResp.class);
                    if (resp.getSuccess() == 1) {
                        if (resp.getRoot()!=null&&resp.getRoot().size()>0) {
                            addCompanyInfo(resp.getRoot());
                            L.e("===>"+resp.getRoot().get(0).getLogo());
                            CompanyInfo info = resp.getRoot().get(0);
                            textView.setText(info.getName());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() " + s);
            }
        });
    }


    private void addCompanyInfo(List<CompanyInfo> list){
        try{
            DbUtils db=DbUtils.create(context,AppConfig.DB_NAME);
            db.saveOrUpdateAll(list);
        }catch (DbException e){
            e.printStackTrace();
        }
    }

}
