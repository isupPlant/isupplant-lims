package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * author huodongsheng
 * on 2020/11/9
 * class name
 */
public class QualityStdConclusionEntity extends BaseEntity {
    private String name;
    private Integer sort;
    private BaseIdValueEntity stdGrade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public BaseIdValueEntity getStdGrade() {
        return stdGrade;
    }

    public void setStdGrade(BaseIdValueEntity stdGrade) {
        this.stdGrade = stdGrade;
    }
}
