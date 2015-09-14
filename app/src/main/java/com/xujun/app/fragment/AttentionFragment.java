package com.xujun.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.R;
import com.xujun.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class AttentionFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG="AttentionFragment";

    List<OfficeInfo> items=new ArrayList<OfficeInfo>();

    private ItemAdapter     mAdapter;

    @ViewInject(R.id.tvTitle)
    private TextView        mSetTextView;

    @ViewInject(R.id.llAttentionSet)
    private LinearLayout    mSetContentLinearLayout;

    @ViewInject(R.id.tvAttentionType1)
    private TextView    typeTextView1;
    @ViewInject(R.id.tvAttentionType2)
    private TextView    typeTextView2;
    @ViewInject(R.id.tvAttentionType3)
    private TextView    typeTextView3;
    @ViewInject(R.id.tvAttentionType4)
    private TextView    typeTextView4;
    @ViewInject(R.id.tvAttentionType5)
    private TextView    typeTextView5;
    @ViewInject(R.id.tvAttentionType6)
    private TextView    typeTextView6;
    @ViewInject(R.id.tvAttentionType7)
    private TextView    typeTextView7;
    @ViewInject(R.id.tvAttentionType8)
    private TextView    typeTextView8;
    @ViewInject(R.id.tvAttentionType9)
    private TextView    typeTextView9;
    @ViewInject(R.id.tvAttentionType10)
    private TextView    typeTextView10;
    @ViewInject(R.id.tvAttentionType11)
    private TextView    typeTextView11;

    @ViewInject(R.id.etAttentionKeyword)
    private EditText keywordEditText;


    @ViewInject(R.id.btnAttentionSave)
    private Button     saveAttentionBtn;

    @ViewInject(R.id.list)
    private ListView      mListView;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.attention_listview,null);
        ViewUtils.inject(this,mContentView);
        mSetTextView.setOnClickListener(this);
        mSetContentLinearLayout.setVisibility(View.GONE);
        mListView.setAdapter(mAdapter);

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
                mSetContentLinearLayout.setVisibility(View.VISIBLE);
                mSetTextView.setVisibility(View.GONE);
                break;
            }
        }
    }

    @OnClick({R.id.btnAttentionSave,R.id.tvAttentionType1,R.id.tvAttentionType2})
    public void saveButton(View view){
        switch (view.getId()){
            case R.id.btnAttentionSave:{
                mSetTextView.setVisibility(View.VISIBLE);
                mSetContentLinearLayout.setVisibility(View.GONE);
                break;
            }
            case R.id.tvAttentionType1:{
                L.e("saveButton......");
                break;
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

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    static class ItemView
    {
        public ImageView icon;
        public TextView title;
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
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ItemView        holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.home_listview_item, null);
                holder = new ItemView();
                holder.title = (TextView) convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            } else {
                holder = (ItemView) convertView.getTag();
            }
            return convertView;
        }
    }
}
