package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.ResumeLife;
import com.xujun.app.model.ResumeLifeResp;
import com.xujun.app.model.ResumeWorkResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/9/6.
 */
public class RLifeActivity extends BaseActivity{

    @ViewInject(R.id.list)
    private ListView mListView;

    private ItemAdapter mAdapter;

    private List<ResumeLife>    items=new ArrayList<ResumeLife>();

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
        mHeadTitle.setText(getText(R.string.resume_head_4));
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
                Intent intent=new Intent(RLifeActivity.this,RLifeEditActivity.class);
                startActivityForResult(intent,AppConfig.REQUEST_RESUME_LIFE_ADD);
                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode==AppConfig.REQUEST_RESUME_LIFE_ADD){

        }

    }

    @Override
    public void loadData() {
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start", 0);
        requestMap.put("end", 10);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.RESUME_LIFE_LIST, getRequestParams(requestMap), new RequestCallBack<String>() {
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
            ResumeLifeResp resp=(ResumeLifeResp) JsonUtil.ObjFromJson(result, ResumeLifeResp.class);
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
            ResumeLife item=items.get(position);
            if (item!=null){
                holder.title.setText(""+item.getBeginTime()+"-"+item.getEndTime()+" "+item.getOrgName()+"  "+item.getOfficeName());
            }
            return convertView;
        }
    }

    private class ItemView {
        TextView        title;
    }

}
