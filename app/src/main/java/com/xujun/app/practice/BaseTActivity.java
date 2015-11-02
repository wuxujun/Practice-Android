package com.xujun.app.practice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.Member;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.SystemBarTintManager;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by xujunwu on 15/7/31.
 */
public abstract class BaseTActivity extends SherlockActivity implements View.OnClickListener{

    protected AppContext    mAppContext;
    protected Context       mContext;


    protected TextView          mHeadTitle;
    protected ImageButton       mHeadBack;
    protected LinearLayout      mHeadBtnLeft;
    protected Button            mHeadBtnRight;
    protected EditText mHeadEditText;

    protected LinearLayout      mHeadSearch;

    protected LinearLayout        mHeadCategory;
    protected LinearLayout        mHeadTab1;
    protected LinearLayout        mHeadTab2;
    protected LinearLayout        mHeadTab3;

    protected TextView mHeadTabText1;
    protected TextView mHeadTabText2;
    protected TextView mHeadTabText3;


    protected ListView      mListView;

    protected Member            mMember;

    protected CityInfo          mCurrentCityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext=(AppContext)getApplication();
        mContext=getApplicationContext();
        mMember=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager=new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.action_bar_color);
        }

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        View actionbarLayout= LayoutInflater.from(this).inflate(R.layout.custom_actionbar,null);
        mHeadTitle=(TextView)actionbarLayout.findViewById(R.id.tvHeadTitle);
        mHeadBack=(ImageButton)actionbarLayout.findViewById(R.id.ibHeadBack);
        mHeadBack.setVisibility(View.VISIBLE);
        actionbarLayout.findViewById(R.id.tvHeadCity).setVisibility(View.GONE);
        actionbarLayout.findViewById(R.id.ivHeadArrow).setVisibility(View.GONE);
        mHeadBtnLeft=(LinearLayout)actionbarLayout.findViewById(R.id.btnHeadLeft);
        mHeadBtnRight=(Button)actionbarLayout.findViewById(R.id.btnHeadRight);
        mHeadSearch=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadSearch);
        mHeadEditText=(EditText)actionbarLayout.findViewById(R.id.etHeadSearch);
        mHeadCategory=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadCategory);
        mHeadTab1=(LinearLayout)actionbarLayout.findViewById(R.id.btnCategoryTab1);
        mHeadTab2=(LinearLayout)actionbarLayout.findViewById(R.id.btnCategoryTab2);
        mHeadTab3=(LinearLayout)actionbarLayout.findViewById(R.id.btnCategoryTab3);

        mHeadTabText1=(TextView)actionbarLayout.findViewById(R.id.tvCategoryTab1);
        mHeadTabText2=(TextView)actionbarLayout.findViewById(R.id.tvCategoryTab2);
        mHeadTabText3=(TextView)actionbarLayout.findViewById(R.id.tvCategoryTab3);

        getActionBar().setCustomView(actionbarLayout);
    }

    public void initHeadBackView(){

        mHeadBack.setOnClickListener(this);
    }

    public void showSearchEditView(){
        mHeadTitle.setVisibility(View.GONE);
        mHeadSearch.setVisibility(View.VISIBLE);
    }

    public void hideSearchEditView(){
        mHeadTitle.setVisibility(View.VISIBLE);
        mHeadSearch.setVisibility(View.GONE);
    }


    public boolean dispatchKeyEvent(KeyEvent event){
        int keyCode=event.getKeyCode();
        if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public RequestParams getRequestParams(Map<String,Object> maps){
        RequestParams params=new RequestParams();
        maps.put("imei",mAppContext.getIMSI());
        maps.put("umeng_token", UmengRegistrar.getRegistrationId(mContext));
        if (mMember!=null){
            maps.put("mid", mMember.getId());
        }
        try{
            String json= JsonUtil.toJson(maps);
            L.e("RequestParams===>" + json);
            params.addBodyParameter("content",json);
//            params.setBodyEntity(new StringEntity(json,"utf-8"));
        }catch (JSONException e){
            e.printStackTrace();
        }
//        catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
        return  params;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    public abstract void loadData();
    public abstract void parserHttpResponse(String result);

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHeadBack:{
                finish();
                break;
            }
        }
    }

    public String getFromAssets(String fileName){
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[]  buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static final Style INFINITE=new Style.Builder().setBackgroundColorValue(Style.holoBlueLight).build();
    public static final Configuration CONFIGURATION=new Configuration.Builder().setDuration(Configuration.DURATION_SHORT).build();
    public void showCroutonMessage(String message){
        final Crouton crouton;
        crouton=Crouton.makeText(this,message,INFINITE);
        crouton.setConfiguration(CONFIGURATION).show();
    }
}
