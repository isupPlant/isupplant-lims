package com.supcon.mes.module_lims.presenter;

import com.google.gson.JsonObject;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.contract.InspectReportDetailContract;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class InspectReportDetailPresenter extends InspectReportDetailContract.Presenter {
    Map<String, Object> params = new HashMap<>();

    @Override
    public void getInspectHeadReport(long id) {
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .getInspectHeadReport(id)
                        .onErrorReturn(error -> {
                            BAP5CommonEntity commonEntity = new BAP5CommonEntity();
                            commonEntity.msg = error.getMessage();
                            commonEntity.success = false;
                            return commonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<InspectHeadReportEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<InspectHeadReportEntity> commonEntity) throws Exception {
                                if (commonEntity.success) {
                                    getView().getInspectHeadReportSuccess(commonEntity.data);
                                } else {
                                    getView().getInspectHeadReportFailed(commonEntity.msg);
                                }
                            }
                        })

        );
    }

    @Override
    public void getInspectReportByPending(long moduleId, Long pendingId) {
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .getInspectReportByPending(moduleId, pendingId)
                        .onErrorReturn(error -> {
                            InspectReportEntity commonEntity = new InspectReportEntity();
                            commonEntity.msg = error.getMessage();
                            commonEntity.success = false;
                            return commonEntity;
                        })
                        .subscribe(inspectReportEntity -> {
                            if (inspectReportEntity.success) {
                                getView().getInspectReportByPendingSuccess(inspectReportEntity);
                            } else {
                                getView().getInspectReportByPendingFailed(inspectReportEntity.msg);
                            }
                        })

        );

    }


    @Override
    public void getInspectReportDetails(int type, Long id) {

        String url = getUrl(type, id);
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .getInspectReportDetails(url, params)
                        .onErrorReturn(error -> {
                            InspectReportDetailListEntity inspectReportDetailListEntity = new InspectReportDetailListEntity();
                            inspectReportDetailListEntity.msg = error.getMessage();
                            inspectReportDetailListEntity.success = false;
                            return inspectReportDetailListEntity;
                        })
                        .subscribe(inspectReportDetailListEntity -> {
                            if (inspectReportDetailListEntity.success) {
                                getView().getInspectReportDetailsSuccess(inspectReportDetailListEntity);
                            } else {
                                getView().getInspectReportDetailsFailed(inspectReportDetailListEntity.msg);
                            }
                        })
        );
    }

    @Override
    public void submitInspectReport(String path, Map<String, Object> params, InspectReportSubmitEntity reportSubmitEntity) {
        mCompositeSubscription.add(
                BaseLimsHttpClient
                        .submitInspectReport(path, params, reportSubmitEntity)
                        .onErrorReturn(error -> {
                            SubmitResultEntity entity = new SubmitResultEntity();
                            if (error.getMessage().contains("503")) {
                                entity.msg = "抱歉，服务不存在或者未启动";
                            } else {
                                entity.msg = error.getMessage();
                            }
                            entity.success = false;
                            return entity;
                        })
                        .subscribe(new Consumer<SubmitResultEntity>() {
                            @Override
                            public void accept(SubmitResultEntity submitResultEntity) throws Exception {
                                if (submitResultEntity.success) {
                                    getView().submitInspectReportSuccess(submitResultEntity);
                                } else {
                                    getView().submitInspectReportFailed(submitResultEntity.msg);
                                }
                            }
                        })
        );
    }

    private String getUrl(int type, Long id) {
        String url = null;
        JsonObject customCondition = new JsonObject();
//        params.put("pageSize", 65535);
        params.put("customCondition", customCondition);
        if (type == 1) {
            params.put("permissionCode", "QCS_5.0.0.0_inspectReport_manuInspReportView");
            url = "/msService/QCS/inspectReport/inspectReport/data-dg1591148650933?datagridCode=QCS_5.0.0.0_inspectReport_manuInspReportViewdg1591148650933&id=" + id;
        } else if (type == 2) {
            params.put("permissionCode", "QCS_5.0.0.0_inspectReport_purchInspReportView");
            url = "/msService/QCS/inspectReport/inspectReport/data-dg1589180035292?datagridCode=QCS_5.0.0.0_inspectReport_purchInspReportViewdg1589180035292&id=" + id;
        } else if (type == 3) {
            params.put("permissionCode", "QCS_5.0.0.0_inspectReport_otherInspReportView");
            url = "/msService/QCS/inspectReport/inspectReport/data-dg1591951581263?datagridCode=QCS_5.0.0.0_inspectReport_otherInspReportViewdg1591951581263&id=" + id;
        }
        return url;
    }
}
