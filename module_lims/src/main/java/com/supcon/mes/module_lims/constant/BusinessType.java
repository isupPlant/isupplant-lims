package com.supcon.mes.module_lims.constant;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 业务类型
 */
public interface BusinessType {
    interface PleaseCheck{
        String PRODUCT_PLEASE_CHECK = "ProductPleaseCheck";
        String INCOMING_PLEASE_CHECK = "IncomingPleaseCheck";
        String OTHER_PLEASE_CHECK = "OtherPleaseCheck";
    }

    interface Report{
        String PRODUCT_REPORT = "ProductReport";
        String INCOMING_REPORT = "IncomingReport";
        String OTHER_REPORT = "OtherReport";
    }

    interface Sample{
        String SAMPLE_COLLECTION = "SampleCollection";
        String SAMPLING = "Sampling";
    }

    interface WorkType{
        String PRODUCT_INSPECT_EDIT = "InspectEdit";
        String PRODUCT_INSPECT_VIEW = "InspectView";
    }


    //notTested,halfTested,tested,checked,refused,canceled,notChecked
    interface SampleTestState{
        String NOT_TESTED = "LIMSSample_testState/notTested"; //待检验
        String HALF_TESTED = "LIMSSample_testState/halfTested"; //部分已检
        String TESTED = "LIMSSample_testState/tested"; //已检验
        String NOT_CHECKED = "LIMSSample_testState/notChecked";//待复核
        String CHECKED = "LIMSSample_testState/checked"; //已复核
        String REFUSED = "LIMSSample_testState/refused"; //已拒绝
        String CANCELED = "LIMSSample_testState/canceled"; //已取消

    }

    //text,number,calculate,enum
    interface ValueType{
        String TEXT = "LIMSBasic_valueKind/text"; //文本
        String NUMBER = "LIMSBasic_valueKind/number"; //数值
        String CALCULATE = "LIMSBasic_valueKind/calculate"; //计算
        String ENUM = "LIMSBasic_valueKind/enum"; //枚举
    }
}
