package com.supcon.mes.module_lims.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceListEntity;
import com.supcon.mes.module_lims.model.contract.QualityStandardReferenceApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;
import com.supcon.mes.module_lims.utils.BAPQueryHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name
 */
public class QualityStandardReferencePresenter extends QualityStandardReferenceApi.Presenter {
    @Override
    public void getQualityStandardReferenceList(int pageNo, String id, Map<String, Object> params) {
        String viewCode = "LIMSBasic_1.0.0_qualityStd_qualityStdVerRef";
        String modelAlias = "stdVersion";
        String joinInfo = "LIMSBA_QUALITY_STDS,ID,LIMSBA_STD_VERSIONS,STD_ID";
        FastQueryCondEntity fastQuery;
        boolean isName = false;

        Map<String, Object> codeParam = new HashMap();
        if (params.containsKey(Constant.BAPQuery.NAME)){
            codeParam.put(Constant.BAPQuery.NAME, params.get(Constant.BAPQuery.NAME));
            isName = true;
        }
        if (params.containsKey(Constant.BAPQuery.BUSI_VERSION)){
            codeParam.put(Constant.BAPQuery.BUSI_VERSION, params.get(Constant.BAPQuery.BUSI_VERSION));
            isName = false;
        }

        if (!isName){
            fastQuery = BAPQueryHelper.createSingleFastQueryCond(params);
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
        if (params.size()>0){
            map.put("fastQueryCond",fastQuery.toString());
        }
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("analySampleId",id);
        map.put("customCondition",conditionMap);
        map.put("pageNo",pageNo);
        map.put("paging",true);
        map.put("pageSize",10);

        mCompositeSubscription.add(BaseLimsHttpClient.getQualityStandard(map).onErrorReturn(new Function<Throwable, QualityStandardReferenceListEntity>() {
            @Override
            public QualityStandardReferenceListEntity apply(Throwable throwable) throws Exception {
                QualityStandardReferenceListEntity entity = new QualityStandardReferenceListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<QualityStandardReferenceListEntity>() {
            @Override
            public void accept(QualityStandardReferenceListEntity entity) throws Exception {
                if (entity.success){
                    getView().getQualityStandardReferenceListSuccess(entity);
                }else {
                    getView().getQualityStandardReferenceListFailed(entity.msg);
                }
            }
        }));
    }
}
