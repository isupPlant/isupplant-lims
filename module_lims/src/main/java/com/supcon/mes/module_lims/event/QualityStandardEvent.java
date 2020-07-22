package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name
 */
public class QualityStandardEvent extends BaseEntity {
    private List<QualityStandardReferenceEntity> list;

    public QualityStandardEvent(List<QualityStandardReferenceEntity> list) {
        this.list = list;
    }

    public List<QualityStandardReferenceEntity> getList() {
        return list;
    }

    public void setList(List<QualityStandardReferenceEntity> list) {
        this.list = list;
    }
}
