package com.xujun.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by xujunwu on 15/8/27.
 */
public class CustGridView extends GridView{
    public CustGridView(Context context) {
        super(context);
    }

    public CustGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustGridView(Context context, AttributeSet attrs, int defStyleAttr) {
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
