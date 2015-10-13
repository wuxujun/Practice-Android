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

import com.andreabaccega.widget.FormEditText;
import com.xujun.app.practice.R;

import org.w3c.dom.Text;

/**
 * Created by xujunwu on 15/8/27.
 */
public class EditViewPopupWindow extends PopupWindow {

    private TextView    titleView;
    private FormEditText        editText;
    private Button              doneButton;


    public EditViewPopupWindow(Context context, View.OnClickListener clickHandler){
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interalView=inflater.inflate(R.layout.popup_input_choose,null);
        setContentView(interalView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        titleView=(TextView)interalView.findViewById(R.id.tvTitle);
        editText=(FormEditText)interalView.findViewById(R.id.etInputValue);
        doneButton=(Button)interalView.findViewById(R.id.btnDone);
        doneButton.setOnClickListener(clickHandler);
    }

    public TextView getTitleView(){
        return titleView;
    }

    public Button getDoneButton() {
        return doneButton;
    }

    public FormEditText getEditText() {
        return editText;
    }
}
