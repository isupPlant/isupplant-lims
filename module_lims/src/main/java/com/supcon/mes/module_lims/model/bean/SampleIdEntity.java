package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/8
 * class name
 */
public class SampleIdEntity extends BaseEntity {
    private Map attrMap;
    private String batchCode;
    private String code;
    private Long id;
    private String name;
    private ProdIdEntity productId;
    private BaseIntIdNameEntity psId;
    private Long registerTime;

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

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

    public ProdIdEntity getProductId() {
        return productId;
    }

    public void setProductId(ProdIdEntity productId) {
        this.productId = productId;
    }

    public BaseIntIdNameEntity getPsId() {
        return psId;
    }

    public void setPsId(BaseIntIdNameEntity psId) {
        this.psId = psId;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }
}