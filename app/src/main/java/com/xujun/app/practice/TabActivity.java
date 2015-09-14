package com.xujun.app.practice;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.cache.LruDiskCache;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
import com.xujun.app.adapter.CityItemAdapter;
import com.xujun.app.adapter.MenuItemAdapter;
import com.xujun.app.fragment.HomeFragment;
import com.xujun.app.fragment.MyFragment;
import com.xujun.app.fragment.AttentionFragment;
import com.xujun.app.fragment.CategoryFragment;
import com.xujun.app.fragment.ResumeFragment;
import com.xujun.app.model.CityInfo;
import com.xujun.app.widget.CityPopupWindow;
import com.xujun.app.widget.MenuPopupWindow;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class TabActivity extends SherlockFragmentActivity implements View.OnClickListener,AMapLocationListener{

    private static final String TAG="TabActivity";

    private Context     mContext;
    private AppContext  mAppContext;
    private AppConfig   mAppConfig;

    @ViewInject(R.id.ll_tab_home)
    private LinearLayout        mTabHome;
    @ViewInject(R.id.ll_tab_category)
    private LinearLayout        mTabCategory;
    @ViewInject(R.id.ll_tab_attention)
    private LinearLayout        mTabAttention;
    @ViewInject(R.id.ll_tab_resume)
    private LinearLayout        mTabResume;
    @ViewInject(R.id.ll_tab_my)
    private LinearLayout        mTabMy;

    @ViewInject(R.id.ib_tab_home)
    private ImageButton         mImgHome;
    @ViewInject(R.id.ib_tab_category)
    private ImageButton         mImgCategory;
    @ViewInject(R.id.ib_tab_attention)
    private ImageButton         mImgAttention;
    @ViewInject(R.id.ib_tab_resume)
    private ImageButton         mImgResume;
    @ViewInject(R.id.ib_tab_my)
    private ImageButton         mImgMy;

    @ViewInject(R.id.tv_tab_home)
    private TextView            mTvHome;
    @ViewInject(R.id.tv_tab_category)
    private TextView            mTvCategory;
    @ViewInject(R.id.tv_tab_attention)
    private TextView            mTvAttention;
    @ViewInject(R.id.tv_tab_resume)
    private TextView            mTvResume;
    @ViewInject(R.id.tv_tab_my)
    private TextView            mTvMy;

    private SherlockFragment    mTab01;
    private SherlockFragment    mTab02;
    private SherlockFragment    mTab03;
    private SherlockFragment    mTab04;
    private SherlockFragment    mTab05;

    private View            actionbarLayout;
    private TextView        mHeadTitle;
    private LinearLayout    mHeadBtnLeft;
    private TextView        mHeadBtnLeftTxt;
    private ImageView       mHeadBtnLeftImg;
    private Button          mHeadBtnRight;
    private EditText        mHeadEditText;
    private LinearLayout    mHeadSearch;
    private LinearLayout    mHeadCategory;


    private CityPopupWindow mCityPopupWindow;
    private MenuPopupWindow mMenuPopupWindow;

    private List<CityInfo>  cityItems=new ArrayList<CityInfo>();

    private CityInfo        currentCity;

    private LocationManagerProxy    mLocationManagerProxy;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_tab);
        mAppContext=(AppContext)getApplication();
        mContext=getApplicationContext();
        mAppConfig=AppConfig.getAppConfig(mContext);

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

        actionbarLayout= LayoutInflater.from(this).inflate(R.layout.custom_actionbar,null);
        mHeadTitle=(TextView)actionbarLayout.findViewById(R.id.tvHeadTitle);
        mHeadEditText=(EditText)actionbarLayout.findViewById(R.id.etHeadSearch);
        mHeadBtnLeft=(LinearLayout)actionbarLayout.findViewById(R.id.btnHeadLeft);
        mHeadBtnLeft.setOnClickListener(this);
        mHeadBtnLeftTxt=(TextView)actionbarLayout.findViewById(R.id.tvHeadCity);
        mHeadBtnLeftImg=(ImageView)actionbarLayout.findViewById(R.id.ivHeadArrow);
        mHeadSearch=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadSearch);
        mHeadSearch.setOnClickListener(this);
        mHeadCategory=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadCategory);
        actionbarLayout.findViewById(R.id.btnCategoryTab1).setOnClickListener(this);
        actionbarLayout.findViewById(R.id.btnCategoryTab2).setOnClickListener(this);
        actionbarLayout.findViewById(R.id.btnCategoryTab3).setOnClickListener(this);

        mHeadBtnRight=(Button)actionbarLayout.findViewById(R.id.btnHeadRight);
        mHeadBtnRight.setText("菜单");
        mHeadBtnRight.setOnClickListener(this);
        getActionBar().setCustomView(actionbarLayout);

        ViewUtils.inject(this);

        mTabHome.setOnClickListener(this);
        mTabCategory.setOnClickListener(this);
        mTabAttention.setOnClickListener(this);
        mTabResume.setOnClickListener(this);
        mTabMy.setOnClickListener(this);

        UmengUpdateAgent.update(this);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);

        PushAgent pushAgent=PushAgent.getInstance(mContext);
        pushAgent.enable();

        initMapLocation();
        setSelect(0);
    }

    private void initMapLocation(){
        mLocationManagerProxy=LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
        mLocationManagerProxy.setGpsEnable(false);

    }

    @Override
    protected void onResume(){
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mContext);
        L.e("Umeng_Token:" + UmengRegistrar.getRegistrationId(mContext));
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mContext);
    }

    private void reloadData(){
        ((HomeFragment)mTab01).setCityInfo(currentCity);
        ((CategoryFragment)mTab02).setCityInfo(currentCity);
        ((AttentionFragment)mTab03).setCityInfo(currentCity);
        ((ResumeFragment)mTab04).setCityInfo(currentCity);
        ((MyFragment)mTab05).setCityInfo(currentCity);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==AppConfig.REQUEST_CITY_SELECTED){
            if (resultCode==RESULT_OK){
                CityInfo cityInfo=(CityInfo)data.getSerializableExtra(AppConfig.PARAM_CITY_INFO);
                if (cityInfo!=null){
                    mHeadBtnLeftTxt.setText(cityInfo.getCityName());
                    currentCity=cityInfo;
                    reloadData();
                }
            }
        }
    }

    private void setSelect(int type){
        resetTabImg();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragment(transaction);
        switch (type){
            case 0:{
                if (mTab01==null){
                    mTab01=new HomeFragment();
                    transaction.add(R.id.content_frame,mTab01);
                }else{
                    transaction.attach(mTab01);
                }
                mHeadTitle.setVisibility(View.GONE);
                mHeadSearch.setVisibility(View.VISIBLE);
                mTvHome.setTextColor(getResources().getColor(R.color.white));
                mImgHome.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 1:{

                if (mTab02==null){
                    mTab02=new CategoryFragment();
                    transaction.add(R.id.content_frame,mTab02);
                }else{
                    transaction.attach(mTab02);
                }
                mHeadCategory.setVisibility(View.VISIBLE);
                mHeadTitle.setVisibility(View.GONE);
                mTvCategory.setTextColor(getResources().getColor(R.color.white));
                mImgCategory.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 2:{

                if (mTab03==null){
                    mTab03=new AttentionFragment();
                    transaction.add(R.id.content_frame,mTab03);
                }else{
                    transaction.attach(mTab03);
                }
                mHeadTitle.setText(getText(R.string.tab_attention));
                mTvAttention.setTextColor(getResources().getColor(R.color.white));
                mImgAttention.setImageResource(R.drawable.ic_tab_home);
                break;
            }
            case 3:{
                if (mTab04==null){
                    mTab04=new ResumeFragment();
                    transaction.add(R.id.content_frame,mTab04);
                }else{
                    transaction.attach(mTab04);
                }
                mHeadTitle.setText(getText(R.string.tab_resume));
                mTvResume.setTextColor(getResources().getColor(R.color.white));
                mImgResume.setImageResource(R.drawable.ic_tab_my);
                break;
            }
            case 4:{
                if (mTab05==null){
                    mTab05=new MyFragment();
                    transaction.add(R.id.content_frame,mTab05);
                }else{
                    transaction.attach(mTab05);
                }
                mHeadTitle.setText(getText(R.string.tab_my));
                mTvMy.setTextColor(getResources().getColor(R.color.white));
                mImgMy.setImageResource(R.drawable.ic_tab_my);
                break;
            }
        }
        transaction.commit();
    }

    private void resetTabImg(){
        mImgHome.setImageResource(R.drawable.ic_tab_home);
        mImgCategory.setImageResource(R.drawable.ic_tab_home);
        mImgAttention.setImageResource(R.drawable.ic_tab_home);
        mImgResume.setImageResource(R.drawable.ic_tab_home);
        mImgMy.setImageResource(R.drawable.ic_tab_my);

        mTvHome.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvCategory.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvAttention.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvResume.setTextColor(getResources().getColor(R.color.app_listtab_off));
        mTvMy.setTextColor(getResources().getColor(R.color.app_listtab_off));

        mHeadSearch.setVisibility(View.GONE);
        mHeadCategory.setVisibility(View.GONE);
        mHeadTitle.setVisibility(View.VISIBLE);
    }

    private void hideFragment(FragmentTransaction ft){
        if (mTab01!=null){
            ft.detach(mTab01);
        }
        if (mTab02!=null){
            ft.detach(mTab02);
        }
        if (mTab03!=null){
            ft.detach(mTab03);
        }
        if (mTab04!=null){
            ft.detach(mTab04);
        }
        if (mTab05!=null){
            ft.detach(mTab05);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_tab_home:
                setSelect(0);
                break;
            case R.id.ll_tab_category:
                setSelect(1);
                break;
            case R.id.ll_tab_attention:
                setSelect(2);
                break;
            case R.id.ll_tab_resume:
                setSelect(3);
                break;
            case R.id.ll_tab_my:
                setSelect(4);
                break;
            case R.id.btnHeadLeft:
            {
                showCityPopupWindow();
                break;
            }
            case R.id.llHeadSearch:{
                Intent intent=new Intent(TabActivity.this,SearchActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConfig.PARAM_CITY_INFO,currentCity);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.btnHeadRight:{
                showMenuPopupWindow();
                break;
            }
            case R.id.btnCategoryTab1:{
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.VISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.INVISIBLE);
                ((CategoryFragment)mTab02).reloadData("10");
                break;
            }
            case R.id.btnCategoryTab2:{
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.VISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.INVISIBLE);
                ((CategoryFragment)mTab02).reloadData("20");
                break;
            }
            case R.id.btnCategoryTab3:{
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.VISIBLE);
                ((CategoryFragment)mTab02).reloadData("30");
                break;
            }
        }
    }

    private void showCityPopupWindow(){
        mHeadBtnLeftImg.setBackgroundResource(R.drawable.arrow_up_white);
        mCityPopupWindow=new CityPopupWindow(this,mMoreCityClickListener);
        mCityPopupWindow.showAsDropDown(mHeadBtnLeft);
        mCityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mHeadBtnLeftImg.setBackgroundResource(R.drawable.arrow_down_white);
            }
        });
        updateCityPopupData();
    }

    private void updateCityPopupData(){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.configAllowTransaction(true);
            db.configDebug(true);
            List<CityInfo> cityInfoList=db.findAll(Selector.from(CityInfo.class).where("top","=",1));
            if (cityInfoList!=null&&cityInfoList.size()>0){
                cityItems.clear();
                cityItems.addAll(cityInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mCityPopupWindow.getGridView().setAdapter(new CityItemAdapter(this,cityItems,R.layout.item_city));
        mCityPopupWindow.getGridView().setOnItemClickListener(mCityItemListener);
        if (currentCity!=null){
            mCityPopupWindow.getCurrentCityTextView().setText(currentCity.getCityName());
        }
    }

    private void showMenuPopupWindow(){
        mMenuPopupWindow=new MenuPopupWindow(this);
        mMenuPopupWindow.showAsDropDown(mHeadBtnRight);
        mMenuPopupWindow.getListView().setAdapter(new MenuItemAdapter(mContext, mAppConfig.getHeadMenu()));
        mMenuPopupWindow.getListView().setOnItemClickListener(mMenuItemListener);
    }



    private View.OnClickListener mMoreCityClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.more_city_linearlayout:{
                    Intent intent=new Intent(TabActivity.this,CityActivity.class);
                    startActivityForResult(intent,AppConfig.REQUEST_CITY_SELECTED);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener mCityItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CityInfo cityInfo=cityItems.get(position);
            if (cityInfo!=null){
                mHeadBtnLeftTxt.setText(cityInfo.getCityName());
                currentCity=cityInfo;
            }
            mCityPopupWindow.dismiss();
        }
    };

    /***
     * 弹出菜单事作
     */
    private AdapterView.OnItemClickListener mMenuItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    /****
     * 定位信息
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getAMapException().getErrorCode()==0){
            Double geoLat=aMapLocation.getLatitude();
            Double geoLng=aMapLocation.getLongitude();
            mAppContext.setProperty(AppConfig.CONF_CURRENT_LATITUDE,StringUtil.doubleToString(geoLat));
            mAppContext.setProperty(AppConfig.CONF_CURRENT_LONGITUDE,StringUtil.doubleToString(geoLng));

            L.e(""+aMapLocation.getCityCode()+"  "+aMapLocation.getCity()+"  "+aMapLocation.getAdCode()+"  "+aMapLocation.getCountry());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
