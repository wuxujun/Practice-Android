package com.xujun.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xujun.banner.salvage.RecyclingPagerAdapter;

import java.util.List;

/**
 * Created by xujunwu on 15/8/6.
 */
public class BPageAdapter<T> extends RecyclingPagerAdapter {
    protected List<T> mDatas;
    protected BViewHolderCreator holderCreator;

    public BPageAdapter(BViewHolderCreator holderCreator,List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    @Override public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(holder);
        } else {
            holder = (Holder<T>) view.getTag();
        }
        holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }

    @Override public int getCount() {
        return mDatas.size();
    }

    /**
     * @param <T> 任何你指定的对象
     */
    public interface Holder<T>{
        View createView(Context context);
        void UpdateUI(Context context,int position,T data);
    }
}
