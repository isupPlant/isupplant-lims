package com.supcon.mes.module_retention.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_lims.model.bean.ProductIdEntity;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.middleware.model.bean.SafetyHiddenEntity;
import com.supcon.mes.middleware.model.bean.Unit;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_lims.model.bean.ProductIdEntity;
import com.supcon.mes.module_lims.model.bean.SampleIdEntity;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RetentionEntity extends BaseEntity {
    public Long id;
    public SampleEntity sampleId;
    public ProductIdEntity productId;
    public BaseSystemBackEntity keeperId;//留样人
    public PendingEntity pending;
    public Long retainDate;//留样日期
    public Long validDate;//到期日期
    public Integer retainDays;//到期期限
    public BaseIdValueEntity retainUnit;//留样期限单位
    public String envDesire;//存放要求
    public BaseIntIdNameEntity storeSetId;//存放位置
    public BaseSystemBackEntity unitId;//计量单位
    public Float stockQty;//现存量
    public Float retainQty;//留样数量
    public BaseSystemBackEntity staffId;//留样人
    public DepartmentEntity deptId;//留样部门
    public Long remainDate;//剩余天数
    public Long tableInfoId;
    public String tableNo;
    public int version;
    public String batchCode;//批号
    public String memoField;//备注
}
