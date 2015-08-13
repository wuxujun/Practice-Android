package com.xujun.banner.transforms;

import android.view.View;

/**
 * Created by xujunwu on 15/8/6.
 */
public class StackTransformer extends BaseTranformer{
    @Override
    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }
}
