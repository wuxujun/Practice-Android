package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andreabaccega.formedittextvalidator.EmptyValidator;
import com.andreabaccega.widget.FormEditText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.xujun.app.adapter.CategoryCheckBoxAdapter;
import com.xujun.app.adapter.ParamCheckBoxAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.InputList;
import com.xujun.app.model.ParamInfo;
import com.xujun.app.widget.CategoryPopupWindow;
import com.xujun.app.widget.EditViewPopupWindow;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xujunwu on 15/9/14.
 */
public class AttentionSetActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @ViewInject(R.id.llCenter)
    private LinearLayout    mCenterLine;

    @ViewInject(R.id.list)
    private ListView mListView;

    private ItemAdapter     mAdapter;

    private List<InputInfo> items=new ArrayList<InputInfo>();

    private List<CategoryInfo>      categoryInfos=new ArrayList<CategoryInfo>();
    private List<ParamInfo>         paramInfos=new ArrayList<ParamInfo>();

    private ViewHolder mEditHolder;
    private DialogPlus mDialog;
    private FormEditText etContent;
    private TextView    tvEditTitle;


    private ViewHolder          mListHolder;
    private DialogPlus          mListDialog;
    private ListView            mCateList;
    private TextView            mCateTitleTV;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        mHeadTitle.setText("修改我的关注");
        ViewUtils.inject(this);

        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        initHeadBackView();
        hideSearchEditView();
        mHeadBtnRight.setText(getText(R.string.btn_save));
        mHeadBtnRight.setOnClickListener(this);


        initView();
    }

    private void initView(){
        View view=getLayoutInflater().inflate(R.layout.edit_single_content,null);
        mEditHolder=new ViewHolder(view);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        etContent=(FormEditText)view.findViewById(R.id.etContent);
        tvEditTitle=(TextView)view.findViewById(R.id.tvTitle);


        View viewList=getLayoutInflater().inflate(R.layout.popup_category_down,null);
        mListHolder=new ViewHolder(viewList);
        mCateList=(ListView)viewList.findViewById(R.id.list);
        mCateTitleTV=(TextView)viewList.findViewById(R.id.tvTitle);
        viewList.findViewById(R.id.btnDone).setOnClickListener(this);
        viewList.findViewById(R.id.btnCancel).setOnClickListener(this);


    }

    OnClickListener clickDoneListener=new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()){
                case R.id.btnSave:{
                    if (etContent!=null&&etContent.getText().toString()!=null){

                    }
                    dialog.dismiss();
                    break;
                }
                case R.id.btnDone:{

                    dialog.dismiss();
                    break;
                }
                case R.id.btnCancel:{

                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    private void showEditDialog(InputInfo info){
        mDialog=DialogPlus.newDialog(this).setContentHolder(mEditHolder).setCancelable(true)
                .setGravity(Gravity.CENTER).setOnClickListener(clickDoneListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        etContent.setText("");
        etContent.setTag(info);
        etContent.setHint("请输入"+info.getTitle());
        tvEditTitle.setText(info.getTitle());
        mDialog.show();
    }

    private void showListDiaog(InputInfo info){
        int height=StringUtil.getScreenHeight(mContext);
        mListDialog=DialogPlus.newDialog(this).setContentHolder(mListHolder).setCancelable(true).setOnClickListener(clickDoneListener).setGravity(Gravity.BOTTOM).setContentHeight(height/2).setMargin(20,0,20,10).create();
        mListDialog.show();
        mCateTitleTV.setText("请选择"+info.getTitle());
        updateCategoryData(info);
    }




    @Override
    public void loadData() {
        try {
            InputList list=(InputList) JsonUtil.ObjFromJson(getFromAssets("attention_set.json"), InputList.class);
            if (list!=null&&list.getRoot()!=null){
                items.clear();
                items.addAll(list.getRoot());
            }
            mAdapter.notifyDataSetChanged();;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnHeadRight:{
                Intent intent=new Intent();
                AttentionSetActivity.this.setResult(AppConfig.SUCCESS,intent);
                AttentionSetActivity.this.finish();
                break;
            }
            case R.id.ibHeadBack:{
                Intent intent=new Intent();
                AttentionSetActivity.this.setResult(AppConfig.CANCEL,intent);
                AttentionSetActivity.this.finish();
                break;
            }
            case R.id.btnDone:{
                mListDialog.dismiss();
                break;
            }
            case R.id.btnCancel:{
                mListDialog.dismiss();
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        InputInfo   item=items.get(i);
        if (item!=null){
            if (item.getType()==5){
                showEditDialog(item);
            }else {
                showListDiaog(item);
            }
        }
    }

    private void updateCategoryData(InputInfo item){
        try {
            DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
            if (item.getType()==3) {
                String paramName="parent_code";
                if (item.getParamValue().equals("10")){
                    paramName="type";
                }
                List<CategoryInfo> categoryInfoList = db.findAll(Selector.from(CategoryInfo.class).where(paramName, "=", item.getParamValue()));
                if (categoryInfoList != null && categoryInfoList.size() > 0) {
                    categoryInfos.clear();
                    categoryInfos.addAll(categoryInfoList);
                }
                mCateList.setAdapter(new CategoryCheckBoxAdapter(this, categoryInfos,true,R.layout.category_down_item));
                mCateList.setOnItemClickListener(mCategoryItemListener);
            }else if(item.getType()==0){
                List<ParamInfo>  paramInfoList=db.findAll(Selector.from(ParamInfo.class).where("type","=",item.getParamValue()));
                if (paramInfoList!=null&&paramInfoList.size()>0){
                    paramInfos.clear();
                    paramInfos.addAll(paramInfoList);
                }
                mCateList.setAdapter(new ParamCheckBoxAdapter(this,paramInfos,true,R.layout.category_down_item));
                mCateList.setOnItemClickListener(mParamItemListener);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener mParamItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private AdapterView.OnItemClickListener mCategoryItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CategoryInfo categoryInfo=categoryInfos.get(position);
            if (categoryInfo!=null){

            }
        }
    };

    private class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemView    holder;
            if (convertView==null){
                holder=new ItemView();
                convertView=View.inflate(mContext,R.layout.item_input,null);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.value=(TextView)convertView.findViewById(R.id.tvItemValue);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            InputInfo item=items.get(position);
            if (item!=null){
                holder.title.setText(item.getTitle());
                holder.value.setText(item.getResultTitle());
            }
            return convertView;
        }
    }

    class ItemView{
        public TextView     title;
        public TextView     value;

    }
}

