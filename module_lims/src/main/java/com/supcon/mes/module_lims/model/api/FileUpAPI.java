package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.io.File;
import java.util.List;

/**
 * Created by wanghaidong on 2020/8/25
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {List.class})
public interface FileUpAPI {
    //void upFile(File file);
    void loadFile(List<String> id, List<String> fileName);
}
