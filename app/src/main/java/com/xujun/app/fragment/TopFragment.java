package com.xujun.app.fragment;

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
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CategoryResp;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.practice.R;
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
public class TopFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG="TopFragment";

    List<CategoryInfo> items=new ArrayList<CategoryInfo>();

    private ItemAdapter     mAdapter;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.fragment_list,null);
        mListView=(ListView)mContentView.findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        return mContentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    public void loadData(){
        items.clear();
        Map<String,Object> requestMap=new HashMap<String,Object>();

        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URLs.CATEGORY_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                L.e("onSuccess() "+responseInfo.result);
                parserHttpResponse(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                L.i("onFailure() "+s);
            }
        });

    }

    @Override
    public void parserHttpResponse(String result) {
        try{
            CategoryResp resp=(CategoryResp)JsonUtil.ObjFromJson(result,CategoryResp.class);
            if (resp.getRoot()!=null){
                items.addAll(resp.getRoot());
                mAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

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
