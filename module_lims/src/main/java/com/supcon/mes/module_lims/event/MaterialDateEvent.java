package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.MaterialReferenceEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/15
 * class name 物料参照 Event
 */
public class MaterialDateEvent extends BaseEntity {
    private boolean radio;
    private List<MaterialReferenceEntity> list;
    private MaterialReferenceEntity data;

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }

    public List<MaterialReferenceEntity> getList() {
        return list;
    }

    public void setList(List<MaterialReferenceEntity> list) {
        this.list = list;
    }

    public MaterialReferenceEntity getData() {
        return data;
    }

    public void setData(MaterialReferenceEntity data) {
        this.data = data;
    }
}
