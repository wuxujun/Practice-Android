package com.xujun.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.xujun.app.practice.R;
import com.xujun.banner.BPageAdapter;

/**
 * Created by xujunwu on 15/8/7.
 */
public class NetworkImageHolderView implements BPageAdapter.Holder<String>{

    private BitmapUtils  bitmapUtils;
    private ImageView   imageView;
    @Override
    public View createView(Context context) {
        imageView=new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        bitmapUtils=new BitmapUtils(context);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.drawable.ic_tab_home);
//        ImageLoader.getInstance().displayImage(data,imageView);
        bitmapUtils.display(imageView,data);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
            }
        });
    }
}
