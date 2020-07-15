package com.supcon.mes.module_sample.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_sample.model.contract.SampleSurveyReportApi;
import com.supcon.mes.module_sample.model.network.SampleHttpClient;

import java.util.HashMap;
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

        Map<String, Object> codeParam = new HashMap();
        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE | TABLE_NO
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            codeParam.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
        }
        if (params.containsKey(Constant.BAPQuery.NAME)) {
            codeParam.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            codeParam.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
        }
        if (params.containsKey(Constant.BAPQuery.TABLE_NO)){
            codeParam.put(Constant.BAPQuery.TABLE_NO, params.get(Constant.BAPQuery.TABLE_NO));
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
                    getView().getSampleSurveyReportListSuccess(entity);
                }else {
                    getView().getSampleSurveyReportListFailed(entity.msg);
                }
            }
        }));
    }
}
