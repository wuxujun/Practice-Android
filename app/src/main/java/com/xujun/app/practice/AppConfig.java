package com.xujun.app.practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xujun.app.model.MenuInfo;
import com.xujun.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xujunwu on 14/12/20.
 */
public class AppConfig {
    private final static String APP_CONFIG="config";

    public final static int    SUCCESS=1000;
    public final static int    FAILED=2000;
    public final static int    CANCEL=3000;

    public final static String  ACTION_ALARM="com.xujun.app.practice.alarm.action";

    public final static String SMS_APPKEY="6de96e7d9e78";
    public final static String SMS_APPKSECRET="ee6510b139aa06a0a403dd4410646bef";

    public final static String WEIXIN_APPID="wx7b3c49ed03390fb2";
    public final static String WEIXIN_APPSECRET="232c9d206dbe83a905294ad00d0b8738";

    public final static String QQ_APPID="1104812756";
    public final static String QQ_APPSECRET="FMeRlwNSHJx1cqhx";

    public final static String WEIBO_APPID="2360797310";
    public final static String WEIBO_APPSECRET="365b9df92cf488cee45622dd14231394";


    public final static String DB_NAME="Practice";

    public final static String LOCAL_DATABASE_VERSION="LOCAL_DATABASE_VERSION";

    public final static String CONF_CURRENT_LATITUDE="conf_current_latitude";
    public final static String CONF_CURRENT_LONGITUDE="conf_current_longitude";

    public final static String CONF_THRID_LOGIN_GENDER="conf_third_login_gender";
    public final static String CONF_THRID_LOGIN_CITY="conf_third_login_city";
    public final static String CONF_THRID_LOGIN_USER_TYPE="conf_third_login_user_type";
    public final static String CONF_THRID_LOGIN_UNIONID="conf_third_login_unionid"; //weixin
    public final static String CONF_THRID_LOGIN_OPENID="conf_third_login_openid"; //weixin


    public final static String PARAM_LOGIN_SOURCE="param_login_source";
    public final static String PARAM_CITY_INFO="param_city_info";
    public final static String PARAM_CATEGORY_INFO="param_category_info";
    public final static String PARAM_MENU_INFO="param_menu_info";
    public final static String PARAM_OFFICE_INFO="param_office_info";
    public final static String PARAM_MEMBER="param_member";
    public final static String PARAM_LOGOUT_RESULT="param_logout_result";

    public final static String EXTRA_DATA_HEIGHT="com.xujun.yoca.EXTRA_DATA_HEIGHT";
    public final static String EXTRA_DATA_AGE="com.xujun.yoca.EXTRA_DATA_AGE";
    public final static String EXTRA_DATA_SEX="com.xujun.yoca.EXTRA_DATA_SEX";
    public final static String EXTRA_DATA_UNIT="com.xujun.yoca.EXTRA_DATA_UNIT";

    public final static int   SEX_FAMALE=0;
    public final static int   SEX_MALE=1;
    public final static int   UNIT_KG=1;
    public final static int   UNIT_LB=2;

    public final static int    REQUEST_CITY_SELECTED=1;

    public final static int    REQUEST_TAKE_PHOTO=3;
    public final static int    REQUEST_CHOOSE_PIC=4;
    public final static int    REQUEST_CROP_PHOTO=5;
    public final static int    REQUEST_SWITCH_ACCOUNT=6;
    public final static int    REQUEST_RESUME_LOGIN=7;


    public final static int   REQUEST_MY_LOGIN=100;
    public final static int   REQUEST_MY_REGISTER=200;
    public final static int   REQUEST_ATTENTION_SET=300;
    public final static int   REQUEST_MY_SETTING=400;

    public final static int   REQUEST_RESUME_WORK_ADD=10;
    public final static int   REQUEST_RESUME_LIFE_ADD=11;
    public final static int   REQUEST_RESUME_HONOR_ADD=12;

    public final static int   REQUEST_IMAGE=20;

    public final static int   LOGIN_TYPE_MY=10;
    public final static int   LOGIN_TYPE_RESUME=20;
    public final static int   LOGIN_TYPE_OFFICE=30;


    public final static String DATA_VERSION= "data_version";
    public final static String CONF_APP_UNIQUEID="APP_UNIQUEID";
    public final static String CONF_COOKIE="cookie";

    public final static String USER_AUTO_LOGIN="pre_auto_login";
    public final static String USER_LOCK_PASS="pre_user_lock";
    public final static String USER_LOCK_TYPE="pre_user_lock_type";

    public final static String DEVICE_SET_SHOW_MODEL="pre_device_show_model";
    public final static String DEVICE_SET_WEIGHT_MODEL="pre_device_weight_model";
    public final static String DEVICE_SET_SHOW_UNIT="pre_device_show_unit";
    public final static String DEVICE_SET_LED_LEVEL="pre_device_led_level";

    public final static String CONF_LOGIN_ACCOUNT="conf_login_account";
    public final static String CONF_LOGIN_PASSWORD="conf_login_password";

    public final static String CONF_LOGIN_FIRST="conf_login_first";
    public final static String CONF_LOGIN_FLAG="conf_login_flag";

    public final static String  OBJECT_MEMBER="file_member";

    public final static int     DATA_TYPE_COMPANY=1;
    public final static int     DATA_TYPE_COMPANY_LOGO=2;
    public final static int     DATA_TYPE_CATEGORY=3;



    private Context mContext;
    private static AppConfig    appConfig;

    public static AppConfig getAppConfig(Context context){
        if (appConfig==null){
            appConfig=new AppConfig();
            appConfig.mContext=context;
        }
        return appConfig;
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getCookie(){
        return get(CONF_COOKIE);
    }

    public String get(String key){
        Properties props=get();
        return (props!=null)?props.getProperty(key):null;
    }

    public Properties get(){
        FileInputStream fis=null;
        Properties props=new Properties();
        try {
            File dirConf=mContext.getDir(APP_CONFIG,Context.MODE_PRIVATE);
            fis=new FileInputStream(dirConf.getPath()+File.separator+APP_CONFIG);
            props.load(fis);
        }catch (Exception e){

        }finally {
            try {
                fis.close();
            }catch (Exception e){

            }
        }
        return props;
    }

    private void setProps(Properties p){
        FileOutputStream fos=null;
        try {
            File dirConf=mContext.getDir(APP_CONFIG,Context.MODE_PRIVATE);
            File conf=new File(dirConf,APP_CONFIG);
            fos=new FileOutputStream(conf);
            p.store(fos,null);
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            }catch (Exception e){

            }
        }
    }

    public void set(Properties ps){
        Properties props=get();
        props.putAll(ps);
        setProps(props);
    }
    public void set(String key,String value){
        Properties props=get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key){
        Properties props=get();
        for (String k:key){
            props.remove(k);
        }
        setProps(props);
    }


    public List<MenuInfo> getHeadMenu(){
        List<MenuInfo> list=new ArrayList<MenuInfo>();
        list.add(new MenuInfo(1,"摇一摇"));
        list.add(new MenuInfo(2,"扫一扫"));
        list.add(new MenuInfo(3,"消息中心"));
        list.add(new MenuInfo(4,"邀请好友"));
        list.add(new MenuInfo(5, "分享"));
        return list;
    }

    public List<MenuInfo> getPhotoMenu(){
        List<MenuInfo> list=new ArrayList<MenuInfo>();
        list.add(new MenuInfo(1,"拍照"));
        list.add(new MenuInfo(2,"上传"));
        return list;
    }

}
