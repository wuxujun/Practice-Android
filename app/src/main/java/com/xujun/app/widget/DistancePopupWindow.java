package com.xujun.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Button mNearbyButton;
    private Button mAreaButton;
    private Button  mSubwayButton;

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
        mNearbyButton=(Button)interalView.findViewById(R.id.btnDistanceNearby);
        mAreaButton=(Button)interalView.findViewById(R.id.btnDistanceArea);
        mSubwayButton=(Button)interalView.findViewById(R.id.btnDistanceSubway);
        mNearbyButton.setOnClickListener(clickListener);
        mAreaButton.setOnClickListener(clickListener);
        mSubwayButton.setOnClickListener(clickListener);
    }

    public ListView  getListView(){
        return mListView;
    }

    public Button getNearbyButton() {
        return mNearbyButton;
    }

    public Button getAreaButton() {
        return mAreaButton;
    }

    public Button getSubwayButton() {
        return mSubwayButton;
    }
}
