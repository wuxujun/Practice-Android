package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.CategoryCheckBoxAdapter;
import com.xujun.app.adapter.CityItemAdapter;
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.widget.CategoryPopupWindow;
import com.xujun.app.widget.CityPopupWindow;
import com.xujun.app.widget.SequenceHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/9/4.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private CategoryInfo        categoryInfo;

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeadLinearLayout;

    @ViewInject(R.id.list)
    private ListView            mListView;

    private SequenceHeadView    headView;
    private CategoryPopupWindow mCategoryPopupWindow;

    private OfficeAdapter       mAdapter;
    private List<OfficeInfo>    items=new ArrayList<OfficeInfo>();

    private List<CategoryInfo>      categoryInfos=new ArrayList<CategoryInfo>();

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
        OfficeInfo info=new OfficeInfo();
        info.setId(1);
        info.setName("测试工程师");
        items.add(info);
        info=new OfficeInfo();
        info.setId(2);
        info.setName("销售人员");
        items.add(info);
        mAdapter.addAll(items);
    }

    @Override
    public void parserHttpResponse(String result) {

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

        mCategoryPopupWindow=new CategoryPopupWindow(this);
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
                }
            }
        });
        updateCategoryPopupData(type);
    }

    private void updateCategoryPopupData(int type){
        try{
            String val="30";

            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=",val));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                categoryInfos.clear();
                categoryInfos.addAll(categoryInfoList);
            }

        }catch (DbException e){
            e.printStackTrace();
        }
        mCategoryPopupWindow.getListView().setAdapter(new CategoryCheckBoxAdapter(this, categoryInfos));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                super.onClick(view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
