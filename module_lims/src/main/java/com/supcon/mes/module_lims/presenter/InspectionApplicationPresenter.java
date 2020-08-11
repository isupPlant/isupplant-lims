package com.supcon.mes.module_lims.presenter;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_lims.constant.BusinessType;

import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.contract.InspectionApplicationApi;
import com.supcon.mes.module_lims.model.network.BaseLimsHttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 检验申请列表Presenter
 */
public class InspectionApplicationPresenter extends InspectionApplicationApi.Presenter {
    @Override
    public void getInspectionApplicationList(String type, boolean isAll, int pageNo, Map<String, Object> params) {
        String query = "";
        String viewCode = "";
        String joinInfo = "BASESET_MATERIALS,ID,QCS_INSPECTS,PROD_ID";
        String modelAlias = "inspect";
        FastQueryCondEntity fastQuery;
        if (type.equals(BusinessType.PleaseCheck.PRODUCT_PLEASE_CHECK)){
            if (isAll){
                query = "manuInspectList-query";
            }else {
                query = "manuInspectList-pending";
            }

            viewCode = "QCS_5.0.0.0_inspect_manuInspectList";
        }else if (type.equals(BusinessType.PleaseCheck.INCOMING_PLEASE_CHECK)){
            if (isAll){
                query = "purchInspectList-query";
            }else {
                query = "purchInspectList-pending";
            }

            viewCode = "QCS_5.0.0.0_inspect_purchInspectList";
        }else if (type.equals(BusinessType.PleaseCheck.OTHER_PLEASE_CHECK)){
            if (isAll){
                query = "otherInspectList-query";
            }else {
                query = "otherInspectList-pending";
            }

            viewCode = "QCS_5.0.0.0_inspect_otherInspectList";
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
        if (params.containsKey(Constant.BAPQuery.TABLE_NO)){
            //创建一个空的实体类
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(codeParam);
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = viewCode;
        }else {
            //创建一个空的实体类
            fastQuery = BAPQueryParamsHelper.createSingleFastQueryCond(new HashMap<>());
            fastQuery.modelAlias = modelAlias;
            fastQuery.viewCode = viewCode;
            //创建fastQuery.subconds内部该有的属性并且赋值
            JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(codeParam, joinInfo);
            fastQuery.subconds.add(joinSubcondEntity);
        }



        Map<String, Object> map = new HashMap<>();
        map.put("fastQueryCond", fastQuery.toString());
        map.put("pageNo", pageNo);
        map.put("pageSize", 10);
        map.put("paging", true);

        mCompositeSubscription.add(BaseLimsHttpClient.inspectionRequestList(query,map).onErrorReturn(new Function<Throwable, InspectionApplicationListEntity>() {
            @Override
            public InspectionApplicationListEntity apply(Throwable throwable) throws Exception {
                InspectionApplicationListEntity entity = new InspectionApplicationListEntity();
                entity.msg = throwable.getMessage();
                entity.success = false;
                return entity;
            }
        }).subscribe(new Consumer<InspectionApplicationListEntity>() {
            @Override
            public void accept(InspectionApplicationListEntity entity) throws Exception {
                if (entity.success){
                    getView().getInspectionApplicationListSuccess(entity);
                }else {
                    getView().getInspectionApplicationListFailed(entity.msg);
                }
            }
        }));

    }
}
