package com.xujun.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.CityInfo;
import com.xujun.app.model.MenuInfo;
import com.xujun.app.practice.AppConfig;
import com.xujun.app.practice.R;
import com.xujun.app.practice.RHonorActivity;
import com.xujun.app.practice.RInfoActivity;
import com.xujun.app.practice.RLifeActivity;
import com.xujun.app.practice.RPhotoActivity;
import com.xujun.app.practice.RTemplateActivity;
import com.xujun.app.practice.RWorkActivity;
import com.xujun.app.widget.CustGridView;
import com.xujun.app.widget.ResumeHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/8/26.
 */
public class ResumeFragment extends BaseFragment{

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeadLinearLayout;

    @ViewInject(R.id.gridview)
    private CustGridView mListView;

    private ItemAdapter     mAdapter;
    private List<MenuInfo> items=new ArrayList<MenuInfo>();

    private ResumeHeadView  mHeadView;

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnResume1:{
                    Intent intent=new Intent(getActivity(), RInfoActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnResume3:{

                    Intent intent=new Intent(getActivity(), RWorkActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnResume4:{

                    Intent intent=new Intent(getActivity(), RPhotoActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnResume2:{

                    Intent intent=new Intent(getActivity(), RLifeActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btnResume5:{

                    Intent intent=new Intent(getActivity(), RHonorActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MenuInfo menuInfo=items.get(i);
            if (menuInfo!=null){
                Intent intent=new Intent(getActivity(), RTemplateActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConfig.PARAM_MENU_INFO,menuInfo);
                intent.putExtras(bundle);
                startActivity(intent);
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
        mContentView=inflater.inflate(R.layout.resume_listview,null);
        ViewUtils.inject(this,mContentView);
        mHeadView=new ResumeHeadView(mContext,mClickListener);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        mHeadLinearLayout.addView(mHeadView, lp);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        return mContentView;
    }


    public void onResume(){
        super.onResume();
        loadData();
    }

    @Override
    public void loadData() {
        items.clear();

        MenuInfo    menuInfo=new MenuInfo(1);
        menuInfo.setTitle("简约直白");
        items.add(menuInfo);

        menuInfo=new MenuInfo(2);
        menuInfo.setTitle("霸气豪爽");
        items.add(menuInfo);

        menuInfo=new MenuInfo(3);
        menuInfo.setTitle("清新婉约");
        items.add(menuInfo);

        menuInfo=new MenuInfo(4);
        menuInfo.setTitle("校园型");
        items.add(menuInfo);

        menuInfo=new MenuInfo(5);
        menuInfo.setTitle("实习型");
        items.add(menuInfo);

        menuInfo=new MenuInfo(6);
        menuInfo.setTitle("荣誉型");
        items.add(menuInfo);

       mAdapter.notifyDataSetChanged();

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    class ItemAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemView    holder;
            if (convertView==null){
                holder=new ItemView();
                convertView=View.inflate(mContext, R.layout.resume_listview_item,null);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            MenuInfo info=items.get(position);
            if (info!=null){
                holder.title.setText(info.getTitle());
            }
            return convertView;
        }
    }

    class ItemView{
        public ImageView    icon;
        public TextView     title;
    }
}
