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
        String PRODUCT_INSPECT_EDIT = "manuInspectEdit";
        String PRODUCT_INSPECT_VIEW = "manuInspectView";

        //otherInspectEdit
        //otherInspectView
        //purchInspectEdit
        //purchInspectView

    }
}
