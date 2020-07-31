package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
public class SampleTestIdEntity extends BaseEntity {
    private Long id;
    private Integer parallelNo;
    private StdVerIdEntity testId;
    private TestMethodIdEntity testMethodId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParallelNo() {
        return parallelNo;
    }

    public void setParallelNo(Integer parallelNo) {
        this.parallelNo = parallelNo;
    }

    public StdVerIdEntity getTestId() {
        return testId;
    }

    public void setTestId(StdVerIdEntity testId) {
        this.testId = testId;
    }

    public TestMethodIdEntity getTestMethodId() {
        return testMethodId;
    }

    public void setTestMethodId(TestMethodIdEntity testMethodId) {
        this.testMethodId = testMethodId;
    }
}
