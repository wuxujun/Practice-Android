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

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/10/29.
 */
public class RewardPopupWindow extends PopupWindow {

    private ListView mListView;
    private Button  mHourButton;
    private Button  mDayButton;
    private Button  mMonthButton;

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
        mHourButton=(Button)interalView.findViewById(R.id.btnRewardHour);
        mDayButton=(Button)interalView.findViewById(R.id.btnRewardDay);
        mMonthButton=(Button)interalView.findViewById(R.id.btnRewardMonth);
        mHourButton.setOnClickListener(clickListener);
        mDayButton.setOnClickListener(clickListener);
        mMonthButton.setOnClickListener(clickListener);
    }

    public ListView  getListView(){
        return mListView;
    }

    public Button getHourButton() {
        return mHourButton;
    }

    public Button getDayButton() {
        return mDayButton;
    }

    public Button getMonthButton() {
        return mMonthButton;
    }
}
