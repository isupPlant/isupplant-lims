package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 样品查询接口
 */
@ContractFactory(entites = {SampleInquiryListEntity.class})
public interface SampleInquiryApi {
    void getSampleInquiryList(String type,int pageNo, Map<String, Object> params);
}
