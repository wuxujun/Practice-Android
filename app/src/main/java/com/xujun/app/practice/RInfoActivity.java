package com.xujun.app.practice;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.xujun.app.adapter.CategoryCheckBoxAdapter;
import com.xujun.app.adapter.CityCheckBoxAdapter;
import com.xujun.app.adapter.ParamCheckBoxAdapter;
import com.xujun.app.adapter.SchoolCheckBoxAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.EduEntity;
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.InputList;
import com.xujun.app.model.ParamInfo;
import com.xujun.app.model.ResumeInfo;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RInfoActivity extends BaseActivity{

    private static final String TAG="RInfoActivity";


    @ViewInject(R.id.list)
    private ListView mListView;

    private ItemAdapter         mAdapter;

    private List<InputInfo> items=new ArrayList<InputInfo>();

    private  boolean        isEdit=false;


    private ViewHolder          mEditHolder;
    private DialogPlus          mDialog;
    private FormEditText        etContent;
    private TextView            tvEditTitle;

    private ViewHolder          mRadioHolder;
    private DialogPlus          mRadioDialog;
    private TextView            tvRadioTitleTV;

    private DatePicker              datePicker;
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

    private List<CityInfo>       cityInfos=new ArrayList<CityInfo>();
    private List<EduEntity>      eduEntities=new ArrayList<EduEntity>();


    private InputInfo           currentInputInfo;
    private ResumeInfo          resumeInfo;

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            InputInfo   item=items.get(i);
            if (item!=null&&isEdit){
                if (item.getValue().equals("sex")){
                    showSexSelectDialog(item);
                }else if(item.getValue().equals("brithday")||item.getValue().equals("graduation")){
                    showDateDialog(item);
                }else if(item.getValue().equals("school")||item.getValue().equals("specialty")){
                    showTwoListDailog(item);
                }else if(item.getValue().equals("educational")){
                    showOneListDialog(item);
                } else {
                    showEditDialog(item);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener onCateClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (currentInputInfo.getValue().equals("specialty")) {

                CategoryInfo categoryInfo = cateInfos.get(i);
                if (categoryInfo != null) {
                    updateTwoResultCategory(categoryInfo.getCode());
                }
            }else{
                CityInfo cityInfo=cityInfos.get(i);
                if (cityInfo!=null){
                    updateTwoResultSchool(cityInfo.getCityId());
                }
            }
        }
    };


    private AdapterView.OnItemClickListener onResultClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (currentInputInfo.getValue().equals("specialty")){
                CategoryInfo categoryInfo=categoryInfos.get(i);
                if (categoryInfo!=null){
                    if (resumeInfo==null){
                        resumeInfo=new ResumeInfo();
                    }
                    resumeInfo.setSpecialty(categoryInfo.getCategory());
                    resumeInfo.setSpecialtyCode(categoryInfo.getCode());
                    mAdapter.notifyDataSetChanged();
                }
                mTwoCateDialog.dismiss();
            }else{
                EduEntity eduEntity=eduEntities.get(i);
                if (eduEntity!=null){
                    if (resumeInfo==null){
                        resumeInfo=new ResumeInfo();
                    }
                    resumeInfo.setSchoolCode(eduEntity.getEduCode());
                    resumeInfo.setSchool(eduEntity.getEduName());
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

        mHeadTitle.setText(getText(R.string.resume_head_title_1));
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

        if (mAppContext.readObject(AppConfig.OBJECT_RESUME_INFO)!=null){
            resumeInfo=(ResumeInfo)mAppContext.readObject(AppConfig.OBJECT_RESUME_INFO);
        }

    }
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            Log.e(TAG,"onCheckedChanged "+i);
            switch (i){
                case R.id.rbSexMale:{
                    SetResumeInfo("1");
                    break;
                }
                case R.id.rbSexFemale:{
                    SetResumeInfo("0");
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
                        Log.e("","-------->"+etContent.getText().toString());
                        SetResumeInfo(etContent.getText().toString());
                    }
                    dialog.dismiss();
                    break;
                }
                case R.id.btnDateDone:{
                    SetResumeInfo(selectDate);
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    private void SetResumeInfo(String val){
        if (resumeInfo==null){
            resumeInfo=new ResumeInfo();
        }
        if (currentInputInfo!=null){
            if (currentInputInfo.getValue().equals("name")){
                resumeInfo.setName(val);
            }else if (currentInputInfo.getValue().equals("srcPlace")){
                resumeInfo.setSrcPlace(val);
            }else if (currentInputInfo.getValue().equals("brithday")){
                resumeInfo.setBrithday(val);
            }else if (currentInputInfo.getValue().equals("graduation")){
                if (val.length()>7){
                    resumeInfo.setGraduation(val.substring(0,7));
                }else {
                    resumeInfo.setGraduation(val);
                }
            }else if (currentInputInfo.getValue().equals("gradepoint")){
                resumeInfo.setGradePoint(val);
            }else if(currentInputInfo.getValue().equals("mobile")){
                resumeInfo.setMobile(val);
            }else if(currentInputInfo.getValue().equals("email")){
                resumeInfo.setEmail(val);
            }else if(currentInputInfo.getValue().equals("sex")){
                if (val.equals("1")){
                    resumeInfo.setSex(1);
                }else{
                    resumeInfo.setSex(0);
                }
            }else if(currentInputInfo.getValue().equals("school")){

            }else if(currentInputInfo.getValue().equals("educational")){

            }else if(currentInputInfo.getValue().equals("specialty")){

            }
        }
        Log.e(TAG, "SetResumeInfo  " + currentInputInfo.getValue() + "  : " + val);
        mAdapter.notifyDataSetChanged();
    }

    private void showEditDialog(InputInfo info){
        currentInputInfo=info;
        mDialog=DialogPlus.newDialog(this).setContentHolder(mEditHolder).setCancelable(true)
                .setGravity(Gravity.CENTER).setOnClickListener(clickDoneListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        etContent.setText("");
        etContent.setTag(info);
        etContent.setHint("请输入"+info.getTitle());
        tvEditTitle.setText(info.getTitle());
        mDialog.show();
    }

    private void showSexSelectDialog(InputInfo info){
        currentInputInfo=info;
        mRadioDialog=DialogPlus.newDialog(this).setContentHolder(mRadioHolder).setCancelable(true).setGravity(Gravity.CENTER).setContentWidth(500).setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();

        mRadioDialog.show();
    }

    private void showDateDialog(InputInfo info){
        currentInputInfo=info;

        mDateDialog=DialogPlus.newDialog(this).setContentHolder(mDateHolder).setCancelable(true).setOnClickListener(clickDoneListener).setGravity(Gravity.CENTER).setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
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
                DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
                List<CategoryInfo> cateInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=","10"));
                if (cateInfoList!=null&&cateInfoList.size()>0){
                    cateInfos.addAll(cateInfoList);
                }
                mTwoCateListView.setAdapter(new CategoryCheckBoxAdapter(this,cateInfoList,false,R.layout.category_check_box_item));
                mTwoCateListView.setOnItemClickListener(onCateClickListener);
                updateTwoResultCategory("1010");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }else{
            try{
                cityInfos.clear();
                DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
                List<CityInfo> cityInfoList=db.findAll(Selector.from(CityInfo.class).where("type","=","1"));
                if (cityInfoList!=null&&cityInfoList.size()>0){
                    cityInfos.addAll(cityInfoList);
                }
                mTwoCateListView.setAdapter(new CityCheckBoxAdapter(this,cityInfos,false));
                mTwoCateListView.setOnItemClickListener(onCateClickListener);
                updateTwoResultSchool("330100");
            }catch (DbException e){
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
            mTwoResultListView.setAdapter(new CategoryCheckBoxAdapter(this,categoryInfos,false,R.layout.category_check_box_item));
            mTwoResultListView.setOnItemClickListener(onResultClickListener);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void updateTwoResultSchool(String code){
        try {
            eduEntities.clear();
            DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
            List<EduEntity> eduEntityList = db.findAll(Selector.from(EduEntity.class).where("cityId", "=", code));
            if (eduEntityList != null && eduEntityList.size() > 0) {
                eduEntities.addAll(eduEntityList);
            }
            mTwoResultListView.setAdapter(new SchoolCheckBoxAdapter(this,eduEntities,false));
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
        mCateListView.setAdapter(new ParamCheckBoxAdapter(this, paramInfos, false, R.layout.category_down_item));
        mCateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ParamInfo paramInfo = paramInfos.get(i);
                if (paramInfo != null) {
                    if (resumeInfo == null) {
                        resumeInfo = new ResumeInfo();
                    }
                    resumeInfo.setEducationalCode(paramInfo.getCode());
                    resumeInfo.setEducational(paramInfo.getName());
                    mAdapter.notifyDataSetChanged();
                }
                mCateDialog.dismiss();
            }
        });
    }

    @Override
    public void loadData() {
        try {
            InputList list=(InputList) JsonUtil.ObjFromJson(getFromAssets("resume_info.json"), InputList.class);
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
                isEdit=!isEdit;
                if (isEdit){
                    showCroutonMessage("点击可编辑");
                    mHeadBtnRight.setText(getString(R.string.btn_done));
                }else{
                    mHeadBtnRight.setText(getString(R.string.btn_edit));
                    if (resumeInfo!=null){
                        if (mMember!=null){
                            if (!StringUtil.isEmpty(mMember.getMobile())){
                                resumeInfo.setMobile(mMember.getMobile());
                            }
                            if (!StringUtil.isEmpty(mMember.getEmail())){
                                resumeInfo.setEmail(mMember.getEmail());
                            }
                        }
                        mAppContext.saveObject(resumeInfo,AppConfig.OBJECT_RESUME_INFO);
                        sendData(resumeInfo);
                    }

                }
                break;
            }
            default:
                super.onClick(view);
        }
    }

    private String  getResumeInfo(ResumeInfo info){
        try{
            return JsonUtil.toJson(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void sendData(ResumeInfo info) {
        info.setMid(mMember.getId());
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("resumeInfo",info);
        L.e(TAG, requestMap.toString());
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.RESUME_INFO_ADD, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() " + responseInfo.result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() " + s);
            }
        });
    }

    class ItemAdapter extends BaseAdapter {

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
                holder.linearLayout=(LinearLayout)convertView.findViewById(R.id.content);
                holder.empty=convertView.findViewById(R.id.empty);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.value=(TextView)convertView.findViewById(R.id.tvItemValue);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.empty.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            InputInfo item=items.get(position);
            if (item!=null){
                holder.title.setText(item.getTitle());
                if (resumeInfo!=null){
                    if (item.getValue().equals("name")&&!StringUtil.isEmpty(resumeInfo.getName())){
                        holder.value.setText(resumeInfo.getName());
                    }else if (item.getValue().equals("srcPlace")&&!StringUtil.isEmpty(resumeInfo.getSrcPlace())){
                        holder.value.setText(resumeInfo.getSrcPlace());
                    }else if (item.getValue().equals("brithday")&&!StringUtil.isEmpty(resumeInfo.getBrithday())){
                        holder.value.setText(resumeInfo.getBrithday());
                    }else if (item.getValue().equals("graduation")&&!StringUtil.isEmpty(resumeInfo.getGraduation())){
                        holder.value.setText(resumeInfo.getGraduation());
                    }else if (item.getValue().equals("mobile")&&!StringUtil.isEmpty(resumeInfo.getMobile())){
                        holder.value.setText(resumeInfo.getMobile());
                    }else if (item.getValue().equals("email")&&!StringUtil.isEmpty(resumeInfo.getEmail())){
                        holder.value.setText(resumeInfo.getEmail());
                    }else if(item.getValue().equals("sex")){
                        if (resumeInfo.getSex()==1){
                            holder.value.setText("男");
                        }else{
                            holder.value.setText("女");
                        }
                    }else if(item.getValue().equals("specialty")&&!StringUtil.isEmpty(resumeInfo.getSpecialty())){
                        holder.value.setText(resumeInfo.getSpecialty());
                    }else if(item.getValue().equals("educational")&&!StringUtil.isEmpty(resumeInfo.getEducational())){
                        holder.value.setText(resumeInfo.getEducational());
                    }else if (item.getValue().equals("gradepoint")&&!StringUtil.isEmpty(resumeInfo.getGradePoint())){
                        holder.value.setText(resumeInfo.getGradePoint());
                    }else if (item.getValue().equals("school")&&!StringUtil.isEmpty(resumeInfo.getSchool())){
                        holder.value.setText(resumeInfo.getSchool());
                    }
                }
                if (item.getType()==10){
                    holder.empty.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
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
        LinearLayout    linearLayout;
        View            empty;
        TextView        title;
        TextView        value;
    }

}
