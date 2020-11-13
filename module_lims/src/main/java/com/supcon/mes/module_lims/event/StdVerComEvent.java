package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/11/12
 * class name
 */
public class StdVerComEvent extends BaseEntity {

    public StdVerComEvent(List<StdVerComIdEntity> list) {
        this.list = list;
    }

    private List<StdVerComIdEntity> list;

    public List<StdVerComIdEntity> getList() {
        return list;
    }

    public void setList(List<StdVerComIdEntity> list) {
        this.list = list;
    }
}
