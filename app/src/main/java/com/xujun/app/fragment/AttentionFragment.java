package com.xujun.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xujun.app.adapter.CategoryCheckBoxAdapter;
import com.xujun.app.adapter.OfficeAdapter;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.AttentionSetActivity;
import com.xujun.app.practice.OfficeActivity;
import com.xujun.app.practice.R;
import com.xujun.app.widget.CategoryPopupWindow;
import com.xujun.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG="AttentionFragment";

    List<OfficeInfo> items=new ArrayList<OfficeInfo>();

    private OfficeAdapter mAdapter;

    @ViewInject(R.id.tvTitle)
    private TextView        mSetTextView;

    @ViewInject(R.id.list)
    private ListView      mListView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new OfficeAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.attention_listview,null);
        ViewUtils.inject(this, mContentView);

        mSetTextView.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OfficeInfo officeInfo=items.get(i);
                if (officeInfo!=null) {
                    Intent intent = new Intent(getSherlockActivity(), OfficeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConfig.PARAM_OFFICE_INFO, officeInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return mContentView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvTitle:{
                Intent intent=new Intent(getActivity(), AttentionSetActivity.class);
                startActivityForResult(intent,AppConfig.REQUEST_ATTENTION_SET);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == AppConfig.REQUEST_ATTENTION_SET) {
            if (resultCode==AppConfig.SUCCESS){


            }
        }
    }

    @Override
    public void loadData(){
        items.clear();
        OfficeInfo info=new OfficeInfo();
        info.setId(1);
        info.setName("测试工程师");
        items.add(info);
        info=new OfficeInfo();
        info.setId(2);
        info.setName("销售人员");
        items.add(info);
        info=new OfficeInfo();
        info.setId(3);
        info.setName("销售人员");
        items.add(info);
        info=new OfficeInfo();
        info.setId(4);
        info.setName("销售人员");
        items.add(info);
        info=new OfficeInfo();
        info.setId(5);
        info.setName("销售人员");
        items.add(info);

        mAdapter.addAll(items);
    }

    @Override
    public void parserHttpResponse(String result) {

    }


}
