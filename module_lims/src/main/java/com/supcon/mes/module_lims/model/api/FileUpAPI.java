package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.io.File;

/**
 * Created by wanghaidong on 2020/8/25
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {BAP5CommonEntity.class,File.class})
public interface FileUpAPI {
    void upFile(File file);
    void loadFile(String id,String fileName);
}
