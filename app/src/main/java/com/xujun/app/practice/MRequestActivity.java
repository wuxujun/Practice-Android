package com.xujun.app.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.OfficeResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已申请的
 * Created by xujunwu on 15/9/6.
 */
public class MRequestActivity extends BaseActivity{

    private List<OfficeInfo>        items=new ArrayList<OfficeInfo>();

    @ViewInject(R.id.list)
    private ListView        mListView;

    private OfficeAdapter    mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewUtils.inject(this);

        mHeadTitle.setText("已申请的");
        initHeadBackView();
        hideSearchEditView();
        initView();
    }


    private void initView(){
        mAdapter=new OfficeAdapter(this);
        if (mListView!=null){
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void loadData() {
        items.clear();
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start", "0");
        requestMap.put("end", "20");
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.MY_OFFICE_REQ_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
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

}
