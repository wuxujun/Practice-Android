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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.xujun.app.model.LoginResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

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

                break;
            }
            case R.id.btnWeixin:{
                showCroutonMessage("微信登录开发中");
                break;
            }
            case R.id.btnWeibo:{

                break;
            }
            case R.id.btnLinkedin:{
                showCroutonMessage("LinkedIn登录开发中");
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

}

