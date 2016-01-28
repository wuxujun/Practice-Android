package com.xujun.app.practice;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.InputList;
import com.xujun.app.model.MemberInfo;
import com.xujun.app.model.ParamInfo;
import com.xujun.app.model.ResumeInfo;
import com.xujun.app.model.ResumeLife;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.StringUtil;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xujunwu on 15/10/8.
 */
public class MyActivity extends BaseActivity{

    @ViewInject(R.id.list)
    private ListView        mListView;

    private ItemAdapter         mAdapter;

    private List<InputInfo>     items=new ArrayList<InputInfo>();

    private boolean isEdit=false;

    private ViewHolder mEditHolder;
    private DialogPlus mDialog;
    private TextView     tvEditTitle;
    private FormEditText etContent;

    private ViewHolder          mRadioHolder;
    private DialogPlus          mRadioDialog;
    private TextView            tvRadioTitleTV;

    private DatePicker datePicker;
    private ViewHolder          mDateHolder;
    private DialogPlus          mDateDialog;
    private TextView            tvDateTitleTV;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String              selectDate=format.format(new Date());

    private ViewHolder          mTwoCateHolder;
    private DialogPlus          mTwoCateDialog;
    private ListView            mTwoCateListView;
    private ListView            mTwoResultListView;

    private ViewHolder          mCateHolder;
    private DialogPlus          mCateDialog;
    private ListView            mCateListView;


    private List<ParamInfo>      paramInfos=new ArrayList<ParamInfo>();
    private List<CategoryInfo>   cateInfos=new ArrayList<CategoryInfo>();
    private List<CategoryInfo>   categoryInfos=new ArrayList<CategoryInfo>();

    private InputInfo           currentInputInfo;

    private MemberInfo          memberInfo;

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            InputInfo   item=items.get(i);
            if (item!=null&&isEdit){
                if (item.getId()==1){

                }else if (item.getValue().equals("sex")){
                    showSexSelectDialog(item);
                }else if(item.getValue().equals("brithday")){
                    showDateDialog(item);
                }else if(item.getValue().equals("school")||item.getValue().equals("specialty")){
                    showTwoListDailog(item);
                }else if(item.getValue().equals("grade")){
                    showOneListDialog(item);
                }else if (item.getValue().equals("thridUser")){

                }else {
                    showEditDialog(item);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener onCateClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CategoryInfo categoryInfo=cateInfos.get(i);
            if (categoryInfo!=null){
                updateTwoResultCategory(categoryInfo.getCode());
            }
        }
    };


    private AdapterView.OnItemClickListener onResultClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (currentInputInfo.getValue().equals("specialty")){
                CategoryInfo categoryInfo=categoryInfos.get(i);
                if (categoryInfo!=null){
                    if (memberInfo==null){
                        memberInfo=new MemberInfo();
                    }
                    memberInfo.setSpecialty(categoryInfo.getCategory());
                    memberInfo.setSpecialtyCode(categoryInfo.getCode());
                    mAdapter.notifyDataSetChanged();
                }
                mTwoCateDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewUtils.inject(this);
        mHeadTitle.setText(getText(R.string.tab_my));

        initHeadBackView();
        hideSearchEditView();
        mHeadBtnRight.setText(getText(R.string.btn_edit));
        mHeadBtnRight.setOnClickListener(this);
        init();
    }

