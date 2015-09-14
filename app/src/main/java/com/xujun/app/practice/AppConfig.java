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

    public final static String SMS_APPKEY="6de96e7d9e78";
    public final static String SMS_APPKSECRET="ee6510b139aa06a0a403dd4410646bef";

    public final static String WEIXIN_APPID="wx7b3c49ed03390fb2";
    public final static String WEIXIN_APPSECRET="232c9d206dbe83a905294ad00d0b8738";

    public final static String QQ_APPID="1104812756";
    public final static String QQ_APPSECRET="FMeRlwNSHJx1cqhx";

    public final static String WEIBO_APPID="2846029855";
    public final static String WEIBO_APPSECRET="6edd97b068547492519b8eefa084f344";


    public final static String DB_NAME="Practice";

    public final static String LOCAL_DATABASE_VERSION="LOCAL_DATABASE_VERSION";

    public final static String CONF_CURRENT_LATITUDE="conf_current_latitude";
    public final static String CONF_CURRENT_LONGITUDE="conf_current_longitude";


    public final static String PARAM_CITY_INFO="param_city_info";
    public final static String PARAM_CATEGORY_INFO="param_category_info";
    public final static String PARAM_MENU_INFO="param_menu_info";


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


    public final static int   REQUEST_ACCOUNT_FRAGMENT_TYPE_NORMAL=100;
    public final static int   REQUEST_ACCOUNT_FRAGMENT_TYPE_MANAGER=200;
    public final static int   REQUEST_ACCOUNT_FRAGMENT_TYPE_OTHER=300;

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

    public final static String LOCK="lock";
    public final static String LOCK_KEY="lock_key";

    public final static String HOME_TARGET_SHOW_INDEX_0="home_target_index_0";
    public final static String HOME_TARGET_SHOW_INDEX_1="home_target_index_1";
    public final static String HOME_TARGET_SHOW_INDEX_2="home_target_index_2";
    public final static String HOME_TARGET_SHOW_INDEX_3="home_target_index_3";
    public final static String HOME_TARGET_SHOW_INDEX_4="home_target_index_4";
    public final static String HOME_TARGET_SHOW_INDEX_5="home_target_index_5";
    public final static String HOME_TARGET_SHOW_INDEX_6="home_target_index_6";
    public final static String HOME_TARGET_SHOW_INDEX_7="home_target_index_7";
    public final static String HOME_TARGET_SHOW_INDEX_8="home_target_index_8";


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
        list.add(new MenuInfo(1));
        list.add(new MenuInfo(2));
        list.add(new MenuInfo(3));
        list.add(new MenuInfo(4));
        list.add(new MenuInfo(5));
        return list;
    }

}
