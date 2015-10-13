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
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.xujun.app.model.BaseResp;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RHonorEditActivity extends BaseActivity{

    @ViewInject(R.id.etHonorName)
    private FormEditText        etTitle;
    @ViewInject(R.id.etHonorContent)
    private FormEditText        etMsg;
    @ViewInject(R.id.tvHonorTime)
    private TextView            tvTime;

    private ViewHolder          mTimeHolder;
    private DialogPlus          mTimeDialog;

    private DatePicker          beginDatePicker;
    private DatePicker          endDatePicker;


    private String              beginTime;
    private String              endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_r_honor);
        ViewUtils.inject(this);

        mHeadTitle.setText("添加" + getText(R.string.resume_head_5));
        initHeadBackView();
        hideSearchEditView();

        mHeadBtnRight.setText(getText(R.string.btn_save));
        mHeadBtnRight.setOnClickListener(this);

        initHeadView();
    }

    private void initHeadView(){
        etTitle.addValidator(new EmptyValidator(null));
        etMsg.addValidator(new EmptyValidator(null));
        tvTime.setOnClickListener(this);

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
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                endTime = format.format(cal.getTime());
            }
        });
    }

    OnClickListener clickListener=new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()){
                case R.id.btnOk:{
                    tvTime.setText(beginTime+"-"+endTime);
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

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
            case R.id.tvHonorTime:{
                showTimeDialog();
                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    private void save(){
        if (!etTitle.testValidity()){
            return;
        }
        if (!etMsg.testValidity()){
            return;
        }
        if (mMember==null&&mMember.getId()<=0){
            showCroutonMessage("请先登录后再试");
            return;
        }
        String title=etTitle.getText().toString();
        String msg=etMsg.getText().toString();

        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("title", StringUtil.urlEncoder(title));
        requestMap.put("msg", StringUtil.urlEncoder(msg));
        if (beginTime!=null){
            requestMap.put("beginTime",beginTime);
        }
        if(endTime!=null){
            requestMap.put("endTime",endTime);
        }

        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.RESUME_HONOR_ADD, getRequestParams(requestMap), new RequestCallBack<String>() {
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
                        gotoHonorActivity();
                    }
                }, 1);
            }else{

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void gotoHonorActivity(){
        Intent intent=new Intent();
        RHonorEditActivity.this.setResult(AppConfig.SUCCESS,intent);
        RHonorEditActivity.this.finish();
    }
}
