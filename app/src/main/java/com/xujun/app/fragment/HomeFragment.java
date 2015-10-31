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
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.HomeCateAdapter;
import com.xujun.app.adapter.NetworkImageHolderView;
import com.xujun.app.model.CategoryInfo;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.OfficeInfo;
import com.xujun.app.model.OfficeResp;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.CategoryActivity;
import com.xujun.app.practice.OfficeActivity;
import com.xujun.app.practice.R;
import com.xujun.app.widget.HeadBannerView;
import com.xujun.app.widget.HomeHeadView;
import com.xujun.banner.BViewHolderCreator;
import com.xujun.banner.Banner;
import com.xujun.util.JsonUtil;
import com.xujun.util.L;
import com.xujun.util.URLs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwu on 15/8/1.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    public static final String TAG="HomeFragment";

    List<OfficeInfo> items=new ArrayList<OfficeInfo>();
    List<CategoryInfo> categoryInfos=new ArrayList<CategoryInfo>();

    private ItemAdapter     mAdapter;

    private HomeHeadView    mHeadView;

    @ViewInject(R.id.list)
    private ListView      mListView;



    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mHeadView=new HomeHeadView(mContext);
        mAdapter=new ItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.activity_list,null);
        ViewUtils.inject(this,mContentView);
        mListView.setVerticalScrollBarEnabled(true);
        mListView.addHeaderView(mHeadView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return mContentView;
    }

    public void loadData(){
        List<String>  images=new ArrayList<String>();
        images.add("http://sx.asiainstitute.cn/images/ai1.png");
        images.add("http://sx.asiainstitute.cn/images/ai0.png");
        reloadBannerData(images);
        categoryInfos.clear();

        try{
            DbUtils db=DbUtils.create(mContext, AppConfig.DB_NAME);
            List<CategoryInfo> categoryInfoList=db.findAll(Selector.from(CategoryInfo.class).where("top","=",1));
            if (categoryInfoList!=null&&categoryInfoList.size()>0){
                categoryInfos.addAll(categoryInfoList);
            }
        }catch (DbException e){
            e.printStackTrace();
        }
        reloadGridViewData();

        items.clear();
        Map<String,Object> requestMap=new HashMap<String,Object>();
        requestMap.put("start", "0");
        requestMap.put("end", "20");
        HttpUtils http=new HttpUtils();
        L.e(TAG,URLs.OFFICES_LIST_URL);
        http.send(HttpRequest.HttpMethod.POST, URLs.OFFICES_LIST_URL, getRequestParams(requestMap), new RequestCallBack<String>() {
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
        L.e(TAG,"......."+result);
        try{
            OfficeResp resp=(OfficeResp) JsonUtil.ObjFromJson(result,OfficeResp.class);
            if (resp.getSuccess()==1){
                items.addAll(resp.getRoot());
            }
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadBannerData(List<String>  images){
        mHeadView.getBanner().setPages(new BViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, images).setPageIndicator(new int[]{R.drawable.ic_page_indicator,R.drawable.ic_page_indicator_focused}).setPageTransformer(Banner.Transformer.DefaultTransformer);

    }

    private void reloadGridViewData(){

        mHeadView.getGridView().setAdapter(new HomeCateAdapter(mContext,categoryInfos));
        mHeadView.getGridView().setOnItemClickListener(mGridViewClickListener);
    }

    private AdapterView.OnItemClickListener mGridViewClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CategoryInfo categoryInfo=categoryInfos.get(i);
            if (categoryInfo!=null){
                Intent intent=new Intent(getSherlockActivity(), CategoryActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConfig.PARAM_CITY_INFO,cityInfo);
                bundle.putSerializable(AppConfig.PARAM_CATEGORY_INFO,categoryInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        OfficeInfo officeInfo=items.get(i-1);
        if (officeInfo!=null) {
            Intent intent = new Intent(getSherlockActivity(), OfficeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConfig.PARAM_OFFICE_INFO, officeInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
               convertView= LayoutInflater.from(mContext).inflate(R.layout.home_listview_item,null);
                holder=new ItemView();
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            OfficeInfo info=items.get(i);
            if (info!=null){
                holder.title.setText(info.getName());
            }

            return convertView;
        }
    }
}
