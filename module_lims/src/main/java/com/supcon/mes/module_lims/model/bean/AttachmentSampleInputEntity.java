package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.io.File;

/**
 * Created by wanghaidong on 2020/9/3
 * Email:wanghaidong1@supcon.com
 */
public class AttachmentSampleInputEntity extends BaseEntity {
    private String id;
    private String name;
    private File file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
