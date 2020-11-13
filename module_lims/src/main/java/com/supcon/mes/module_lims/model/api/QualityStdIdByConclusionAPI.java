package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;

/**
 * author huodongsheng
 * on 2020/11/9
 * class name
 */
@ContractFactory(entites = {BAP5CommonListEntity.class})
public interface QualityStdIdByConclusionAPI {
    void getStdVerGradesByStdVerId(String stdVersionId);
}
