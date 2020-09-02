package com.supcon.mes.module_sample.listener;

import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/9/2
 * class name
 */
public interface InspectionSubRefreshListener {
    void refreshOver(int position, List<InspectionItemsEntity> list);
}
