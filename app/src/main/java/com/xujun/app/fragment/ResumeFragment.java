package com.xujun.app.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.practice.R;
import com.xujun.app.widget.CustGridView;
import com.xujun.app.widget.ResumeHeadView;
import com.xujun.pullzoom.PullToZoomListView;

/**
 * Created by xujunwu on 15/8/26.
 */
public class ResumeFragment extends BaseFragment{

    @ViewInject(R.id.llHead)
    private LinearLayout    mHeadLinearLayout;

    @ViewInject(R.id.gridview)
    private CustGridView mListView;


    private ResumeHeadView  mHeadView;

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mHeadView=new ResumeHeadView(mContext,mClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveinstanceState){
        mContentView=inflater.inflate(R.layout.resume_listview,null);
        ViewUtils.inject(this,mContentView);

        mHeadLinearLayout.removeAllViews();;
        mHeadLinearLayout.addView(mHeadView);
        return mContentView;
    }



    @Override
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView==null){

            }
            return convertView;
        }
    }

    class ItemView{
        public ImageView    icon;
        public TextView     title;
    }
}
