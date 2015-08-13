package com.xujun.app.practice;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by xujunwu on 15/7/31.
 */
public class BaseActivity extends SherlockActivity{

    protected AppContext    mAppContext;
    protected Context       mContext;


    protected TextView      mHeadTitle;
    protected ImageButton   mHeadBack;
    protected Button        mHeadBtnLeft;
    protected Button        mHeadBtnRight;


    protected ListView      mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext=(AppContext)getApplication();
        mContext=getApplicationContext();

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        View actionbarLayout= LayoutInflater.from(this).inflate(R.layout.custom_actionbar,null);
        mHeadTitle=(TextView)actionbarLayout.findViewById(R.id.tvHeadTitle);
        mHeadBack=(ImageButton)actionbarLayout.findViewById(R.id.ibHeadBack);
        mHeadBtnLeft=(Button)actionbarLayout.findViewById(R.id.btnHeadLeft);
        mHeadBtnLeft.setVisibility(View.GONE);
        mHeadBtnRight=(Button)actionbarLayout.findViewById(R.id.btnHeadRight);
        getActionBar().setCustomView(actionbarLayout);
    }


    public boolean dispatchKeyEvent(KeyEvent event){
        int keyCode=event.getKeyCode();
        if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
