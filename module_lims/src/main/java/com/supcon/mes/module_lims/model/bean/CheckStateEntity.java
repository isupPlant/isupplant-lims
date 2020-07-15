package com.supcon.mes.module_lims.model.bean;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
public class CheckStateEntity extends BaseSystemBackStringIdEntity {
    private Long cid;
    private String layRec;
    private Integer layNo;
    private String fullPathName;
    private String entityCode;
    private Integer sort;
    private Long seqId;
    private String uniqueCode;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
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

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
}
