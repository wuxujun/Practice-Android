package com.xujun.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CategoryResp;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.CategoryActivity;
import com.xujun.app.practice.R;
import com.xujun.app.widget.CategoryHeadView;
import com.xujun.app.widget.RoundedLetterView;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.StringUtil;
import com.xujun.util.URLs;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/1.
 */
public class CategoryFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    public static final String TAG="TopFragment";

    private CategoryHeadView    headView;

    List<CategoryInfo> items=new ArrayList<CategoryInfo>();

    private ItemAdapter     mAdapter;

    private String          mHeadTabType="10";
    private String          mHeadCategory="1010";

    @ViewInject(R.id.list)
    private ListView      mListView;

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeadLinearLayout;

    public void setHeadTabType(String type){
        this.mHeadTabType=type;
    }

    private View.OnClickListener  mHeadClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            headView.getCategory1().setTextColor(getResources().getColor(R.color.btn_color));
            headView.getCategory2().setTextColor(getResources().getColor(R.color.btn_color));
            headView.getCategory3().setTextColor(getResources().getColor(R.color.btn_color));
            headView.getCategory4().setTextColor(getResources().getColor(R.color.btn_color));
            switch (view.getId()){
                case R.id.tvCategory1:{
                    headView.getCategory1().setTextColor(getResources().getColor(R.color.black));
                    mHeadCategory="1010";
                    break;
                }
                case R.id.tvCategory2:{
                    headView.getCategory2().setTextColor(getResources().getColor(R.color.black));
                    mHeadCategory="1020";
                    break;
                }
                case R.id.tvCategory3:{
                    headView.getCategory3().setTextColor(getResources().getColor(R.color.black));
                    mHeadCategory="1030";
                    break;
                }
                case R.id.tvCategory4:{
                    headView.getCategory4().setTextColor(getResources().getColor(R.color.black));
                    mHeadCategory="1040";
                    break;
                }
            }
            loadData();
        }
    };

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.fragment_list,null);
        ViewUtils.inject(this,mContentView);
        headView=new CategoryHeadView(mContext,mHeadClickListener);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        mHeadLinearLayout.addView(headView, lp);
        headView.getCategory1().setTextColor(getResources().getColor(R.color.black));

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return mContentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    public void loadData(){
        items.clear();
        try{
            DbUtils db=DbUtils.create(mContext, AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("parent_code","=",mHeadCategory));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                items.addAll(categoryInfoList);
            }
            mAdapter.notifyDataSetChanged();
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CategoryInfo categoryInfo=items.get(i);
        if (categoryInfo!=null){
            Intent intent=new Intent(getSherlockActivity(), CategoryActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable(AppConfig.PARAM_CATEGORY_INFO,categoryInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    static class ItemView
    {
        public RoundedLetterView icon;

        public TextView title;
    }

    class ItemAdapter extends BaseAdapter {

        @Override
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

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ItemView        holder;
            if (convertView==null){
                convertView= LayoutInflater.from(mContext).inflate(R.layout.category_listview_item,null);
                holder=new ItemView();
                holder.icon=(RoundedLetterView)convertView.findViewById(R.id.rlName);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            CategoryInfo info=items.get(i);
            if (info!=null) {
                holder.title.setText(info.getCategory());
                holder.icon.setTitleText(info.getCategory().substring(0, 1));
            }
            return convertView;
        }
    }
}
