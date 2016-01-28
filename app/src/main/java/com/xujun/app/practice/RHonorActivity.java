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
import android.widget.ImageView;
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
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.xujun.app.model.ResumeHonor;
import com.xujun.app.model.ResumeHonorResp;
import com.xujun.util.DateUtil;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RHonorActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.list)
    private ListView    mListView;

    private ItemAdapter mAdapter;


    private List<ResumeHonor>   items=new ArrayList<ResumeHonor>();


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

        mHeadTitle.setText(getText(R.string.resume_head_title_5));
        initHeadBackView();
        hideSearchEditView();
        mHeadBtnRight.setText(getText(R.string.btn_add));
        mHeadBtnRight.setOnClickListener(this);

        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnHeadRight:{
                Intent intent=new Intent(RHonorActivity.this,RHonorEditActivity.class);
                startActivityForResult(intent,AppConfig.REQUEST_RESUME_HONOR_ADD);

                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode==AppConfig.REQUEST_RESUME_HONOR_ADD){

        }

    }

    @Override
    public void loadData() {
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start", 0);
        requestMap.put("end", 10);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.RESUME_HONOR_LIST, getRequestParams(requestMap), new RequestCallBack<String>() {
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
            ResumeHonorResp resp=(ResumeHonorResp) JsonUtil.ObjFromJson(result,ResumeHonorResp.class);
            if (resp.getSuccess()==1){
                if (resp.getRoot()!=null){
                    items.clear();
                    items.addAll(resp.getRoot());
                    mAdapter.notifyDataSetChanged();
                }else{
                    showCroutonMessage("无数据");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
            ResumeHonor item=items.get(position);
            if (item!=null){
                holder.title.setText(""+item.getBeginTime().substring(0,7)+"-"+item.getEndTime().substring(0,7)+" "+item.getTitle());
            }
            return convertView;
        }
    }

    private class ItemView {
        TextView        title;
    }
}
