package com.xujun.app.practice;

import android.content.Context;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by xujunwu on 15/7/31.
 */
public class BaseActivity extends SherlockActivity{

    protected AppContext    mAppContext;
    protected Context       mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
