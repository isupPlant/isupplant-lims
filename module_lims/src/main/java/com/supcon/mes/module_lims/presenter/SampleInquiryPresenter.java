package com.supcon.mes.module_lims.presenter;

import com.google.gson.Gson;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.model.bean.ConditionEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
import com.supcon.mes.module_lims.model.contract.SampleInquiryApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/6
 * class name 收样取样列表公用Presenter
 */
public class SampleInquiryPresenter extends SampleInquiryApi.Presenter {
    @Override
    public void getSampleInquiryList(String type, int pageNo, Map<String, Object> params) {
        String query = "";
        String customCondition = "";
        String viewCode = "";
        String modelAlias = "sampleInfo";
        String joinInfo = "LIMSBA_PICKSITE,ID,LIMSSA_SAMPLE_INFOS,PS_ID";
        boolean isName = false;
        FastQueryCondEntity fastQuery;
        if (type.equals(BusinessType.Sample.SAMPLE_COLLECTION)){
            query = "receiveListPart-query";
            customCondition = new Gson().toJson(new ConditionEntity("sampleReceive"));
            viewCode = "LIMSSample_5.0.0.0_sample_receiveListLayout";
        }else if (type.equals(BusinessType.Sample.SAMPLING)){
            query = "collectListPart-query";
            customCondition = new Gson().toJson(new ConditionEntity("sampleCollect"));
            viewCode = "LIMSSample_5.0.0.0_sample_collectListLayout";
        }

        Map<String, Object> codeParam = new HashMap();
        //从外层传进的集合中取出 检索条件 CODE | NAME | BATCH_CODE
        if (params.containsKey(Constant.BAPQuery.CODE)) {
            codeParam.put(Constant.BAPQuery.CODE, params.get(Constant.BAPQuery.CODE));
            isName = false;
        }
        if (params.containsKey(Constant.BAPQuery.NAME)){
            codeParam.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
            isName = true;
        }
        if (params.containsKey(Constant.BAPQuery.BATCH_CODE)){
            codeParam.put(Constant.BAPQuery.BATCH_CODE, params.get(Constant.BAPQuery.BATCH_CODE));
            isName = false;
        }

        if (!isName){
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(params);
            fastQuery.viewCode = viewCode;
            fastQuery.modelAlias = modelAlias;
        }else {
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = viewCode;
            //创建fastQuery.subconds内部该有的属性并且赋值
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(codeParam, joinInfo);
            fastQuery.subconds.add(joinSubcondEntity);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond", fastQuery.toString());
        map.put("customCondition",customCondition);
        map.put("pageNo", pageNo);
        map.put("pageSize", 10);
        map.put("paging", true);

        mCompositeSubscription.add(BaseLimsHttpClient.sampleInquiryList(query,map).onErrorReturn(new Function<Throwable, SampleInquiryListEntity>() {
            @Override
            public SampleInquiryListEntity apply(Throwable throwable) throws Exception {
                SampleInquiryListEntity entity = new SampleInquiryListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<SampleInquiryListEntity>() {
            @Override
            public void accept(SampleInquiryListEntity entity) throws Exception {
                if (entity.success){
                    getView().getSampleInquiryListSuccess(entity);
                }else {
                    getView().getSampleInquiryListFailed(entity.msg);
                }
            }
        }));

    }
}
