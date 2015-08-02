package com.xujun.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.practice.AppContext;
import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/8/1.
 */
public class BaseFragment extends SherlockFragment{
    protected Context       mContext;
    protected AppContext    mAppContext;

    protected View          mContentView;

    @ViewInject(R.id.list)
    protected ListView      mListView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mContext=getSherlockActivity().getApplicationContext();
        mAppContext=(AppContext)getSherlockActivity().getApplication();
    }
}
