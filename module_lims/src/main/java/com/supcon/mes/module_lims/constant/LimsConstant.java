package com.supcon.mes.module_lims.constant;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 业务类型
 */
public interface LimsConstant {

    //检验申请
    interface PleaseCheck{
        String PRODUCT_PLEASE_CHECK = "ProductPleaseCheck";
        String INCOMING_PLEASE_CHECK = "IncomingPleaseCheck";
        String OTHER_PLEASE_CHECK = "OtherPleaseCheck";
    }

    //报告
    interface Report{
        String PRODUCT_REPORT = "ProductReport";
        String INCOMING_REPORT = "IncomingReport";
        String OTHER_REPORT = "OtherReport";
    }

    //样品
    interface Sample{
        String SAMPLE_COLLECTION = "SampleCollection";
        String SAMPLING = "Sampling";
    }

    //工作类型
    interface WorkType{
        String PRODUCT_INSPECT_EDIT = "InspectEdit";
        String PRODUCT_INSPECT_VIEW = "InspectView";
    }
    interface BAPQuery{
        String REGISTER_TIME="REGISTER_TIME";//注册时间
        String C_NAME="C_NAME";//文件名
        String C_MD5="C_MD5";//md5值
        String LIMS_REFED="LIMS_REFED";//读取标记
        String SAMPLING_POINT = "SAMPLING_POINT";
    }


    //样品检验状态
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

    //值类型
    //text,number,calculate,enum
    interface ValueType{
        String TEXT = "LIMSBasic_valueKind/text"; //文本
        String NUMBER = "LIMSBasic_valueKind/number"; //数值
        String CALCULATE = "LIMSBasic_valueKind/calculate"; //计算
        String ENUM = "LIMSBasic_valueKind/enum"; //枚举
    }

    //模块编码
    interface ModuleCode{
        String LIMS_MODULE_CODE = "QCS_5.0.0.0";

        String LIMS_APPLY_ENTITY_CODE = "QCS_5.0.0.0_inspect";
        String LIMS_REPORT_ENTITY_CODE = "QCS_5.0.0.0_inspectReport";

        String LIMS_PRODUCT_APPLY_MENU_CODE = "QCS_5.0.0.0_inspect_manuInspectList";
        String LIMS_INCOMING_APPLY_MENU_CODE = "QCS_5.0.0.0_inspect_purchInspectList";
        String LIMS_OTHER_APPLY_MENU_CODE = "QCS_5.0.0.0_inspect_otherInspectList";

        String LIMS_PRODUCT_REPORT_MENU_CODE = "QCS_5.0.0.0_inspectReport_manuInspReportList";
        String LIMS_INCOMING_REPORT_MENU_CODE = "QCS_5.0.0.0_inspectReport_purchInspReportList";
        String LIMS_OTHER_REPORT_MENU_CODE = "QCS_5.0.0.0_inspectReport_otherInspReportList";
    }

    //配置项KEY
    interface Keys{
        String SPECIAL_RESULT = "LIMSBasic.specialResult";
    }


    interface SampleState{
        String NOT_COLLECTED = "LIMSSample_sampleState/notCollected"; //待取样
        String NOT_RECEIVED = "LIMSSample_sampleState/notReceived"; //待收样
        String NOT_HANDOVER = "LIMSSample_sampleState/notHandover"; //待交接
        String NOT_TESTED = "LIMSSample_sampleState/notTested"; //待检验
        String HALF_TESTED = "LIMSSample_sampleState/halfTested"; //部分已检
        String TESTED = "LIMSSample_sampleState/tested"; //已检验
        String CHECKED = "LIMSSample_sampleState/checked"; //已审核
        String REFUSED = "LIMSSample_sampleState/refused"; //已拒绝
        String CANCELED = "LIMSSample_sampleState/canceled"; //已取消
    }

    interface ReportState{
        String TESTING = "QCS_checkState/testing"; //检验中
        String TEST_COMPLETE = "QCS_checkState/tested"; //已检验
        String REPORTING = "QCS_checkState/reporting"; //报告编制中
        String REPORTED = "QCS_checkState/reported"; //报告已审核
    }

    interface ConclusionType{
        String HIGH_GRADE = "LIMSBasic_standardGrade/highGrade" ; //优等品
        String QUALIFIED = "LIMSBasic_standardGrade/Qualified"; //合格
        String UN_QUALIFIED = "LIMSBasic_standardGrade/Unqualified";//不合格
        String FIRST_GRADE = "LIMSBasic_standardGrade/firstGrade";//一等品
    }

    interface AppCode{
        String LIMS_SerialRef = "LIMS_SerialRef"; //串口采集设备参照
        String LIMS_InspectionItemPda = "LIMS_InspectionItemPda";
        String LIMS_InspectionItem = "LIMS_InspectionItem"; //检验分项
        String LIMS_SAMPLE_FILE_ANALYSE="LIMS_SAMPLE_FILE_ANALYSE";

        String SAMPLE_REF="SAMPLE_REF";
        String SAMPLE_UNIT_REF="SAMPLE_UNIT_REF";
        String WARE_STORE_REF="WARE_STORE_REF";
        String retentionEdit="retentionEdit";
    }
}
