package com.xujun.app.practice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by xujunwu on 15/7/31.
 */
public class StartActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);

        ViewUtils.inject(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent=new Intent(StartActivity.this,TabActivity.class);
                startActivity(intent);
                finish();
            }
        },1);
    }
}
