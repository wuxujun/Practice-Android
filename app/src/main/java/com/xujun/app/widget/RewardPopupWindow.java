package com.xujun.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/10/29.
 */
public class RewardPopupWindow extends PopupWindow {

    private ListView mListView;
    private TextView mHourButton;
    private TextView  mDayButton;
    private TextView  mMonthButton;

    public RewardPopupWindow(Context context, View.OnClickListener clickListener){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(R.layout.popup_reward_choose,null);

        setContentView(interalView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mListView=(ListView)interalView.findViewById(R.id.list);
        mHourButton=(TextView)interalView.findViewById(R.id.btnRewardHour);
        mDayButton=(TextView)interalView.findViewById(R.id.btnRewardDay);
        mMonthButton=(TextView)interalView.findViewById(R.id.btnRewardMonth);
        mHourButton.setOnClickListener(clickListener);
        mDayButton.setOnClickListener(clickListener);
        mMonthButton.setOnClickListener(clickListener);
    }

    public ListView  getListView(){
        return mListView;
    }

    public TextView getHourButton() {
        return mHourButton;
    }

    public TextView getDayButton() {
        return mDayButton;
    }

    public TextView getMonthButton() {
        return mMonthButton;
    }
}
