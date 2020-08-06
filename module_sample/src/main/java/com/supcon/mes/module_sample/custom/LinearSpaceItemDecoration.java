package com.supcon.mes.module_sample.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.supcon.common.view.util.DisplayUtil;

/**
 * author huodongsheng
 * on 2020/8/3
 * class name
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    public LinearSpaceItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childLayoutPosition = parent.getChildAdapterPosition(view);
        if (childLayoutPosition == 0) {
            outRect.set(DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
        } else {
            outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
        }
    }
}
