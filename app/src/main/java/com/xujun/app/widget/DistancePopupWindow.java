package com.xujun.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xujun.app.practice.R;

/**
 * Created by xujunwu on 15/10/29.
 */
public class DistancePopupWindow extends PopupWindow {

    private ListView mListView;
    private TextView mNearbyButton;
    private TextView mAreaButton;
    private TextView  mSubwayButton;

    public DistancePopupWindow(Context context,View.OnClickListener clickListener){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(R.layout.popup_distance_choose,null);

        setContentView(interalView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mListView=(ListView)interalView.findViewById(R.id.list);
        mNearbyButton=(TextView)interalView.findViewById(R.id.btnDistanceNearby);
        mAreaButton=(TextView)interalView.findViewById(R.id.btnDistanceArea);
        mSubwayButton=(TextView)interalView.findViewById(R.id.btnDistanceSubway);
        mNearbyButton.setOnClickListener(clickListener);
        mAreaButton.setOnClickListener(clickListener);
        mSubwayButton.setOnClickListener(clickListener);
    }

    public ListView  getListView(){
        return mListView;
    }

    public TextView getNearbyButton() {
        return mNearbyButton;
    }

    public TextView getAreaButton() {
        return mAreaButton;
    }

    public TextView getSubwayButton() {
        return mSubwayButton;
    }
}
