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
 * Created by xujunwu on 15/8/27.
 */
public class AttentionPopupWindow extends PopupWindow {

    private ListView        mListView;

    public AttentionPopupWindow(Context context,View.OnClickListener clickHandler){
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
        mListView=(ListView)interalView.findViewById(R.id.list);
    }

    public ListView getListView(){
        return mListView;
    }

}
