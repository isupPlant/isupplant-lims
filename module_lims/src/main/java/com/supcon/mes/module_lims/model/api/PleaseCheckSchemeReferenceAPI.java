package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.PleaseCheckSchemeListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name
 */
@ContractFactory(entites = {PleaseCheckSchemeListEntity.class})
public interface PleaseCheckSchemeReferenceAPI {
    void getPleaseCheckSchemeList(int pageNo, String id, Map<String, Object> map);
}
