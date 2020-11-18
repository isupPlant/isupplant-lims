package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
public class TestRequestNoEvent extends BaseEntity {
    private List<TestRequestNoEntity> list;

    public TestRequestNoEvent(List<TestRequestNoEntity> list) {
        this.list = list;
    }

    public List<TestRequestNoEntity> getList() {
        return list;
    }

    public void setList(List<TestRequestNoEntity> list) {
        this.list = list;
    }
}
