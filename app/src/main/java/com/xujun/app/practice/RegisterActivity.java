package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andreabaccega.formedittextvalidator.EmailValidator;
import com.andreabaccega.formedittextvalidator.EmptyValidator;
import com.andreabaccega.formedittextvalidator.OrValidator;
import com.andreabaccega.formedittextvalidator.PhoneValidator;
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
import com.xujun.util.URLs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/7.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.etMobile)
    private FormEditText    mobileET;
    @ViewInject(R.id.etEmail)
    private FormEditText    emailET;
    @ViewInject(R.id.etPassword)
    private FormEditText    passwdET;
    @ViewInject(R.id.etPassword2)
    private FormEditText    passwordET;

    @ViewInject(R.id.btnRegister)
    private Button          registerBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);

        mHeadTitle.setText(getText(R.string.register));
        mHeadBtnRight.setText(getText(R.string.clear));
        mHeadBtnRight.setOnClickListener(this);
        initHeadBackView();
        hideSearchEditView();
        initUI();
    }

    private void initUI(){
        mobileET.addValidator(new OrValidator(getString(R.string.input_mobile_hint), new PhoneValidator(null)));
        emailET.addValidator(new OrValidator(getString(R.string.input_email_hint), new EmailValidator(null)));
        passwordET.addValidator(new EmptyValidator(null));
        passwdET.addValidator(new EmptyValidator(null));

        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{
                mobileET.setText("");
                emailET.setText("");
                passwdET.setText("");
                passwordET.setText("");
                break;
            }
            case R.id.btnRegister:{
                register();
                break;
            }
            default:
                super.onClick(view);
                break;
        }
    }

    private void register(){
        if(!mobileET.testValidity()){
            return;
        }
        if (!emailET.testValidity()){
            return;
        }
        if (!passwordET.testValidity()){
            return;
        }
        if (!passwdET.testValidity()){
            return;
        }

        String mobile=mobileET.getText().toString();
        String email=emailET.getText().toString();
        String passwd=passwdET.getText().toString();
        String password=passwordET.getText().toString();
        if (!passwd.equals(password)){
            showCroutonMessage("两次输入密码不一致!");
            return;
        }

        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("mobile",mobile);
        requestMap.put("email", email);
        requestMap.put("password",password);

        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.REGISTER_USER, getRequestParams(requestMap), new RequestCallBack<String>() {
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
            LoginResp resp=(LoginResp) JsonUtil.ObjFromJson(result,LoginResp.class);
            if (resp.getIsExist()==1){
                showCroutonMessage("用户已存在.");
            }else{
                if (resp.getData()!=null){
                    mAppContext.saveObject(resp.getData(),AppConfig.OBJECT_MEMBER);
                    mAppContext.setProperty(AppConfig.CONF_LOGIN_FLAG, "1");
                    mAppContext.setProperty(AppConfig.CONF_LOGIN_ACCOUNT,mobileET.getText().toString());
                    mAppContext.setProperty(AppConfig.CONF_LOGIN_PASSWORD,passwordET.getText().toString());
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(AppConfig.PARAM_MEMBER,resp.getData());
                    intent.putExtras(bundle);
                    RegisterActivity.this.setResult(AppConfig.SUCCESS, intent);
                    RegisterActivity.this.finish();
                }else{
                    showCroutonMessage("注册失败,请重试");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
