package com.supcon.mes.module_lims.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.contract.SurveyReportApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 检验报告单presenter
 */
public class SurveyReportPresenter extends SurveyReportApi.Presenter {
    @Override
    public void getSurveyReportList(String type, boolean isAll, int pageNo, Map<String, Object> params) {
        String query = "";
        String viewCode = "";
        String joinInfo = "QCS_INSPECTS,ID,QCS_INSPECT_REPORTS,INSPECT_ID";
        String modelAlias = "inspectReport";
        if (type.equals(BusinessType.Report.PRODUCT_REPORT)){
            if (isAll){
                query = "manuInspReportList-query";
            }else {
                query = "manuInspReportList-pending";
            }
            viewCode = "QCS_5.0.0.0_inspectReport_manuInspReportList";
        }else if (type.equals(BusinessType.Report.INCOMING_REPORT)){
            if (isAll){
                query = "purchInspReportList-query";
            }else {
                query = "purchInspReportList-pending";
            }
            viewCode = "QCS_5.0.0.0_inspectReport_purchInspReportList";
        }else if (type.equals(BusinessType.Report.OTHER_REPORT)){
            if (isAll){
                query = "otherInspReportList-query";
            }else {
                query = "otherInspReportList-pending";
            }
            viewCode = "QCS_5.0.0.0_inspectReport_otherInspReportList";
        }

        Map<String, Object> firstParams = new HashMap();
        Map<String,Object> secondParams=new HashMap<>();

        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE | TABLE_NO
        if (params.containsKey(Constant.BAPQuery.TABLE_NO)){
            firstParams.put(Constant.BAPQuery.TABLE_NO, params.get(Constant.BAPQuery.TABLE_NO));
        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            firstParams.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
        }
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(firstParams);
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            secondParams.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"BASESET_MATERIALS,ID,QCS_INSPECT_REPORTS,PROD_ID"));
        }
        if (params.containsKey(Constant.BAPQuery.NAME)) {
            secondParams.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,"BASESET_MATERIALS,ID,QCS_INSPECT_REPORTS,PROD_ID"));
        }

        if (params.containsKey("INSPECT_TABLE_NO")){
            secondParams.put(Constant.BAPQuery.TABLE_NO, params.get("INSPECT_TABLE_NO"));
            fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,joinInfo));
        }

        //创建一个空的实体类

        fastQuery.modelAlias = modelAlias;
        fastQuery.viewCode = viewCode;
        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond", fastQuery.toString());
        map.put("pageNo", pageNo);
        map.put("pageSize", 10);
        map.put("paging", true);

        mCompositeSubscription.add(BaseLimsHttpClient.surveyReportList(query,map).onErrorReturn(new Function<Throwable, SurveyReportListEntity>() {
            @Override
            public SurveyReportListEntity apply(Throwable throwable) throws Exception {
                SurveyReportListEntity entity = new SurveyReportListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<SurveyReportListEntity>() {
            @Override
            public void accept(SurveyReportListEntity entity) throws Exception {
                if (entity.success){
                    if (entity.data!=null) {
                        entity.data.result=filterSurveyReports(entity.data.result);
                    }
                    getView().getSurveyReportListSuccess(entity);
                }else {
                    getView().getSurveyReportListFailed(entity.msg);
                }
            }
        }));
    }
    private List<SurveyReportEntity> filterSurveyReports(List<SurveyReportEntity> entities){
        List<SurveyReportEntity> list=new ArrayList<>();
        if (!entities.isEmpty()) {
            for (SurveyReportEntity entity : entities) {
                if (TextUtils.isEmpty(entity.pending.openUrl) || entity.pending.openUrl.contains("ReportView")) {
                    list.add(entity);
                }
            }
        }
        return list;
    }
}
