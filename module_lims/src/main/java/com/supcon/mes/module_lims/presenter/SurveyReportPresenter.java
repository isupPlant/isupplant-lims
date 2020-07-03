package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.contract.SurveyReportApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
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
    public void getSurveyReportList(String type, int pageNo, Map<String, Object> params) {
        String query = "";
        String viewCode = "";
        String joinInfo = "BASESET_MATERIALS,ID,QCS_INSPECT_REPORTS,PROD_ID";
        String modelAlias = "inspectReport";
        if (type.equals(BusinessType.Report.PRODUCT_REPORT)){
            query = "manuInspReportList-query";
            viewCode = "QCS_5.0.0.0_inspectReport_manuInspReportList";
        }else if (type.equals(BusinessType.Report.INCOMING_REPORT)){
            query = "purchInspReportList-query";
            viewCode = "QCS_5.0.0.0_inspectReport_purchInspReportList";
        }else if (type.equals(BusinessType.Report.OTHER_REPORT)){
            query = "otherInspReportList-query";
            viewCode = "QCS_5.0.0.0_inspectReport_otherInspReportList";
        }

        Map<String, Object> codeParam = new HashMap();
        //从外层传进的集合中取出 检索条件 CODE | NAME
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            codeParam.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
        }
        if (params.containsKey(Constant.BAPQuery.NAME)) {
            codeParam.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
        }

        //创建一个空的实体类
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
        fastQuery.modelAlias = modelAlias;
        fastQuery.viewCode = viewCode;
        //创建fastQuery.subconds内部该有的属性并且赋值
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(codeParam, joinInfo);
        fastQuery.subconds.add(joinSubcondEntity);

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
                    getView().getSurveyReportListSuccess(entity);
                }else {
                    getView().getSurveyReportListFailed(entity.msg);
                }
            }
        }));


    }
}
