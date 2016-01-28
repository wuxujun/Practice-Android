package com.xujun.app.practice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.CategoryCheckBoxAdapter;
import com.xujun.app.adapter.CityItemAdapter;
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.adapter.ParamCheckBoxAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.OfficeResp;
import com.xujun.app.model.ParamInfo;
import com.xujun.app.widget.CategoryPopupWindow;
import com.xujun.app.widget.CityPopupWindow;
import com.xujun.app.widget.DistancePopupWindow;
import com.xujun.app.widget.RewardPopupWindow;
import com.xujun.app.widget.SequenceHeadView;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/4.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private CategoryInfo        categoryInfo;

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeadLinearLayout;

    @ViewInject(R.id.list)
    private ListView            mListView;

    @ViewInject(R.id.llMask)
    private LinearLayout        mMaskView;

    private SequenceHeadView    headView;
    /**
     * 分类
     */
    private CategoryPopupWindow mCategoryPopupWindow;

    /**
     * 报酬
     */
    private RewardPopupWindow   mRewardPopupWindow;
    /**
     * 距离
     */
    private DistancePopupWindow mDistancePopupWindow;


    private OfficeAdapter       mAdapter;
    private List<OfficeInfo>    items=new ArrayList<OfficeInfo>();

    private List<ParamInfo>      paramInfos=new ArrayList<ParamInfo>();
    private List<CategoryInfo>   categoryInfos=new ArrayList<CategoryInfo>();

    private View.OnClickListener  mRewardClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if (mRewardPopupWindow!=null){
                mRewardPopupWindow.getHourButton().setTextColor(getResources().getColor(R.color.btn_color));
                mRewardPopupWindow.getHourButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
                mRewardPopupWindow.getDayButton().setTextColor(getResources().getColor(R.color.btn_color));
                mRewardPopupWindow.getDayButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
                mRewardPopupWindow.getMonthButton().setTextColor(getResources().getColor(R.color.btn_color));
                mRewardPopupWindow.getMonthButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
            }
            switch (view.getId()){
                case R.id.btnRewardHour:
                    mRewardPopupWindow.getHourButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mRewardPopupWindow.getHourButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateRewardData(1);
                    break;
                case R.id.btnRewardDay:{
                    mRewardPopupWindow.getDayButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mRewardPopupWindow.getDayButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateRewardData(2);
                    break;
                }
                case R.id.btnRewardMonth:{
                    mRewardPopupWindow.getMonthButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mRewardPopupWindow.getMonthButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateRewardData(3);
                    break;
                }
            }
        }
    };

    private View.OnClickListener  mDistanceClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if (mDistancePopupWindow!=null){
                mDistancePopupWindow.getNearbyButton().setTextColor(getResources().getColor(R.color.btn_color));
                mDistancePopupWindow.getNearbyButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
                mDistancePopupWindow.getAreaButton().setTextColor(getResources().getColor(R.color.btn_color));
                mDistancePopupWindow.getAreaButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
                mDistancePopupWindow.getSubwayButton().setTextColor(getResources().getColor(R.color.btn_color));
                mDistancePopupWindow.getSubwayButton().setBackgroundColor(getResources().getColor(R.color.app_item_bg));
            }
            switch (view.getId()){
                case R.id.btnDistanceNearby:
                    mDistancePopupWindow.getNearbyButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mDistancePopupWindow.getNearbyButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateDistanceData(1);
                    break;
                case R.id.btnDistanceArea:{
                    mDistancePopupWindow.getAreaButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mDistancePopupWindow.getAreaButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateDistanceData(2);
                    break;
                }
                case R.id.btnDistanceSubway:{
                    mDistancePopupWindow.getSubwayButton().setTextColor(getResources().getColor(R.color.app_blue));
                    mDistancePopupWindow.getSubwayButton().setBackgroundColor(getResources().getColor(R.color.white));
                    updateDistanceData(3);
                    break;
                }
            }
        }
    };

    private View.OnClickListener  mHeadClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnDataOrder1:{
                    showCategoryPopupWindow(1);
                    break;
                }
                case R.id.btnDataOrder2:{
                    showCategoryPopupWindow(2);
                    break;
                }
                case R.id.btnDataOrder3:{
                    showCategoryPopupWindow(3);
                    break;
                }
                case R.id.btnDataOrder4:{
                    showCategoryPopupWindow(4);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        categoryInfo=(CategoryInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CATEGORY_INFO);
        if (categoryInfo!=null){
            mHeadTitle.setText(categoryInfo.getCategory());
        }
        ViewUtils.inject(this);

        headView=new SequenceHeadView(mContext,mHeadClickListener);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        mHeadLinearLayout.addView(headView, lp);
        mAdapter=new OfficeAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        initHeadBackView();
        hideSearchEditView();
    }

    @Override
    public void loadData() {
        items.clear();
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start", "0");
        requestMap.put("end", "20");
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.OFFICES_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
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
    public void parserHttpResponse(String result) {
         try{
            OfficeResp resp=(OfficeResp) JsonUtil.ObjFromJson(result, OfficeResp.class);
            if (resp.getSuccess()==1){
                items.addAll(resp.getRoot());
            }
            mAdapter.addAll(items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showCategoryPopupWindow(final int type){
        switch (type){
            case 1:{
                headView.getArrow1().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
            case 2:{
                headView.getArrow2().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
            case 3:{
                headView.getArrow3().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
            case 4:{
                headView.getArrow4().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
        }
        ColorDrawable cd = new ColorDrawable(0x000000);
        if (type>2) {
            mCategoryPopupWindow = new CategoryPopupWindow(this,R.layout.popup_category_choose);
            mCategoryPopupWindow.setBackgroundDrawable(cd);
            mMaskView.setVisibility(View.VISIBLE);
            mCategoryPopupWindow.showAsDropDown(mHeadLinearLayout);
            mCategoryPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    switch (type) {
                        case 3: {
                            headView.getArrow3().setImageResource(R.drawable.arrow_down_grpe);
                            break;
                        }
                        case 4: {
                            headView.getArrow4().setImageResource(R.drawable.arrow_down_grpe);
                            break;
                        }
                    }
                    mMaskView.setVisibility(View.GONE);
                }
            });
            updateCategoryPopupData(type);
        }else if (type==1){
            mDistancePopupWindow=new DistancePopupWindow(this,mDistanceClickListener);
            mDistancePopupWindow.setBackgroundDrawable(cd);
            mMaskView.setVisibility(View.VISIBLE);
            mDistancePopupWindow.showAsDropDown(mHeadLinearLayout);
            mDistancePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    headView.getArrow1().setImageResource(R.drawable.arrow_down_grpe);
                    mMaskView.setVisibility(View.GONE);
                }
            });
            mDistancePopupWindow.getNearbyButton().setTextColor(getResources().getColor(R.color.app_blue));
            mDistancePopupWindow.getNearbyButton().setBackgroundColor(getResources().getColor(R.color.white));
            updateDistanceData(1);
        }else if(type==2){
            mRewardPopupWindow=new RewardPopupWindow(this,mRewardClickListener);
            mRewardPopupWindow.setBackgroundDrawable(cd);
            mMaskView.setVisibility(View.VISIBLE);
            mRewardPopupWindow.showAsDropDown(mHeadLinearLayout);
            mRewardPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    headView.getArrow2().setImageResource(R.drawable.arrow_down_grpe);
                    mMaskView.setVisibility(View.GONE);
                }
            });
            mRewardPopupWindow.getHourButton().setTextColor(getResources().getColor(R.color.app_blue));
            mRewardPopupWindow.getHourButton().setBackgroundColor(getResources().getColor(R.color.white));
            updateRewardData(1);
        }
    }

    private void updateDistanceData(int type){
        try{
            categoryInfos.clear();
            String val="2";
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("type","=",val));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                categoryInfos.addAll(categoryInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mDistancePopupWindow.getListView().setAdapter(new CategoryCheckBoxAdapter(this, categoryInfos,true,R.layout.category_check_box_item));
        mDistancePopupWindow.getListView().setOnItemClickListener(mDistanceItemListener);

    }

    private void updateRewardData(int type){
        try{
            categoryInfos.clear();
            String val="4010";
            if (type==2){
                val="4020";
            }else if(type==3){
                val="4030";
            }
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=",val));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                categoryInfos.addAll(categoryInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mRewardPopupWindow.getListView().setAdapter(new CategoryCheckBoxAdapter(this, categoryInfos,false,R.layout.category_check_box_item));
        mRewardPopupWindow.getListView().setOnItemClickListener(mRewardItemListener);
    }

    private void updateCategoryPopupData(int type){
        boolean isCheck=false;
        try{
            paramInfos.clear();
            String val="2";
            if (type==4){
                val="7";
            }
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<ParamInfo> paramInfoList=db.findAll(Selector.from(ParamInfo.class).where("type","=",val));
            if (paramInfoList!=null&&paramInfoList.size()>0){
                paramInfos.addAll(paramInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mCategoryPopupWindow.getListView().setAdapter(new ParamCheckBoxAdapter(this, paramInfos,isCheck,R.layout.category_check_box_item));
        mCategoryPopupWindow.getListView().setOnItemClickListener(mCategoryItemListener);

    }

    private AdapterView.OnItemClickListener mCategoryItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            ParamInfo paramInfo=paramInfos.get(position);
            if (paramInfo!=null){

            }
            mCategoryPopupWindow.dismiss();
        }
    };


    private AdapterView.OnItemClickListener mDistanceItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            mDistancePopupWindow.dismiss();
        }
    };

    private AdapterView.OnItemClickListener mRewardItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            mRewardPopupWindow.dismiss();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                super.onClick(view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        OfficeInfo officeInfo=items.get(i);
        if (officeInfo!=null) {
            Intent intent = new Intent(CategoryActivity.this, OfficeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConfig.PARAM_OFFICE_INFO, officeInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
