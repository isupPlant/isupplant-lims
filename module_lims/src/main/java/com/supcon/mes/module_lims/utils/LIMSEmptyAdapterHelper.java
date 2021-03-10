package com.supcon.mes.module_lims.utils;

import android.content.Context;

import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.mes.mbap.adapter.RecyclerEmptyAdapter;
import com.supcon.mes.mbap.beans.EmptyViewEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.EmptyViewHelper;

/**
 * author huodongsheng
 * on 2021/3/9
 * class name
 */
public class LIMSEmptyAdapterHelper extends EmptyAdapterHelper {

    public static IListAdapter<EmptyViewEntity> getLIMSRecyclerEmptyAdapter(Context context, String msg){

        LIMSRecyclerEmptyAdapter emptyAdapter = new LIMSRecyclerEmptyAdapter(context);
        emptyAdapter.addData(EmptyViewHelper.createEmptyEntity(msg));

        return emptyAdapter;

    }
}
