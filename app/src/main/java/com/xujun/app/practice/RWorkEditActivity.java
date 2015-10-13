package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andreabaccega.formedittextvalidator.EmptyValidator;
import com.andreabaccega.widget.FormEditText;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.message.UmengRegistrar;
import com.xujun.app.model.BaseResp;
import com.xujun.app.model.Member;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RWorkEditActivity extends BaseActivity{

    @ViewInject(R.id.list)
    private ListView mListView;

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeader;

    private ItemAdapter mAdapter;

    private List<String>        items=new ArrayList<String>();

    private FormEditText        etName;
    private FormEditText        etOffice;
    private TextView            tvTime;

    private ViewHolder  mEditHolder;
    private DialogPlus  mDialog;
    private FormEditText    etContent;

    private ViewHolder          mTimeHolder;
    private DialogPlus          mTimeDialog;

    private DatePicker          beginDatePicker;
    private DatePicker          endDatePicker;


    private String              beginTime;
    private String              endTime;

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewUtils.inject(this);

        mHeadTitle.setText("添加" + getText(R.string.resume_head_2));
        initHeadBackView();
        hideSearchEditView();

        mHeadBtnRight.setText(getText(R.string.btn_save));
        mHeadBtnRight.setOnClickListener(this);

        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        initHeadView();
    }

    private void initHeadView(){
        View head=getLayoutInflater().inflate(R.layout.edit_r_work,null);
        head.findViewById(R.id.btnAdd).setOnClickListener(this);
        etName=(FormEditText)head.findViewById(R.id.etCompanyName);
        etName.addValidator(new EmptyValidator(null));
        etOffice=(FormEditText)head.findViewById(R.id.etOffice);
        etOffice.addValidator(new EmptyValidator(null));
        tvTime=(TextView)head.findViewById(R.id.tvTime);
        tvTime.setOnClickListener(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        mHeader.addView(head, lp);

        View view=getLayoutInflater().inflate(R.layout.edit_r_content,null);
        mEditHolder=new ViewHolder(view);
        if (mEditHolder.getInflatedView()!=null){
            mEditHolder.getInflatedView().findViewById(R.id.btnSave).setOnClickListener(this);
        }
        etContent=(FormEditText)view.findViewById(R.id.etContent);

        beginTime= DateUtil.getYear()+"-01-01";
        endTime=DateUtil.getToday();

        View timeView=getLayoutInflater().inflate(R.layout.edit_r_time,null);
        mTimeHolder=new ViewHolder(timeView);
        timeView.findViewById(R.id.btnOk).setOnClickListener(this);
        beginDatePicker=(DatePicker)timeView.findViewById(R.id.dpBeginTime);
        beginDatePicker.init(DateUtil.getYear(), 0, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal=Calendar.getInstance();
                cal.set(i,i1,i2);
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                beginTime=format.format(cal.getTime());
            }
        });
        endDatePicker=(DatePicker)timeView.findViewById(R.id.dpEndTime);
        endDatePicker.init(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal=Calendar.getInstance();
                cal.set(i,i1,i2);
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                endTime=format.format(cal.getTime());
            }
        });
    }

    OnClickListener clickListener=new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()){
                case R.id.btnSave:{
                    L.e("............=====>");
                    if (etContent!=null&&etContent.getText().toString()!=null){
                        items.add(etContent.getText().toString());
                        mAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                    break;
                }
                case R.id.btnOk:{
                    tvTime.setText(beginTime+"-"+endTime);
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    private void showEditDialog(){
        mDialog= DialogPlus.newDialog(this).setContentHolder(mEditHolder).setCancelable(true)
                .setGravity(Gravity.CENTER).setOnClickListener(clickListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        etContent.setText("");
        mDialog.show();
    }

    private void showTimeDialog(){
        mTimeDialog=DialogPlus.newDialog(this).setContentHolder(mTimeHolder).setCancelable(false)
                .setGravity(Gravity.CENTER).setOnClickListener(clickListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        mTimeDialog.show();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnHeadRight:{
                save();
                break;
            }
            case R.id.btnAdd:{
                showEditDialog();
                break;
            }
            case R.id.tvTime:{
                showTimeDialog();
                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    private void save(){
        if (!etName.testValidity()){
            return;
        }
        if (!etOffice.testValidity()){
            return;
        }
        if (mMember==null&&mMember.getId()<=0){
            showCroutonMessage("请先登录后再试");
            return;
        }
        String name=etName.getText().toString();
        String office=etOffice.getText().toString();

        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("companyName", StringUtil.urlEncoder(name));
        requestMap.put("officeName", StringUtil.urlEncoder(office));
        if (beginTime!=null){
            requestMap.put("beginTime",beginTime);
        }
        if(endTime!=null){
            requestMap.put("endTime",endTime);
        }
        if (items.size()>0){
            List list=new ArrayList();
            for (String str:items){
                Map<String,Object> it=new HashMap<String, Object>();
                it.put("title",str);
                list.add(it);
            }
            requestMap.put("datas",list);
        }

        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.RESUME_WORK_ADD, getRequestParams(requestMap), new RequestCallBack<String>() {
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
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {
        try{
            BaseResp resp=(BaseResp) JsonUtil.ObjFromJson(result,BaseResp.class);
            if (resp.getSuccess()==1){
                showCroutonMessage("保存成功.");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoWorkActivity();
                    }
                }, 1);
            }else{

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void gotoWorkActivity(){
        Intent intent=new Intent();
        RWorkEditActivity.this.setResult(AppConfig.SUCCESS,intent);
        RWorkEditActivity.this.finish();
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
                convertView=View.inflate(mContext,R.layout.item_r_honor,null);
                holder.title=(TextView)convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.title.setText(items.get(position));
            return convertView;
        }
    }

    private class ItemView {
        TextView        title;
    }
}
