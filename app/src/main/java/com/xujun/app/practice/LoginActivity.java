package com.xujun.app.practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.formedittextvalidator.EmptyValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.widget.FormEditText;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.umeng.message.UmengRegistrar;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.xujun.app.model.LoginResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.etMobile)
    private FormEditText    mobileET;
    @ViewInject(R.id.etPassword)
    private FormEditText    passwordET;
    @ViewInject(R.id.btnLogin)
    private Button          loginBtn;

    @ViewInject(R.id.btnQQ)
    private Button          qqBtn;
    @ViewInject(R.id.btnWeixin)
    private Button          weixinBtn;
    @ViewInject(R.id.btnWeibo)
    private Button          weiboBtn;
    @ViewInject(R.id.btnLinkedin)
    private Button          linkedinBtn;

    private int             sourceType=10;


    private UMSocialService mController= UMServiceFactory.getUMSocialService("com.umeng.login");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sourceType=getIntent().getIntExtra(AppConfig.PARAM_LOGIN_SOURCE,AppConfig.LOGIN_TYPE_MY);
        ViewUtils.inject(this);
        mHeadTitle.setText(getText(R.string.login));
        mHeadBtnRight.setText(getText(R.string.register));
        mHeadBtnRight.setOnClickListener(this);
        hideSearchEditView();
        initHeadBackView();

        initUI();
    }

    private void initUI(){
        mobileET.addValidator(new EmptyValidator(null));
        passwordET.addValidator(new EmptyValidator(null));
        loginBtn.setOnClickListener(this);

        qqBtn.setOnClickListener(this);
        weixinBtn.setOnClickListener(this);
        weiboBtn.setOnClickListener(this);
        linkedinBtn.setOnClickListener(this);

        String mobile=mAppContext.getProperty(AppConfig.CONF_LOGIN_ACCOUNT);
        if (!StringUtil.isEmpty(mobile)){
            mobileET.setText(mobile);
        }
        String password=mAppContext.getProperty(AppConfig.CONF_LOGIN_PASSWORD);
        if (!StringUtil.isEmpty(password)){
            passwordET.setText(password);
        }
        initPlatforms();
    }

    private void initPlatforms()
    {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this, AppConfig.QQ_APPID,
                AppConfig.QQ_APPSECRET);
        qqSsoHandler.addToSocialSDK();

        UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this,AppConfig.WEIXIN_APPID,AppConfig.WEIXIN_APPSECRET);
        wxHandler.addToSocialSDK();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());


    }

    private static Scope buildScope(){
        return Scope.build(Scope.R_BASICPROFILE,Scope.W_SHARE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnLogin:{
                login();
                break;
            }
            case R.id.btnQQ:{
                qqLogin();
                break;
            }
            case R.id.btnWeixin:{
                weixinLogin();
                break;
            }
            case R.id.btnWeibo:{
                weiboLogin();
                break;
            }
            case R.id.btnLinkedin:{

                LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        L.e("Success");
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        L.e("Failed "+error.toString());
                    }
                }, true);

                break;
            }
            default:
                super.onClick(view);
                break;
        }
    }

    private void login(){
        if (!mobileET.testValidity()){
            return;
        }
        if (!passwordET.testValidity()){
            return;
        }
        String mobile=mobileET.getText().toString();
        String password=passwordET.getText().toString();

        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("mobile",mobile);
        requestMap.put("password",password);

        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.LOGIN_VALIDATE_HTTP, getRequestParams(requestMap), new RequestCallBack<String>() {
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
    public void loadData(){

    }

    @Override
    public void parserHttpResponse(String result) {
        try{
            LoginResp resp=(LoginResp) JsonUtil.ObjFromJson(result, LoginResp.class);
            if (resp.getData()!=null){
                mAppContext.saveObject(resp.getData(),AppConfig.OBJECT_MEMBER);
                mAppContext.setProperty(AppConfig.CONF_LOGIN_FLAG, "1");
                mAppContext.setProperty(AppConfig.CONF_LOGIN_ACCOUNT, mobileET.getText().toString());
                mAppContext.setProperty(AppConfig.CONF_LOGIN_PASSWORD,passwordET.getText().toString());

                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConfig.PARAM_MEMBER,resp.getData());
                intent.putExtras(bundle);
                LoginActivity.this.setResult(AppConfig.SUCCESS, intent);
                LoginActivity.this.finish();
            }else{
                showCroutonMessage("用户登录失败.");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        LISessionManager.getInstance(mContext).onActivityResult(this,requestCode,resultCode,data);
    }


    private void weixinLogin(){
        mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Toast.makeText(LoginActivity.this, "微信登录请求中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            L.e("weixin OnCompelte() " + info.toString() + "");
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_GENDER, info.get("sex").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_CITY, info.get("city").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_USER_TYPE, "1");
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_UNIONID, info.get("unionid").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_OPENID, info.get("openid").toString());
                            requestLogin("1", info.get("openid").toString(), info.get("nickname").toString(), info.get("headimgurl").toString());
                        } else {
                            Toast.makeText(LoginActivity.this, "发生错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                L.e("Weibo Login onError " + e.getMessage());
                Toast.makeText(LoginActivity.this, "微信登录发生错误..." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                L.e("Weibo Login onCancel " + share_media.toString());
                Toast.makeText(LoginActivity.this, "微信登录已取消", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void qqLogin(){
        mController.doOauthVerify(LoginActivity.this,SHARE_MEDIA.QQ,new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                L.e("QQ onStart().....");
                Toast.makeText(LoginActivity.this,"QQ登录请求中...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                mController.getPlatformInfo(LoginActivity.this,SHARE_MEDIA.QQ,new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(LoginActivity.this,"获取平台数据开始...",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status==200&&info!=null){
                            L.e("onComplete() " + info.toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_GENDER, info.get("gender").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_CITY, info.get("city").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_USER_TYPE, "2");
                            requestLogin("2","qq123456", info.get("screen_name").toString(), info.get("profile_image_url").toString());
                        }else{
                            Toast.makeText(LoginActivity.this,"发生错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                Toast.makeText(LoginActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(LoginActivity.this,"QQ登录已取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void weiboLogin(){
        mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA,new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                L.e("Weibo login start...");
                Toast.makeText(LoginActivity.this,"微博登录请求中...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                mController.getPlatformInfo(LoginActivity.this,SHARE_MEDIA.SINA,new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(LoginActivity.this,"获取平台数据开始...",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status==200&&info!=null){
                            L.e("..."+info.toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_GENDER, info.get("gender").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_CITY, info.get("location").toString());
                            mAppContext.setProperty(AppConfig.CONF_THRID_LOGIN_USER_TYPE, "3");
                            requestLogin("3",info.get("uid").toString(), info.get("screen_name").toString(), info.get("profile_image_url").toString());
                        }else{
                            L.e("发生错误." + status);
                        }
                    }
                });
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                Toast.makeText(LoginActivity.this,"微博登录发生错误..."+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(LoginActivity.this,"微博登录已取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestLogin(String userType,String openid,String nick,String avatar){

        Map<String,String> sb=new HashMap<String, String>();
        sb.put("imei",mAppContext.getIMSI());
        sb.put("umengToken", UmengRegistrar.getRegistrationId(mContext));
        sb.put("userNick",nick);
        sb.put("openid",openid);
        sb.put("avatar",avatar);
        sb.put("userType", userType);
        sb.put("mobile", "");

    }
}

