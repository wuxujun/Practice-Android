package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.OfficeResp;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/27.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.list)
    private ListView        mListView;

    private List<OfficeInfo>    items=new ArrayList<OfficeInfo>();

    private OfficeAdapter   mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        ViewUtils.inject(this);
        initHeadBackView();
        showSearchEditView();
        mHeadEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        mHeadEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    search(mHeadEditText.getText().toString());
                }
                return false;
            }
        });

        mHeadEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                search(textView.getText().toString());
                return false;
            }
        });

        mHeadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                L.e(charSequence.toString() + "   " + i + "   " + i1 + "  " + i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mHeadBtnRight.setText(R.string.btn_search);
        mHeadBtnRight.setOnClickListener(this);

        initView();
    }

    private void initView(){
        mAdapter=new OfficeAdapter(this);
        if (mListView!=null){
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(onItemClickListener);
        }
    }

    public void search(String key){
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start",0);
        requestMap.put("end", "20");
        requestMap.put("keyword",key);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.SEARCH_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
            OfficeResp resp=(OfficeResp) JsonUtil.ObjFromJson(result, OfficeResp.class);
            if (resp.getSuccess()==1){
                items.addAll(resp.getRoot());
            }
            mAdapter.addAll(items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{
                String key=mHeadEditText.getText().toString();
                if (StringUtil.isEmpty(key)){
                    showCroutonMessage("请输入搜索关键字");
                    return;
                }
                search(key);
                break;
            }
            default:
                super.onClick(view);
        }
    }


    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            OfficeInfo officeInfo=items.get(i);
            if (officeInfo!=null) {
                Intent intent = new Intent(SearchActivity.this, OfficeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConfig.PARAM_OFFICE_INFO, officeInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
}
