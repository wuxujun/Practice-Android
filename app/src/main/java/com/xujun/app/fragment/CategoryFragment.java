package com.xujun.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CategoryResp;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.CategoryActivity;
import com.xujun.app.practice.R;
import com.xujun.app.widget.CategoryHeadView;
import com.xujun.app.widget.CategoryPopupWindow;
import com.xujun.app.widget.RoundedLetterView;
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
 * Created by xujunwu on 15/8/1.
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private CategoryHeadView    headView;

    private List<CategoryInfo> items=new ArrayList<CategoryInfo>();
    private List<OfficeInfo>    officeInfos=new ArrayList<OfficeInfo>();

    private ItemAdapter     mAdapter;
    private OfficeAdapter       officeAdapter;

    private String          mHeadTabType="10";
    private String          mHeadCategory="1010";

    @ViewInject(R.id.list)
    private ListView      mListView;

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeadLinearLayout;

    private CategoryPopupWindow mCategoryPopupWindow;
    private List<CategoryInfo>      categoryInfos=new ArrayList<CategoryInfo>();


    public void setHeadTabType(String type){
        this.mHeadTabType=type;
    }

    private View.OnClickListener  mHeadClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
           if (mHeadTabType.equals("10")){
               selectHeadView(view);
           }else if(mHeadTabType.equals("20")){
               switch (view.getId()){
                   case R.id.btnCategory1:{
                       showCategoryPopupWindow(1);
                       break;
                   }
                   case R.id.btnCategory2:{
                       showCategoryPopupWindow(2);
                       break;
                   }
                   case R.id.btnCategory3:{
                       showCategoryPopupWindow(3);
                       break;
                   }
                   case R.id.btnCategory4:{
                       showCategoryPopupWindow(4);
                       break;
                   }
               }
           }else if(mHeadTabType.equals("30")){
               switch (view.getId()){
                   case R.id.btnCategory1:{
                       showCategoryPopupWindow(5);
                       break;
                   }
                   case R.id.btnCategory2:{
                       showCategoryPopupWindow(6);
                       break;
                   }
                   case R.id.btnCategory3:{
                       showCategoryPopupWindow(7);
                       break;
                   }
               }
           }
        }
    };


    private void selectHeadView(View view){
        headView.getCategory1().setTextColor(getResources().getColor(R.color.btn_color));
        headView.getCategory2().setTextColor(getResources().getColor(R.color.btn_color));
        headView.getCategory3().setTextColor(getResources().getColor(R.color.btn_color));
        headView.getCategory4().setTextColor(getResources().getColor(R.color.btn_color));
        switch (view.getId()){
            case R.id.btnCategory1:{
                headView.getCategory1().setTextColor(getResources().getColor(R.color.black));
                mHeadCategory="1010";
                break;
            }
            case R.id.btnCategory2:{
                headView.getCategory2().setTextColor(getResources().getColor(R.color.black));
                mHeadCategory="1020";
                break;
            }
            case R.id.btnCategory3:{
                headView.getCategory3().setTextColor(getResources().getColor(R.color.black));
                mHeadCategory="1030";
                break;
            }
            case R.id.btnCategory4:{
                headView.getCategory4().setTextColor(getResources().getColor(R.color.black));
                mHeadCategory="1040";
                break;
            }
        }
        loadData();
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
        officeAdapter=new OfficeAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.fragment_list,null);
        ViewUtils.inject(this,mContentView);
        headView=new CategoryHeadView(mContext,mHeadClickListener);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        mHeadLinearLayout.addView(headView, lp);
        resetHeadView();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return mContentView;
    }

    private void resetHeadView(){
        headView.getCategory1().setTextColor(getResources().getColor(R.color.black));
        headView.getCategory2().setTextColor(getResources().getColor(R.color.btn_color));
        headView.getCategory3().setTextColor(getResources().getColor(R.color.btn_color));
        headView.getCategory4().setTextColor(getResources().getColor(R.color.btn_color));
    }

    public void reloadData(String type){
        items.clear();
        officeInfos.clear();
        mHeadTabType=type;
        resetHeadView();
        headView.getLinearLayout4().setVisibility(View.VISIBLE);
        headView.getArrow1().setVisibility(View.GONE);
        headView.getArrow2().setVisibility(View.GONE);
        headView.getArrow3().setVisibility(View.GONE);
        headView.getArrow4().setVisibility(View.GONE);
        headView.getLine3().setVisibility(View.VISIBLE);
        if (mHeadTabType.equals("10")){
            headView.getCategory1().setText(R.string.category_head_1);
            headView.getCategory2().setText(R.string.category_head_2);
            headView.getCategory3().setText(R.string.category_head_3);
            headView.getCategory4().setText(R.string.category_head_4);
            mListView.setAdapter(mAdapter);
            loadData();
        }else if(mHeadTabType.equals("20")){
            headView.getCategory1().setText(R.string.data_order_0);
            headView.getCategory2().setText(R.string.data_order_2);
            headView.getCategory3().setText(R.string.data_order_3);
            headView.getCategory4().setText(R.string.data_order_4);
            headView.getArrow1().setVisibility(View.VISIBLE);
            headView.getArrow2().setVisibility(View.VISIBLE);
            headView.getArrow3().setVisibility(View.VISIBLE);
            headView.getArrow4().setVisibility(View.VISIBLE);
            OfficeInfo info=new OfficeInfo();
            info.setId(1);
            info.setName("测试工程师");
            officeInfos.add(info);
            info=new OfficeInfo();
            info.setId(2);
            info.setName("销售人员");
            officeInfos.add(info);
            mListView.setAdapter(officeAdapter);
            officeAdapter.notifyDataSetChanged();

        }else{
            headView.getCategory1().setText(R.string.head_tab_category_3);
            headView.getCategory2().setText(R.string.data_order_5);
            headView.getCategory3().setText(R.string.data_order_6);
            headView.getLinearLayout4().setVisibility(View.GONE);
            headView.getLine3().setVisibility(View.GONE);
            headView.getArrow1().setVisibility(View.VISIBLE);
            headView.getArrow2().setVisibility(View.VISIBLE);
            headView.getArrow3().setVisibility(View.VISIBLE);

            OfficeInfo info=new OfficeInfo();
            info.setId(1);
            info.setName("测试工程师");
            officeInfos.add(info);
            info=new OfficeInfo();
            info.setId(2);
            info.setName("销售人员");
            officeInfos.add(info);

            mListView.setAdapter(officeAdapter);
            officeAdapter.notifyDataSetChanged();
        }
    }

    public void loadData(){
        items.clear();
        try{
            DbUtils db=DbUtils.create(mContext, AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=",mHeadCategory));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                items.addAll(categoryInfoList);
            }
            mAdapter.notifyDataSetChanged();
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CategoryInfo categoryInfo=items.get(i);
        if (categoryInfo!=null){
            Intent intent=new Intent(getSherlockActivity(), CategoryActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable(AppConfig.PARAM_CATEGORY_INFO,categoryInfo);
            intent.putExtras(bundle);
            startActivity(intent);
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
            case 5:{
                headView.getArrow1().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
            case 6:{
                headView.getArrow2().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
            case 7:{
                headView.getArrow3().setImageResource(R.drawable.arrow_up_grpe);
                break;
            }
        }

        mCategoryPopupWindow=new CategoryPopupWindow(getActivity());
        mCategoryPopupWindow.showAsDropDown(mHeadLinearLayout);
        mCategoryPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (type){
                    case 1:{
                        headView.getArrow1().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 2:{
                        headView.getArrow2().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 3:{
                        headView.getArrow3().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 4:{
                        headView.getArrow4().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 5:{
                        headView.getArrow1().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 6:{
                        headView.getArrow2().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                    case 7:{
                        headView.getArrow3().setImageResource(R.drawable.arrow_down_grpe);
                        break;
                    }
                }
            }
        });
        updateCategoryPopupData(type);
    }

    private void updateCategoryPopupData(int type){
        try{
            String val="2010";
            if (!mHeadTabType.equals("10")){
                switch (type){
                    case 1:
                        val="2010";
                        break;
                    case 2:
                        break;
                    case 3:
                        val="4020";
                        break;
                    case 4:
                        break;
                    case 5:
                        val="2020";
                        break;
                    case 6:
                        break;
                }
            }
            DbUtils db=DbUtils.create(getActivity(),AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=",val));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                categoryInfos.clear();
                categoryInfos.addAll(categoryInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mCategoryPopupWindow.getListView().setAdapter(new CategoryCheckBoxAdapter(getActivity(), categoryInfos));
        mCategoryPopupWindow.getListView().setOnItemClickListener(mCategoryItemListener);

    }

    private AdapterView.OnItemClickListener mCategoryItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CategoryInfo categoryInfo=categoryInfos.get(position);
            if (categoryInfo!=null){

            }
        }
    };


    static class ItemView
    {
        public RoundedLetterView icon;

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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_listview_item, null);
                holder = new ItemView();
                holder.icon = (RoundedLetterView) convertView.findViewById(R.id.rlName);
                holder.title = (TextView) convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            } else {
                holder = (ItemView) convertView.getTag();
            }
            CategoryInfo info = items.get(i);
            if (info != null) {
                holder.title.setText(info.getCategory());
                holder.icon.setTitleText(info.getCategory().substring(0, 1));
            }
            return convertView;
        }
    }

    class OfficeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return officeInfos.size();
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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.home_listview_item, null);
                holder = new ItemView();
                holder.title = (TextView) convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            } else {
                holder = (ItemView) convertView.getTag();
            }
            return convertView;
        }
    }
}