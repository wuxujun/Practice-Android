package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xujun.app.model.BaseResp;
import com.xujun.app.model.Member;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.widget.OfficeHeadView;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.UIHelper;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/7.
 */
public class OfficeActivity extends BaseTActivity implements View.OnClickListener{

    private List<String> items=new ArrayList<String>();

    @ViewInject(R.id.list)
    private ListView            mListView;

    private ItemAdapter             mAdapter;
    private OfficeHeadView          headView;
    private OfficeInfo              officeInfo;

    private int                     actionType;

    private View.OnClickListener onClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnSubmit:{
                    actionType=1;
                    break;
                }
                case R.id.btnCollection:{
                    actionType=2;
                    break;
                }
                case R.id.btnAttention:{
                    actionType=3;
                    break;
                }
            }
            sendRequest();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        officeInfo=(OfficeInfo)getIntent().getSerializableExtra(AppConfig.PARAM_OFFICE_INFO);
        ViewUtils.inject(this);

        initHeadBackView();
        hideSearchEditView();
        mHeadTitle.setVisibility(View.GONE);
        mHeadCategory.setVisibility(View.VISIBLE);
        mHeadTabText1.setText("职位");
        mHeadTabText2.setText("公司");
        mHeadTab1.setOnClickListener(this);
        mHeadTab2.setOnClickListener(this);
        mHeadTab3.setVisibility(View.GONE);
        mHeadBtnRight.setText(getText(R.string.share));
        mHeadBtnRight.setOnClickListener(this);
        initView();
    }

    private void initView(){
        mAdapter=new ItemAdapter();
        headView=new OfficeHeadView(this,onClickListener);
        if (mListView!=null){
            mListView.addHeaderView(headView);
            mListView.setAdapter(mAdapter);
        }
        if (officeInfo!=null){
            headView.setOfficeInfo(officeInfo);
            if (!StringUtil.isEmpty(officeInfo.getContent())){
                items.add(officeInfo.getContent());
            }
            if (!StringUtil.isEmpty(officeInfo.getRemark())){
                items.add(officeInfo.getRemark());
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void openLoginActivity(){
        Intent intent=new Intent(OfficeActivity.this, LoginActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt(AppConfig.PARAM_LOGIN_SOURCE, AppConfig.LOGIN_TYPE_OFFICE);
        intent.putExtras(bundle);
        startActivityForResult(intent, AppConfig.REQUEST_RESUME_LOGIN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHeadBack:{
                finish();
                break;
            }
            case R.id.btnHeadRight:{
                mController.getConfig().setPlatforms(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL);
                mController.openShare(OfficeActivity.this,false);
                break;
            }
        }
    }


    private void sendRequest(){
        if (mMember==null){
            openLoginActivity();
            return;
        }
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("companyId",officeInfo.getCompanyId());
        requestMap.put("officeId", officeInfo.getId());
        requestMap.put("actionType",actionType);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.OFFICE_ACTION_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
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

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode==AppConfig.REQUEST_RESUME_LOGIN){
            if (resultCode==AppConfig.SUCCESS){
                mMember=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
                sendRequest();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void loadData(){

    }

    @Override
    public void parserHttpResponse(String result) {
        try{
            BaseResp resp=(BaseResp) JsonUtil.ObjFromJson(result,BaseResp.class);
            if (resp.getSuccess()==1){
                showCroutonMessage(resp.getErrorMsg());
            }else{
                showCroutonMessage("请求失败,请重试!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
        public TextView content;
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemView        holder;
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.item_office_desc,null);
                holder=new ItemView();
                holder.title=(TextView)convertView.findViewById(R.id.tvTitle);
                holder.content=(TextView)convertView.findViewById(R.id.tvContent);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            String str=items.get(position);
            holder.content.setText(str);
            if (position==0){
                holder.title.setText("职位描述");
            }else{
                holder.title.setText("职位要求");
            }
            return convertView;
        }
    }


    /**
     *分享
     */
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private void configPlatforms()
    {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        SinaShareContent sinaShareContent=new SinaShareContent();
        sinaShareContent.setShareContent("亚洲学院");
        sinaShareContent.setTargetUrl("http://www.asiainstitute.cn");
        mController.setShareMedia(sinaShareContent);


        SmsHandler smsHandler=new SmsHandler();
        smsHandler.addToSocialSDK();
        SmsShareContent sms=new SmsShareContent();
        sms.setShareContent("亚洲学院 http://www.asiainstitute.cn");
        mController.setShareMedia(sms);

        EmailHandler emailHandler=new EmailHandler();
        emailHandler.addToSocialSDK();
        MailShareContent mail=new MailShareContent();
        mail.setTitle("亚洲学院");
        mail.setShareContent("http://www.asiainstitute.cn");
        mController.setShareMedia(mail);

        String appId=AppConfig.WEIXIN_APPID;
        String appSecret=AppConfig.WEIXIN_APPSECRET;
        UMWXHandler umwxHandler=new UMWXHandler(OfficeActivity.this,appId,appSecret);
        umwxHandler.addToSocialSDK();

        UMWXHandler umwxHandler1=new UMWXHandler(OfficeActivity.this,appId,appSecret);
        umwxHandler1.setToCircle(true);
        umwxHandler1.addToSocialSDK();

        WeiXinShareContent weiXinShareContent=new WeiXinShareContent();
        weiXinShareContent.setShareContent("亚洲学院");
        weiXinShareContent.setTitle("亚洲学院");
        weiXinShareContent.setTargetUrl("http://www.asiainstitute.cn");
        mController.setShareMedia(weiXinShareContent);

        CircleShareContent circleShareContent=new CircleShareContent();
        circleShareContent.setShareContent("亚洲学院");
        circleShareContent.setTargetUrl("http://www.asiainstitute.cn");
        mController.setShareMedia(circleShareContent);


        appId=AppConfig.QQ_APPID;
        appSecret=AppConfig.QQ_APPSECRET;
        UMQQSsoHandler umqqSsoHandler=new UMQQSsoHandler(OfficeActivity.this,appId,appSecret);
//        umqqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        umqqSsoHandler.addToSocialSDK();
        QQShareContent qqShareContent=new QQShareContent();
        qqShareContent.setShareContent("亚洲学院");
        qqShareContent.setTitle("亚洲学院");
        qqShareContent.setTargetUrl("http://www.asiainstitute.cn");
        mController.setShareMedia(qqShareContent);

        QZoneSsoHandler qZoneSsoHandler=new QZoneSsoHandler(OfficeActivity.this,appId,appSecret);
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone=new QZoneShareContent();
        qzone.setShareContent("亚洲学院");
        qzone.setTargetUrl("http://www.asiainstitute.cn");
        qzone.setTitle("亚洲学院");
        mController.setShareMedia(qzone);
    }

}
