package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.OfficeResp;
import com.xujun.app.model.SearchHisEntity;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import java.security.Key;
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

    @ViewInject(R.id.gridview)
    private GridView        mGridView;

    @ViewInject(R.id.btnClear)
    private Button          mClearBtn;

    private List<SearchHisEntity>    items=new ArrayList<SearchHisEntity>();

    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
                if (i == KeyEvent.KEYCODE_ENTER&&KeyEvent.ACTION_DOWN==keyEvent.getAction()) {
                    search(mHeadEditText.getText().toString());
                }
                return false;
            }
        });

        mHeadEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                L.e(".......","..........======>"+i);
                if (KeyEvent.KEYCODE_SEARCH==i&&KeyEvent.ACTION_DOWN==keyEvent.getAction()) {
                    search(textView.getText().toString());
                }
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

        mHeadBtnRight.setText(R.string.btn_cancel);
        mHeadBtnRight.setOnClickListener(this);

        initView();
    }

    private void initView(){
        mAdapter=new ItemAdapter();
        if (mListView!=null){
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(onItemClickListener);
        }

        mClearBtn.setOnClickListener(this);
    }

    public void search(String key){
        try{
            DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
            SearchHisEntity entity=new SearchHisEntity();
            entity.setTitle(key);
            entity.setAddtime(System.currentTimeMillis());
            db.saveOrUpdate(entity);


            Intent intent=new Intent(SearchActivity.this,SearchResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConfig.PARAM_SEARCH_HIS, entity);
            intent.putExtras(bundle);
            startActivity(intent);
        }catch (DbException e){
            e.printStackTrace();
        }

    }

    @Override
    public void loadData() {
        try {
            DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
            List<SearchHisEntity> searchHisEntityList = db.findAll(SearchHisEntity.class);
            if (searchHisEntityList != null && searchHisEntityList.size() > 0) {
                items.clear();
                items.addAll(searchHisEntityList);
            }
            mAdapter.notifyDataSetChanged();
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {
//        try{
//            OfficeResp resp=(OfficeResp) JsonUtil.ObjFromJson(result, OfficeResp.class);
//            if (resp.getSuccess()==1){
//                items
//            }
//            mAdapter.addAll(items);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{
                finish();
//                String key=mHeadEditText.getText().toString();
//                if (StringUtil.isEmpty(key)){
//                    showCroutonMessage("请输入搜索关键字");
//                    return;
//                }
//                search(key);
                break;
            }
            case R.id.btnClear:{
                try {
                    DbUtils db = DbUtils.create(this, AppConfig.DB_NAME);
                    List<SearchHisEntity> searchHisEntityList = db.findAll(SearchHisEntity.class);
                    if (searchHisEntityList != null && searchHisEntityList.size() > 0) {
                        items.clear();
                        db.deleteAll(searchHisEntityList);
                    }
                    mAdapter.notifyDataSetChanged();
                }catch (DbException e){
                    e.printStackTrace();
                }
                break;
            }
            default:
                super.onClick(view);
        }
    }


    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SearchHisEntity entity=items.get(i);
            if (entity!=null) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConfig.PARAM_SEARCH_HIS, entity);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };


    private class ItemAdapter extends BaseAdapter {

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
                convertView=View.inflate(mContext,R.layout.item_search_his,null);
                holder.title=(TextView)convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            SearchHisEntity item=items.get(position);
            if (item!=null){
                holder.title.setText(item.getTitle());
            }
            return convertView;
        }
    }

    class ItemView{
        public TextView     title;

    }
}
