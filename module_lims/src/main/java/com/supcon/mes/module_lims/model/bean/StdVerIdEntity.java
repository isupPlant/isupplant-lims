package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
public class StdVerIdEntity extends BaseEntity {
    private Map attrMap;
    private BaseIntIdNameEntity stdId;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public BaseIntIdNameEntity getStdId() {
        return stdId;
    }

    public void setStdId(BaseIntIdNameEntity stdId) {
        this.stdId = stdId;
    }
}
