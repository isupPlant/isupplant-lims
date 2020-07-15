package com.supcon.mes.module_lims.model.bean;


/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class SexEntity extends BaseSystemBackStringIdEntity {
    private Integer cid;
    private String layRec;
    private Integer layNo;
    private Boolean leaf;
    private Boolean leaf2;
    private String entityCode;
    private Boolean attribute;
    private Boolean defaultFlag;
    private Long seqId;
    private String uniqueCode;
    private Boolean root;
    private Boolean isParent;


    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }


    public String getLayRec() {
        return layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

    public Integer getLayNo() {
        return layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getLeaf2() {
        return leaf2;
    }

    public void setLeaf2(Boolean leaf2) {
        this.leaf2 = leaf2;
    }


    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }


    public Boolean getAttribute() {
        return attribute;
    }

    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    public Boolean getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }
}
