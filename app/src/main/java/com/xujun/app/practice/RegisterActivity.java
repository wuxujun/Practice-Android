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

        mHeadTitle.setText(getText(R.string.register));
        mHeadBtnRight.setText(getText(R.string.clear));
        mHeadBtnRight.setOnClickListener(this);
        initHeadBackView();
        hideSearchEditView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{

                break;
            }
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void loadData(){

    }

    @Override
    public void parserHttpResponse(String result) {

    }

}
