package com.xujun.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/8/26.
 */
public class MenuPopupWindow extends PopupWindow{

    private CustListView        mListView;

    public MenuPopupWindow(Context context){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(R.layout.popup_menu_choose,null);
        setContentView(interalView);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mListView=(CustListView)interalView.findViewById(R.id.list);
    }

    public CustListView  getListView(){
        return mListView;
    }
}
