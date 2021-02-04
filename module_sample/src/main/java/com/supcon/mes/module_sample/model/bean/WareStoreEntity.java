package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;

/**
 * Created by wanghaidong on 2021/2/3
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class WareStoreEntity extends BaseEntity {
    public BaseSystemBackEntity warehouse;
    public Integer id;
    public String name;
    public String code;
}
