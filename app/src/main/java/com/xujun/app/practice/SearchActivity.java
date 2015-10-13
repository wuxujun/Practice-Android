package com.xujun.app.practice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.model.CityInfo;
import com.xujun.util.L;

/**
 * Created by xujunwu on 15/8/27.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        mCurrentCityInfo=(CityInfo)getIntent().getSerializableExtra(AppConfig.PARAM_CITY_INFO);
        ViewUtils.inject(this);
        initHeadBackView();
        showSearchEditView();
        mHeadEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        mHeadEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    search(mHeadEditText.getText().toString());
                }
                return false;
            }
        });

        mHeadEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                search(textView.getText().toString());
                return false;
            }
        });

        mHeadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                L.e(charSequence.toString() + "   " + i + "   " + i1 + "  " + i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mHeadBtnRight.setText(R.string.btn_cancel);
        mHeadBtnRight.setOnClickListener(this);
    }

    public void search(String key){

    }

    @Override
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHeadRight:{

                break;
            }
            default:
                super.onClick(view);
        }
    }
}
