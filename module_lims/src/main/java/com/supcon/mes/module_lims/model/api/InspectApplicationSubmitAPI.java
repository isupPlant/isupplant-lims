package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/23
 * class name
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface InspectApplicationSubmitAPI {

    void submitInspectApplication(String path, Map<String,Object> params, InspectApplicationSubmitEntity entity);
}
