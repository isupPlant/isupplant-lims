package com.supcon.mes.module_sample.presenter;

import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_sample.model.contract.SampleSurveyReportApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/8
 * class name
 */
public class SampleSurveyReportPresenter extends SampleSurveyReportApi.Presenter {
    @Override
    public void getSampleSurveyReportList(boolean isAll, int pageNo, Map<String, Object> params) {
        String query = "";
        String joinInfo = "LIMSSA_SAMPLE_INFOS,ID,LIMSSA_SAMPLE_REPORTS,SAMPLE_ID";
        String viewCode = "LIMSSample_5.0.0.0_sampleReport_sampleReportList";
        String modelAlias = "sampleReport";
        if (isAll){
            query = "sampleReportList-query";
        }else {
            query = "sampleReportList-pending";
        }

        Map<String, Object> firstParams = new HashMap();
        Map<String,Object> secondParams=new HashMap<>();

        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE | TABLE_NO
        if (params.containsKey(Constant.BAPQuery.TABLE_NO)){
            firstParams.put(Constant.BAPQuery.TABLE_NO, params.get(Constant.BAPQuery.TABLE_NO));
        }
        FastQueryCondEntity fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(firstParams);
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            secondParams.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
        }
        if (params.containsKey(Constant.BAPQuery.NAME)) {
            secondParams.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            secondParams.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
        }
        fastQuery.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(secondParams,joinInfo));
        if (params.containsKey(Constant.BAPQuery.PICKSITE)){
            Map<String,Object> thirdParams=new HashMap<>();
            thirdParams.put(Constant.BAPQuery.PICKSITE,params.get(Constant.BAPQuery.PICKSITE));
            JoinSubcondEntity subcondEntity = new JoinSubcondEntity();
            subcondEntity.subconds=new ArrayList<>();
            subcondEntity.type = "2";
            subcondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(thirdParams,"LIMSBA_PICKSITE,ID,LIMSSA_SAMPLE_INFOS,PS_ID"));
            fastQuery.subconds.add(subcondEntity);
        }

        //创建一个空的实体类

        fastQuery.modelAlias = modelAlias;
        fastQuery.viewCode = viewCode;
        //创建fastQuery.subconds内部该有的属性并且赋值

        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond", fastQuery.toString());
        map.put("pageNo", pageNo);
        map.put("pageSize", 10);
        map.put("paging", true);

        mCompositeSubscription.add(SampleHttpClient.getSampleSurveyReportList(query,map).onErrorReturn(new Function<Throwable, SurveyReportListEntity>() {
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
                    getView().getSampleSurveyReportListSuccess(entity);
                }else {
                    getView().getSampleSurveyReportListFailed(entity.msg);
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
