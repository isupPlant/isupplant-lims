package com.supcon.mes.module_lims.event;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.ProdIdEntity;
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class SampleMaterialDataEvent extends BaseEntity {
    private boolean radio;
    private List<SampleMaterialEntity> list;
    private SampleMaterialEntity data;

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }

    public List<SampleMaterialEntity> getList() {
        return list;
    }

    public void setList(List<SampleMaterialEntity> list) {
        this.list = list;
    }

    public SampleMaterialEntity getData() {
        return data;
    }

    public void setData(SampleMaterialEntity data) {
        this.data = data;
    }
}
