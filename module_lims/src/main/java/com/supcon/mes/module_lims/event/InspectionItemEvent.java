package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
public class InspectionItemEvent extends BaseEntity {
    private List<StdVerComIdEntity> list;

    public List<StdVerComIdEntity> getList() {
        return list;
    }

    public void setList(List<StdVerComIdEntity> list) {
        this.list = list;
    }
}
