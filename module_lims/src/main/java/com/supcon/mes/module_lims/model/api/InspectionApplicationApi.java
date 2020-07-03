package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 检验申请接口
 */
@ContractFactory(entites = {InspectionApplicationListEntity.class})
public interface InspectionApplicationApi {
    void getInspectionApplicationList(String type, int pageNo, Map<String, Object> params);
}
