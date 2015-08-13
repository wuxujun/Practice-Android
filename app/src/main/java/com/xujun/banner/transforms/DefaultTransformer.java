package com.xujun.banner.transforms;

import android.view.View;

/**
 * Created by xujunwu on 15/8/6.
 */
public class DefaultTransformer extends BaseTranformer{
    @Override
    protected void onTransform(View view, float position) {
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }
}
