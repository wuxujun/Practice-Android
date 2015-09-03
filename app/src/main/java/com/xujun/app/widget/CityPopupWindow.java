package com.xujun.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/8/27.
 */
public class CityPopupWindow extends PopupWindow{

    private CustGridView mGridView;

    private LinearLayout    mMoreCityLayout;
    private TextView        mCurrentCityTextView;

    public CityPopupWindow(Context context,View.OnClickListener clickHandler){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(R.layout.popup_city_choose,null);
        setContentView(interalView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        mMoreCityLayout=(LinearLayout)interalView.findViewById(R.id.more_city_linearlayout);
        mMoreCityLayout.setOnClickListener(clickHandler);

        mCurrentCityTextView=(TextView)interalView.findViewById(R.id.current_city);
        mGridView=(CustGridView)interalView.findViewById(R.id.gridview);
    }

    public CustGridView  getGridView(){
        return mGridView;
    }

    public TextView getCurrentCityTextView(){
        return mCurrentCityTextView;
    }
}
