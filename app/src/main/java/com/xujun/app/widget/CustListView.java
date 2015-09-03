package com.xujun.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by xujunwu on 15/8/27.
 */
public class CustListView extends ListView {

    public CustListView(Context context) {
        super(context);
    }


    public CustListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.dispatchTouchEvent(event);
    }
}
