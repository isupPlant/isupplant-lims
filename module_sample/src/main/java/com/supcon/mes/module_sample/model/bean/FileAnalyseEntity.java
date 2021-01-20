package com.supcon.mes.module_sample.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wanghaidong on 2021/1/18
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class FileAnalyseEntity extends BaseEntity {
    public Long id;
    public String collectCode;  //采集码
    public String filePath;    //文件路径
    public String md5;        //md5值
    public String name;      //参照文件名称

    @Expose(serialize = false)
    public boolean check;
}
