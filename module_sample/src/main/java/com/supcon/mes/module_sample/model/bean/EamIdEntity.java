package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class EamIdEntity extends BaseEntity {
    private String code;
    private Long id;
    private String name;
    private BaseIdValueEntity state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseIdValueEntity getState() {
        return state;
    }

    public void setState(BaseIdValueEntity state) {
        this.state = state;
    }
}
