package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.SampleIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;

/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
public class SumbitReport extends BaseEntity {
    public SampleIdEntity sampleId;
    public StdVerIdEntity stdVerId;
    public String testResult;
    public int version;
    public String memoField;

}
