package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.InputList;
import com.xujun.app.model.Member;
import com.xujun.app.widget.ToggleButton;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/9/6.
 */
public class SettingActivity extends BaseActivity{

    @ViewInject(R.id.list)
    private ListView mListView;

    @ViewInject(R.id.llFooter)
    private LinearLayout        mFooter;

    private ItemAdapter         mAdapter;

    private List<InputInfo> items=new ArrayList<InputInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewUtils.inject(this);

        mHeadTitle.setText(getText(R.string.my_setting));
        initHeadBackView();
        hideSearchEditView();

        initView();
    }

    private void initView(){
        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        Member member=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
        if (member!=null) {
            View logoutView = getLayoutInflater().inflate(R.layout.item_logout, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            mFooter.addView(logoutView, lp);
            logoutView.findViewById(R.id.btnLogout).setOnClickListener(mLogoutClickListener);
        }
    }

    @Override
    public void loadData() {
        try {
            InputList list=(InputList) JsonUtil.ObjFromJson(getFromAssets("setting.json"), InputList.class);
            if (list!=null&&list.getRoot()!=null){
                items.clear();
                items.addAll(list.getRoot());
            }
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    private View.OnClickListener    mLogoutClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAppContext.deleteObject(AppConfig.OBJECT_MEMBER);
            mAppContext.setProperty(AppConfig.CONF_LOGIN_FLAG,"0");
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            intent.putExtra(AppConfig.PARAM_LOGOUT_RESULT,"1");
            intent.putExtras(bundle);
            SettingActivity.this.setResult(AppConfig.SUCCESS, intent);
            SettingActivity.this.finish();
        }
    };

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
                convertView=View.inflate(mContext,R.layout.item_setting,null);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.toggleButton=(ToggleButton)convertView.findViewById(R.id.swType);
                holder.enter=(ImageView)convertView.findViewById(R.id.ivEnter);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.enter.setVisibility(View.GONE);
            InputInfo item=items.get(position);
            if (item!=null){
                if (item.getType()==10) {
                    holder.title.setText(item.getTitle());
                } else if (item.getType()==11){
                    holder.title.setText(item.getTitle());
                    holder.toggleButton.setVisibility(View.GONE);
                    holder.enter.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }
    }

    private class ItemView {
        TextView        title;
        ToggleButton    toggleButton;
        ImageView       enter;
    }



}
