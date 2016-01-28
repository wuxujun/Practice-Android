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
 * Created by xujunwu on 15/8/26.
 */
public class CategoryPopupWindow extends PopupWindow{

    private ListView mListView;
    private LinearLayout    mHeader;
    private TextView        mTitleView;
    private Button          mDoneButton;
    private Button          mCancelButton;


    public CategoryPopupWindow(Context context,int resid){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(resid,null);

        setContentView(interalView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        mListView=(ListView)interalView.findViewById(R.id.list);
        mHeader=(LinearLayout)interalView.findViewById(R.id.llHeader);
        mTitleView=(TextView)interalView.findViewById(R.id.tvTitle);
        mDoneButton=(Button)interalView.findViewById(R.id.btnDone);
        mCancelButton=(Button)interalView.findViewById(R.id.btnCancel);

        mHeader.setVisibility(View.GONE);
        mDoneButton.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
    }

    public ListView  getListView(){
        return mListView;
    }

    public LinearLayout getHeader(){
        return mHeader;
    }

    public TextView getTitleView(){
        return mTitleView;
    }

    public Button   getDoneButton(){
        return mDoneButton;
    }

    public Button   getCancelButton(){
        return mCancelButton;
    }
}