    private void init(){
        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        View view=getLayoutInflater().inflate(R.layout.edit_single_content,null);
        mEditHolder=new ViewHolder(view);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        etContent=(FormEditText)view.findViewById(R.id.etContent);
        tvEditTitle=(TextView)view.findViewById(R.id.tvTitle);

        View viewRadio=getLayoutInflater().inflate(R.layout.select_radio_choose,null);
        mRadioHolder=new ViewHolder(viewRadio);
        ((RadioGroup)viewRadio.findViewById(R.id.rbSex)).setOnCheckedChangeListener(onCheckedChangeListener);

        View dateView=getLayoutInflater().inflate(R.layout.edit_time,null);
        mDateHolder=new ViewHolder(dateView);
        tvDateTitleTV=(TextView)dateView.findViewById(R.id.tvTitle);
        dateView.findViewById(R.id.btnDateDone).setOnClickListener(this);
        datePicker=(DatePicker)dateView.findViewById(R.id.dpDate);
        datePicker.init(DateUtil.getYear(), 0, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                selectDate = format.format(cal.getTime());
            }
        });
        View cateView=getLayoutInflater().inflate(R.layout.one_cate_choose,null);
        mCateHolder=new ViewHolder(cateView);
        mCateListView=(ListView)cateView.findViewById(R.id.list);


        View twoCateView=getLayoutInflater().inflate(R.layout.two_cate_choose,null);
        mTwoCateHolder=new ViewHolder(twoCateView);
        mTwoCateListView=(ListView)twoCateView.findViewById(R.id.cateList);
        mTwoResultListView=(ListView)twoCateView.findViewById(R.id.resultlist);
        if (mAppContext.readObject(AppConfig.OBJECT_MEMBER_INFO)!=null){
            memberInfo=(MemberInfo)mAppContext.readObject(AppConfig.OBJECT_MEMBER_INFO);
        }
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.rbSexMale:{
                    SetMemberInfo("1");
                    break;
                }
                case R.id.rbSexFemale:{
                    SetMemberInfo("0");
                    break;
                }
            }
            mRadioDialog.dismiss();
        }
    };

    OnClickListener clickDoneListener=new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()){
                case R.id.btnSave:{
                    if (etContent!=null&&etContent.getText().toString()!=null){
                        SetMemberInfo(etContent.getText().toString());
                    }
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    private void showEditDialog(InputInfo info){
        currentInputInfo=info;
        mDialog=DialogPlus.newDialog(this).setContentHolder(mEditHolder).setCancelable(false)
                .setGravity(Gravity.CENTER).setOnClickListener(clickDoneListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        tvEditTitle.setText(info.getTitle());
        etContent.setText("");
        etContent.setHint("请输入"+info.getTitle());
        etContent.setTag(info);
        mDialog.show();
    }

    private void showSexSelectDialog(InputInfo info){
        currentInputInfo=info;
        mRadioDialog=DialogPlus.newDialog(this).setContentHolder(mRadioHolder).setCancelable(true).setGravity(Gravity.CENTER).setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        mRadioDialog.show();
    }

    private void showDateDialog(InputInfo info){
        currentInputInfo=info;

        mDateDialog=DialogPlus.newDialog(this).setContentHolder(mDateHolder).setCancelable(true).setOnClickListener(clickDoneListener).setGravity(Gravity.BOTTOM).setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        mDateDialog.show();
        tvDateTitleTV.setText("请选择" + info.getTitle());
    }

    private void showTwoListDailog(InputInfo info){
        currentInputInfo=info;
        int height=StringUtil.getScreenHeight(mContext);
        mTwoCateDialog=DialogPlus.newDialog(this).setContentHolder(mTwoCateHolder).setCancelable(true).setGravity(Gravity.BOTTOM).setContentHeight(height/2).create();
        mTwoCateDialog.show();
        updateTwoCategory(info);
    }

    private void updateTwoCategory(InputInfo info){
        if (info.getValue().equals("specialty")) {
            try {
                cateInfos.clear();
                categoryInfos.clear();
                DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
                List<CategoryInfo> cateInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=","10"));
                if (cateInfoList!=null&&cateInfoList.size()>0){
                    cateInfos.addAll(cateInfoList);
                }
                mTwoCateListView.setAdapter(new CategoryCheckBoxAdapter(this,cateInfoList,false,R.layout.category_down_item));
                mTwoCateListView.setOnItemClickListener(onCateClickListener);
                updateTwoResultCategory("1010");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTwoResultCategory(String code){
        try {
            categoryInfos.clear();
            DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList = db.findAll(Selector.from(CategoryInfo.class).where("parent_code", "=", code));
            if (categoryInfoList != null && categoryInfoList.size() > 0) {
                categoryInfos.addAll(categoryInfoList);
            }
            mTwoResultListView.setAdapter(new CategoryCheckBoxAdapter(this,categoryInfos,false,R.layout.category_down_item));
            mTwoResultListView.setOnItemClickListener(onResultClickListener);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void showOneListDialog(InputInfo info){
        currentInputInfo=info;
        int height=StringUtil.getScreenHeight(mContext);
        mCateDialog=DialogPlus.newDialog(this).setContentHolder(mCateHolder).setCancelable(true).setGravity(Gravity.BOTTOM).setContentHeight(height/2).create();
        mCateDialog.show();
        updateCategory(3);
    }

    private void updateCategory(int type){
        try{
            paramInfos.clear();
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<ParamInfo> paramInfoList=db.findAll(Selector.from(ParamInfo.class).where("type","=",type));
            if (paramInfoList!=null&&paramInfoList.size()>0){
                paramInfos.addAll(paramInfoList);
            }
        }catch (DbException e){
            e.printStackTrace();
        }
        mCateListView.setAdapter(new ParamCheckBoxAdapter(this, paramInfos, false,R.layout.category_down_item));
        mCateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ParamInfo paramInfo = paramInfos.get(i);
                if (paramInfo != null) {
                    if (memberInfo == null) {
                        memberInfo = new MemberInfo();
                        if (mMember!=null){
                            memberInfo.setMid(mMember.getId());
                        }
                    }
                    memberInfo.setEducationalCode(paramInfo.getCode());
                    memberInfo.setEducational(paramInfo.getName());
                    mAdapter.notifyDataSetChanged();
                }
                mCateDialog.dismiss();
            }
        });
    }

    private void SetMemberInfo(String val){
        if (memberInfo==null){
            memberInfo=new MemberInfo();
            if (mMember!=null){
                memberInfo.setMid(mMember.getId());
            }
        }
        if (currentInputInfo!=null){
            if (currentInputInfo.getValue().equals("nickName")){
                memberInfo.setUserName(val);
            }else if (currentInputInfo.getValue().equals("brithday")){
                memberInfo.setBrithday(val);
            }else if(currentInputInfo.getValue().equals("mobile")){
                memberInfo.setMobile(val);
            }else if(currentInputInfo.getValue().equals("email")){
                memberInfo.setEmail(val);
            }else if(currentInputInfo.getValue().equals("sex")){
                if (val.equals("1")){
                    memberInfo.setSex(1);
                }else{
                    memberInfo.setSex(0);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnHeadRight:{
                isEdit=!isEdit;
                if (isEdit){
                    showCroutonMessage("点击可编辑");
                    mHeadBtnRight.setText(getString(R.string.btn_done));
                }else{
                    mHeadBtnRight.setText(getString(R.string.btn_edit));
                    if (memberInfo!=null){
                        mAppContext.saveObject(memberInfo,AppConfig.OBJECT_MEMBER_INFO);
                    }
                }
                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    @Override
    public void loadData() {
        try {
            InputList list=(InputList)JsonUtil.ObjFromJson(getFromAssets("my_info.json"),InputList.class);
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

    class ItemAdapter extends BaseAdapter{

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
                holder.linearLayoutAvatar=(LinearLayout)convertView.findViewById(R.id.avatar);
                holder.linearLayout=(LinearLayout)convertView.findViewById(R.id.content);
                holder.empty=convertView.findViewById(R.id.empty);
                holder.avatar=(ImageView)convertView.findViewById(R.id.ivIcon);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.value=(TextView)convertView.findViewById(R.id.tvItemValue);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.empty.setVisibility(View.GONE);
            holder.linearLayoutAvatar.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            InputInfo item=items.get(position);
            if (item!=null){
                holder.title.setText(item.getTitle());
                if(item.getType()==10){
                    holder.empty.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.linearLayoutAvatar.setVisibility(View.GONE);
                }
                if (item.getType()==1){
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.linearLayoutAvatar.setVisibility(View.VISIBLE);
                    holder.empty.setVisibility(View.GONE);
                }

                if (memberInfo!=null){
                    if (item.getValue().equals("nickName")&&!StringUtil.isEmpty(memberInfo.getUserName())){
                        holder.value.setText(memberInfo.getUserName());
                    }else if (item.getValue().equals("brithday")&&!StringUtil.isEmpty(memberInfo.getBrithday())){
                        holder.value.setText(memberInfo.getBrithday());
                    }else if (item.getValue().equals("mobile")&&!StringUtil.isEmpty(memberInfo.getMobile())){
                        holder.value.setText(memberInfo.getMobile());
                    }else if (item.getValue().equals("email")&&!StringUtil.isEmpty(memberInfo.getEmail())){
                        holder.value.setText(memberInfo.getEmail());
                    }else if(item.getValue().equals("sex")){
                        if (memberInfo.getSex()==1){
                            holder.value.setText("男");
                        }else{
                            holder.value.setText("女");
                        }
                    }else if(item.getValue().equals("specialty")&&!StringUtil.isEmpty(memberInfo.getSpecialty())){
                        holder.value.setText(memberInfo.getSpecialty());
                    }else if(item.getValue().equals("grade")&&!StringUtil.isEmpty(memberInfo.getEducational())){
                        holder.value.setText(memberInfo.getEducational());
                    }else if (item.getValue().equals("school")&&!StringUtil.isEmpty(memberInfo.getSchool())){
                        holder.value.setText(memberInfo.getSchool());
                    }
                }
                if (mMember!=null){
                    if (item.getValue().equals("mobile")&& !StringUtil.isEmpty(mMember.getMobile())){
                        holder.value.setText(mMember.getMobile());
                    }
                    if (item.getValue().equals("email")&& !StringUtil.isEmpty(mMember.getEmail())){
                        holder.value.setText(mMember.getEmail());
                    }
                }
            }
            return convertView;
        }
    }

    private class ItemView {
        LinearLayout linearLayoutAvatar;
        LinearLayout linearLayout;
        ImageView       avatar;
        View            empty;
        TextView        title;
        TextView        value;
    }
}
