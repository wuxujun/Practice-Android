package com.xujun.app.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/7.
 */
public class OfficeActivity extends BaseActivity implements View.OnClickListener{

    private List<String> items=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        mHeadTitle.setText(getText(R.string.office_detail));
        initHeadBackView();
        hideSearchEditView();

        mHeadBtnRight.setText(getText(R.string.share));
        mHeadBtnRight.setOnClickListener(this);
        mListView=(ListView)findViewById(R.id.list);
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

    @Override
    public void loadData(){

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ItemView        holder;
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.home_listview_item,null);
                holder=new ItemView();
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
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
