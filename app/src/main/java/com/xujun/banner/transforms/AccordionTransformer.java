package com.xujun.banner.transforms;

import android.view.View;

/**
 * Created by xujunwu on 15/8/6.
 */
public class AccordionTransformer extends BaseTranformer{
    @Override
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0 ? 0 : view.getWidth());
        view.setScaleX(position < 0 ? 1f + position : 1f - position);
    }
}
