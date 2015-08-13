package com.xujun.app.practice;

import android.os.Bundle;
import android.view.View;

/**
 * Created by xujunwu on 15/8/7.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mHeadBack.setImageDrawable(getResources().getDrawable(R.drawable.back));
        mHeadBack.setOnClickListener(this);
        mHeadTitle.setText(getText(R.string.register));
        mHeadBtnLeft.setVisibility(View.GONE);

        mHeadBtnRight.setText(getText(R.string.clear));
        mHeadBtnRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHeadBack:{
                finish();
                break;
            }
            case R.id.btnHeadRight:{

                break;
            }
        }
    }
}
