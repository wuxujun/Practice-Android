package com.xujun.app.practice;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.Toast;

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
import com.xujun.util.UIHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class  TabActivity extends SherlockFragmentActivity implements View.OnClickListener{

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
    private ImageButton     mHeadBtnRight;
    private EditText        mHeadEditText;
    private LinearLayout    mHeadSearch;
    private LinearLayout    mHeadCategory;


    private CityPopupWindow mCityPopupWindow;
    private MenuPopupWindow mMenuPopupWindow;

    private List<CityInfo>  cityItems=new ArrayList<CityInfo>();

    private CityInfo        currentCity;

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
        actionbarLayout.findViewById(R.id.etHeadSearch).setOnClickListener(this);
        mHeadCategory=(LinearLayout)actionbarLayout.findViewById(R.id.llHeadCategory);
        actionbarLayout.findViewById(R.id.btnCategoryTab1).setOnClickListener(this);
        actionbarLayout.findViewById(R.id.btnCategoryTab2).setOnClickListener(this);
        actionbarLayout.findViewById(R.id.btnCategoryTab3).setOnClickListener(this);

        mHeadBtnRight=(ImageButton)actionbarLayout.findViewById(R.id.btnHeadRight);
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

        Intent  notify=new Intent(this,NotifyService.class);
        startService(notify);

        setSelect(0);
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
                mHeadBtnRight.setVisibility(View.VISIBLE);
                mHeadBtnRight.setImageResource(R.drawable.ic_head_menu);
                mHeadBtnLeft.setVisibility(View.VISIBLE);
                mHeadTitle.setVisibility(View.GONE);
                mHeadSearch.setVisibility(View.VISIBLE);
                mTvHome.setTextColor(getResources().getColor(R.color.app_blue));
                mImgHome.setImageResource(R.drawable.ic_tab_home_s);
                break;
            }
            case 1:{

                if (mTab02==null){
                    mTab02=new CategoryFragment();
                    transaction.add(R.id.content_frame,mTab02);
                }else{
                    transaction.attach(mTab02);
                }
                mHeadBtnRight.setVisibility(View.VISIBLE);
                mHeadBtnRight.setImageResource(R.drawable.ic_head_search);
                mHeadBtnLeft.setVisibility(View.INVISIBLE);
                mHeadCategory.setVisibility(View.VISIBLE);
                mHeadTitle.setVisibility(View.GONE);
                mTvCategory.setTextColor(getResources().getColor(R.color.app_blue));
                mImgCategory.setImageResource(R.drawable.ic_tab_category_s);
                break;
            }
            case 2:{

                if (mTab03==null){
                    mTab03=new AttentionFragment();
                    transaction.add(R.id.content_frame,mTab03);
                }else{
                    transaction.attach(mTab03);
                }
                mHeadBtnRight.setVisibility(View.VISIBLE);
                mHeadBtnRight.setImageResource(R.drawable.ic_head_search);
                mHeadBtnLeft.setVisibility(View.INVISIBLE);
                mHeadTitle.setText(getText(R.string.tab_attention));
                mTvAttention.setTextColor(getResources().getColor(R.color.app_blue));
                mImgAttention.setImageResource(R.drawable.ic_tab_attent_s);
                break;
            }
            case 3:{
                if (mTab04==null){
                    mTab04=new ResumeFragment();
                    transaction.add(R.id.content_frame,mTab04);
                }else{
                    transaction.attach(mTab04);
                }
                mHeadBtnRight.setVisibility(View.INVISIBLE);
                mHeadBtnLeft.setVisibility(View.INVISIBLE);
                mHeadTitle.setText(getText(R.string.tab_resume));
                mTvResume.setTextColor(getResources().getColor(R.color.app_blue));
                mImgResume.setImageResource(R.drawable.ic_tab_resume_s);
                break;
            }
            case 4:{
                if (mTab05==null){
                    mTab05=new MyFragment();
                    transaction.add(R.id.content_frame,mTab05);
                }else{
                    transaction.attach(mTab05);
                }
                mHeadBtnRight.setVisibility(View.VISIBLE);
                mHeadBtnRight.setImageResource(R.drawable.ic_head_menu);
                mHeadBtnLeft.setVisibility(View.INVISIBLE);
                mHeadTitle.setText(getText(R.string.tab_my));
                mTvMy.setTextColor(getResources().getColor(R.color.app_blue));
                mImgMy.setImageResource(R.drawable.ic_tab_my_s);
                break;
            }
        }
        transaction.commit();
    }

    private void resetTabImg(){
        mImgHome.setImageResource(R.drawable.ic_tab_home);
        mImgCategory.setImageResource(R.drawable.ic_tab_category);
        mImgAttention.setImageResource(R.drawable.ic_tab_attent);
        mImgResume.setImageResource(R.drawable.ic_tab_resume);
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
            case R.id.etHeadSearch:
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
                setHeadCateTextColor(0);
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.VISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.INVISIBLE);
                ((CategoryFragment)mTab02).reloadData("10");
                break;
            }
            case R.id.btnCategoryTab2:{
                setHeadCateTextColor(1);
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.VISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.INVISIBLE);
                ((CategoryFragment)mTab02).reloadData("20");
                break;
            }
            case R.id.btnCategoryTab3:{
                setHeadCateTextColor(2);
                actionbarLayout.findViewById(R.id.lineTab1).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab2).setVisibility(View.INVISIBLE);
                actionbarLayout.findViewById(R.id.lineTab3).setVisibility(View.VISIBLE);
                ((CategoryFragment)mTab02).reloadData("30");
                break;
            }
        }
    }

    private void setHeadCateTextColor(int idx){
        ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab1)).setTextColor(getResources().getColor(R.color.black));
        ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab2)).setTextColor(getResources().getColor(R.color.black));
        ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab3)).setTextColor(getResources().getColor(R.color.black));
        switch (idx){
            case 0:
                ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab1)).setTextColor(getResources().getColor(R.color.app_blue));
                break;
            case 1:
                ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab2)).setTextColor(getResources().getColor(R.color.app_blue));
                break;
            case 2:
                ((TextView)actionbarLayout.findViewById(R.id.tvCategoryTab3)).setTextColor(getResources().getColor(R.color.app_blue));
                break;
        }
    }

    private void showCityPopupWindow(){
        mHeadBtnLeftImg.setBackgroundResource(R.drawable.ic_arrow_up_s);
        mCityPopupWindow=new CityPopupWindow(this,mMoreCityClickListener);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        mCityPopupWindow.showAsDropDown(mHeadBtnLeft);
        mCityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mHeadBtnLeftImg.setBackgroundResource(R.drawable.ic_arrow_down_s);
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
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
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        mMenuPopupWindow.showAsDropDown(mHeadBtnRight);
        mMenuPopupWindow.getListView().setAdapter(new MenuItemAdapter(mContext, mAppConfig.getHeadMenu()));
        mMenuPopupWindow.getListView().setOnItemClickListener(mMenuItemListener);
        mMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
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
            switch (i){
                case 0:{
                    UIHelper.openShake(TabActivity.this);
                    break;
                }
            }
            mMenuPopupWindow.dismiss();
        }
    };

    private long exitTime=0;
    public boolean dispatchKeyEvent(KeyEvent event){
        int keyCode=event.getKeyCode();
        if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(TabActivity.this,getResources().getString(R.string.exit_tips),Toast.LENGTH_LONG).show();
                exitTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
