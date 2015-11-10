package com.xujun.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.Member;
import com.xujun.app.practice.AboutActivity;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.LoginActivity;
import com.xujun.app.practice.MCollectionActivity;
import com.xujun.app.practice.MEvaluationActivity;
import com.xujun.app.practice.MInternshipActivity;
import com.xujun.app.practice.MRequestActivity;
import com.xujun.app.practice.MessageActivity;
import com.xujun.app.practice.R;
import com.xujun.app.practice.RegisterActivity;
import com.xujun.app.practice.SettingActivity;
import com.xujun.app.widget.MyHeadView;
import com.xujun.app.widget.ResumeHeadView;
import com.xujun.pullzoom.PullToZoomListView;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/1.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    public static final String TAG="MyFragment";

    List<String>  items=new ArrayList<String>();


    private MyHeadView      mHeadView;

    private ItemAdapter     mAdapter;

    @ViewInject(R.id.list)
    private ListView   listView;



    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tvLogin:{
                    Log.e(TAG, "------> Login");
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, AppConfig.REQUEST_MY_LOGIN);
                    break;
                }
                case R.id.tvRegister:{
                    Log.e(TAG,"-------> Register");
                    Intent intent=new Intent(getActivity(), RegisterActivity.class);
                    startActivityForResult(intent, AppConfig.REQUEST_MY_REGISTER);
                    break;
                }
                case R.id.layout_view:{
                    if (mMember!=null){
                        UIHelper.openMy(getActivity());
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.activity_list,null);
        ViewUtils.inject(this,mContentView);
        mHeadView=new MyHeadView(mContext,mClickListener);
        listView.addHeaderView(mHeadView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return mContentView;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode==AppConfig.REQUEST_MY_LOGIN){
            if (resultCode==AppConfig.SUCCESS){
                Member  member=(Member)data.getSerializableExtra(AppConfig.PARAM_MEMBER);
                mHeadView.getActionLayout().setVisibility(View.INVISIBLE);
                if (member!=null) {
                    mMember=member;
                    mHeadView.getUserName().setText(member.getMobile());
                }
            }
        }else if (requestCode==AppConfig.REQUEST_MY_REGISTER){
            if (resultCode==AppConfig.SUCCESS){
                Member  member=(Member)data.getSerializableExtra(AppConfig.PARAM_MEMBER);
                mHeadView.getActionLayout().setVisibility(View.INVISIBLE);
                if (member!=null) {
                    mMember=member;
                    mHeadView.getUserName().setText(member.getMobile());
                }
            }
        }else if(requestCode==AppConfig.REQUEST_MY_SETTING){
            if (requestCode==AppConfig.SUCCESS){
                mHeadView.getActionLayout().setVisibility(View.VISIBLE);
                mHeadView.getUserName().setText("未登录");
            }
        } else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void loadData(){
        items.clear();
        items.add("0");
        items.add("已申请的");
        items.add("发现更多");
        items.add("消息通知");
        items.add("设置");
        items.add("关于我们");
        mAdapter.notifyDataSetChanged();

        String  loginFlag=mAppContext.getProperty(AppConfig.CONF_LOGIN_FLAG);
        if (!StringUtil.isEmpty(loginFlag)){
            L.e(".........."+loginFlag);
            if (loginFlag.equals("1")){
                mHeadView.getActionLayout().setVisibility(View.INVISIBLE);
                Member member=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
                if (member!=null){
                    mMember=member;
                    L.e("--------->"+member.getMobile());
                    mHeadView.getUserName().setText(member.getMobile());
                }
            }else{
                mHeadView.getActionLayout().setVisibility(View.VISIBLE);
                mHeadView.getUserName().setText("未登录");
            }
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e(TAG,"---------->"+position);
        switch (position){
            case 1:{
                break;
            }
            case 2:{
                mMember=(Member)mAppContext.readObject(AppConfig.OBJECT_MEMBER);
                if (mMember!=null) {
                    UIHelper.openMyRequest(getActivity());
                }else {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, AppConfig.REQUEST_MY_LOGIN);
                }
                break;
            }
            case 4:{
                Intent intent=new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            }
            case 5:{
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent,AppConfig.REQUEST_MY_SETTING);
                break;
            }
            case 6:{
                Intent intent=new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    static class ItemView
    {
        public ImageView        icon;
        public TextView         title;

        public LinearLayout     head;
        public LinearLayout     item;
    }

    class ItemAdapter extends BaseAdapter{


        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView holder;
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.my_listview_item,null);
                holder=new ItemView();
                holder.head=(LinearLayout)convertView.findViewById(R.id.llHead);
                holder.item=(LinearLayout)convertView.findViewById(R.id.llItem);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.icon=(ImageView)convertView.findViewById(R.id.ivIcon);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            if (items.get(position).equals("0")){
                holder.item.setVisibility(View.GONE);
                holder.head.findViewById(R.id.tv_my_btn_0).setOnClickListener(mMyClickListener);
                holder.head.findViewById(R.id.tv_my_btn_1).setOnClickListener(mMyClickListener);
                holder.head.findViewById(R.id.tv_my_btn_2).setOnClickListener(mMyClickListener);
            }else {
                holder.head.setVisibility(View.GONE);
                if (items.get(position).equals("消息通知")) {
                    holder.icon.setImageResource(R.drawable.ic_my_message);
                }else if (items.get(position).equals("设置")) {
                    holder.icon.setImageResource(R.drawable.ic_my_setting);
                }else if (items.get(position).equals("关于我们")) {
                    holder.icon.setImageResource(R.drawable.ic_my_about);
                }else if (items.get(position).equals("发现更多")) {
                    holder.icon.setImageResource(R.drawable.ic_my_more);
                }else if (items.get(position).equals("已申请的")) {
                    holder.icon.setImageResource(R.drawable.ic_my_resume);
                }
                holder.title.setText(items.get(position));
            }
            return convertView;
        }
    }

    private View.OnClickListener mMyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mMember==null){
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, AppConfig.REQUEST_MY_LOGIN);
                return;
            }
            switch (view.getId()){
                case R.id.tv_my_btn_0:{
                    Intent intent=new Intent(getActivity(), MInternshipActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.tv_my_btn_1:{
                    Intent intent=new Intent(getActivity(), MCollectionActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.tv_my_btn_2:{
                    Intent intent=new Intent(getActivity(), MEvaluationActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };
}
