package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;

import java.util.List;
import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 样品查询接口
 */
@ContractFactory(entites = {SampleInquiryListEntity.class, BAP5CommonEntity.class})
public interface SampleInquiryAPI {
    void getSampleInquiryList(String type,int pageNo, Map<String, Object> params);

    void sampleSubmit(String type, String time, String ids, List<SampleInquiryEntity> submitList);
}
