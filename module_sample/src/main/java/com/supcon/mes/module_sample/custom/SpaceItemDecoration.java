package com.supcon.mes.module_sample.custom;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author huodongsheng
 * on 2020/8/3
 * class name
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int count;
    public SpaceItemDecoration(int space,int count) {
        this.space = space;
        this.count = count;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) % count == 0) {
            outRect.left = 0;
        }
        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1){
            outRect.top = space;
        }
    }
}
