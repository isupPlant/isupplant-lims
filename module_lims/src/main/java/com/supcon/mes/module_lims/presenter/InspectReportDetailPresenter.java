package com.supcon.mes.module_lims.presenter;

import com.google.gson.JsonObject;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.contract.InspectReportDetailContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class InspectReportDetailPresenter extends InspectReportDetailContract.Presenter {
    Map<String, Object> params=new HashMap<>();
    @Override
    public void getInspectReportDetails(int type, Long id) {

        String url=getUrl(type,id);
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .getInspectReportDetails(url, params)
                        .onErrorReturn(error->{
                            InspectReportDetailListEntity inspectReportDetailListEntity=new InspectReportDetailListEntity();
                            inspectReportDetailListEntity.msg=error.getMessage();
                            inspectReportDetailListEntity.success=false;
                            return inspectReportDetailListEntity;
                        })
                        .subscribe(inspectReportDetailListEntity->{
                            if (inspectReportDetailListEntity.success){
                                getView().getInspectReportDetailsSuccess(inspectReportDetailListEntity);
                            }else {
                                getView().getInspectReportDetailsFailed(inspectReportDetailListEntity.msg);
                            }
                        })
                    );
    }
    private String getUrl(int type,Long id){
        String url=null;
        JsonObject customCondition=new JsonObject();
        params.put("pageSize",65535);
        if (type==1){
            params.put("customCondition",customCondition);
            params.put("permissionCode","QCS_5.0.0.0_inspectReport_manuInspReportView");
            url="/msService/QCS/inspectReport/inspectReport/data-dg1591148650933?datagridCode=QCS_5.0.0.0_inspectReport_manuInspReportViewdg1591148650933&id="+id;
        }
        return  url;
    }
}
