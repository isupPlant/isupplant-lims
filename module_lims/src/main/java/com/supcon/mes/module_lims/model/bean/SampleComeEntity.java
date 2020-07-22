package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;


/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
public class SampleComeEntity extends BaseEntity {

    public String comName;
    public String dispValue;
    public Long id;
    public BaseIntIdNameEntity sampleTestId;
    public BaseIdValueEntity valueKind;
    public BaseIntIdNameEntity testId;

}
